package taskmanager.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import jakarta.transaction.Transactional;
import taskmanager.dto.TaskDTO;
import taskmanager.dto.TaskResponse;
import taskmanager.dto.UserResponse;
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
		 if (taskRepository.existsByTaskName(taskDTO.getTaskName())) {
		        throw new IllegalArgumentException("Username already exists!");
		 }
	   
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
	    task.setCompleted(taskDTO.isCompleted()); //added
	    task.setCategory(category);
	  
	    Task savedTask = taskRepository.save(task);
	    
	    return modelMapper.map(savedTask, TaskDTO.class);
	}


	@Override
	public TaskDTO updateTask(Long taskId, TaskDTO taskDTO) {

		if (taskDTO.getCategoryId() == null || taskDTO.getCategoryId() == null) {
	        throw new IllegalArgumentException("Category id is required");
	    }
		if (taskDTO.getDueDate() != null && taskDTO.getDueDate().before(new Date())) {
	        throw new IllegalArgumentException("Due date must be in the future!");
	    }
	    categoryRepository.findById(taskDTO.getCategoryId())
	        .orElseThrow(() -> new IllegalArgumentException("Category id not found: " + taskDTO.getCategoryId()));
	   
	    if (taskRepository.existsByTaskName(taskDTO.getTaskName())) {
	        throw new IllegalArgumentException("Username already exists!");
	    }
	   
	    Task updatedTask = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task", "task id", taskId));
		updatedTask.setTaskName(taskDTO.getTaskName());
	    updatedTask.setDescription(taskDTO.getDescription());
		updatedTask.setDueDate(taskDTO.getDueDate());
		updatedTask.setTitle(taskDTO.getTitle());
	    updatedTask.setCategoryId(taskDTO.getCategoryId());
	    updatedTask.setCompleted(taskDTO.isCompleted());
		
		taskRepository.save(updatedTask);
	
		return modelMapper.map(updatedTask, TaskDTO.class);
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

		        List<UserResponse> userSummaries = task.getAssignedUsers().stream()
		            .map(user -> new UserResponse(user.getUserId(), user.getUsername()))
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


	@Override
	public List<TaskResponse> findIdleTasks() {
	    List<TaskResponse> idleTasks = new ArrayList<>();
	    
	    // Creating this custom is more efficient that pulling and processing
	    // all user data
	    List<Task> tasks = taskRepository.findByAssignedUsersIsEmpty();

	    if (tasks.isEmpty()) {
	    	throw new ResourceNotFoundException("No idle tasks!");
	    }
	    
	    for (Task task : tasks) {
	        TaskResponse taskResponse = new TaskResponse();
	        taskResponse.setTaskId(task.getTaskId());
	        taskResponse.setTaskName(task.getTaskName());
	        taskResponse.setDueDate(task.getDueDate());
	        idleTasks.add(taskResponse);
	    }

	    return idleTasks;
	}

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
	public List<UserResponse> getUsersAssignedToTask(Long taskId) {
	    List<UserResponse> users = taskRepository.findUsersByTaskId(taskId)
	        .stream()
	        .map(user -> new UserResponse(user.getUserId(), user.getUsername()))
	        .collect(Collectors.toList());

	    if (users.isEmpty()) 
	        throw new ResourceNotFoundException("Users", "for the task", taskId);

	    return users;
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
