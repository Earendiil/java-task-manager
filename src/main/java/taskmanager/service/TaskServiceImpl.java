package taskmanager.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import jakarta.transaction.Transactional;
import taskmanager.dto.TaskDTO;
import taskmanager.dto.UserIdDTO;
import taskmanager.entity.Category;
import taskmanager.entity.Task;
import taskmanager.entity.User;
import taskmanager.exceptions.ResourceNotFoundException;
import taskmanager.repositories.CategoryRepository;
import taskmanager.repositories.TaskRepository;
import taskmanager.repositories.UserRepository;

@Service
public class TaskServiceImpl implements TaskService{
	
	private final CategoryRepository categoryRepository;
	private final TaskRepository taskRepository;
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	User user;
	
	


	public TaskServiceImpl(CategoryRepository categoryRepository, TaskRepository taskRepository,
			UserRepository userRepository, ModelMapper modelMapper) {
		super();
		this.categoryRepository = categoryRepository;
		this.taskRepository = taskRepository;
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}


	@Override
	public TaskDTO createTask(TaskDTO taskDTO) {
	    if (taskDTO.getCategoryId() == null) {
	        throw new IllegalArgumentException("Category ID is required");
	    }

	    Category category = categoryRepository.findById(taskDTO.getCategoryId())
	        .orElseThrow(() -> new IllegalArgumentException("Category ID not found: " + taskDTO.getCategoryId()));

	    // Convert DTO to Entity
	    Task task = new Task();
	    task.setTaskName(taskDTO.getTaskName());
	    task.setTitle(taskDTO.getTitle());
	    task.setDescription(taskDTO.getDescription());
	    task.setDueDate(taskDTO.getDueDate());
	    task.setCompleted(taskDTO.isCompleted());
	    task.setCategory(category);
	  
	    Task savedTask = taskRepository.save(task);
	    
	    return modelMapper.map(savedTask, TaskDTO.class);
	}


	@Override
	public Task updateTask(Long taskId, Task task) {

		if (task.getCategory() == null || task.getCategory().getCategoryId() == null) {
	        throw new IllegalArgumentException("Category id is required");
	    }
		if (task.getDueDate() != null && task.getDueDate().before(new Date())) {
			throw new IllegalArgumentException("Due date must ne in the future!");
		}
			
	    categoryRepository.findById(task.getCategory().getCategoryId())
	        .orElseThrow(() -> new IllegalArgumentException("Category id not found: " + task.getCategory().getCategoryId()));
	 
	    
	    Task updatedTask = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task", "task id", taskId));
		updatedTask.setDescription(task.getDescription());
		updatedTask.setDueDate(task.getDueDate());
		updatedTask.setTitle(task.getTitle());
	    updatedTask.setCategoryId(task.getCategoryId());
		
		taskRepository.save(updatedTask);
	
	return updatedTask;
	}


	@Override
	public List<TaskDTO> findAllTasks(Long userId, Boolean completed, Long categoryId, String sortBy, String sortDirection) {
		List<Task> tasks = taskRepository.findAll();
		if (tasks.isEmpty()) {
			throw new RuntimeException("No tasks exist!");
		}
		
		    if (completed != null) {
		        tasks = tasks.stream()
		                     .filter(task -> task.isCompleted() == completed)
		                     .collect(Collectors.toList());
		    }
		    if (categoryId != null) {
		        tasks = tasks.stream()
		                     .filter(task -> task.getCategory() != null && task.getCategory().getCategoryId().equals(categoryId))
		                     .collect(Collectors.toList());
		    }

		    // Apply sorting
		    if (sortBy != null && sortDirection != null) {
		        if ("asc".equalsIgnoreCase(sortDirection)) {
		            tasks = tasks.stream()
		                         .sorted(Comparator.comparing(Task::getDueDate))  // Change to the appropriate field
		                         .collect(Collectors.toList());
		        } else if ("desc".equalsIgnoreCase(sortDirection)) {
		            tasks = tasks.stream()
		                         .sorted(Comparator.comparing(Task::getDueDate).reversed())  // Change to the appropriate field
		                         .collect(Collectors.toList());
		        }
		        }
		    
		    List<TaskDTO> dtos = tasks.stream().map(task -> {
		        TaskDTO dto = new TaskDTO();
		        dto.setTaskId(task.getTaskId());
		        dto.setTaskName(task.getTaskName());
		        dto.setTitle(task.getTitle());
		        dto.setDescription(task.getDescription());
		        dto.setDueDate(task.getDueDate());
		        dto.setCompleted(task.isCompleted());
		        dto.setCategoryId(task.getCategory().getCategoryId());

		        List<UserIdDTO> userSummaries = task.getAssignedUsers().stream()
		            .map(user -> new UserIdDTO(user.getUserId(), user.getUsername()))
		            .collect(Collectors.toList());

		        dto.setAssignedUsers(userSummaries);

		        return dto;
		    }).collect(Collectors.toList());
			return dtos;
	
	}
	


	@Override
	public void deleteTaskById(Long taskId) {
		Task updatedTask = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task", "task id", taskId));
		taskRepository.delete(updatedTask);
	}

//
//	@Override
//	public List<Task> findIdleTasks() {
//	
//		List<Task> idleTasks = new ArrayList<>();
//		List<Task> tasks = taskRepository.findAll();
//		if (tasks.isEmpty()) {
//			throw new RuntimeException("No tasks exist!");
//		}
//		for (Task task : tasks) {
//			if (task.getUser() == null) {
//				idleTasks.add(task);
//			}
//		}
//		return idleTasks;
//	}

	public List<Task> filterTasks(Boolean completed, Date dueDate, Long categoryId, Long userId) {
        if (completed != null) {
            return taskRepository.findByCompleted(completed);
        } else if (dueDate != null) {
            return taskRepository.findByDueDateBefore(dueDate);
        } else if (categoryId != null) {
            return taskRepository.findByCategory_CategoryId(categoryId);
        } else if (userId != null) {
            return taskRepository.findByUser_UserId(userId);
        }
        return taskRepository.findAll(); // Return all tasks if no filter is applied
    }
	

	@Override
	public List<User> getUsersAssignedToTask(Long taskId) {
		List<User> users = taskRepository.findUsersByTaskId(taskId);
		System.out.println("Users found: " + users);

		if (users == null || users.isEmpty()) 
		    throw new ResourceNotFoundException("No users assigned");
		return 	users;
	}

	@Override
	public void assignToUser(Long taskId, Long userId) {
		Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found!"));
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
		if (task.getAssignedUsers() == null) {
	        task.setAssignedUsers(new ArrayList<>());
	    }
		if (!task.getAssignedUsers().contains(user)) {
			task.getAssignedUsers().add(user);
		}
		if (user.getTasks().isEmpty()) {
			user.setTasks(new ArrayList<>());
		}
		if (!user.getTasks().contains(task)) {
			user.getTasks().add(task);
		}
		userRepository.save(user);
		taskRepository.save(task);
		System.out.println("Added");
	}

	@Override
	public List<Task> findIdleTasks() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Transactional
	@Override
	public void unassignTask(Long taskId, Long userId) {
		Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found!"));
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
		
		task.getAssignedUsers().remove(user);
		user.getTasks().remove(task);
		
		taskRepository.save(task);
	    userRepository.save(user);
		
	}
}
