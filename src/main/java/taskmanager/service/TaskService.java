package taskmanager.service;


import java.util.Date;
import java.util.List;

import taskmanager.dto.TaskDTO;
import taskmanager.dto.UserResponse;
import taskmanager.entity.Task;


public interface TaskService {

	
	TaskDTO createTask(TaskDTO taskDTO);

	Task updateTask(Long taskId, Task task);

	List<TaskDTO> findAllTasks(Long userId, Boolean completed, Long categoryId, String sortBy, String sortDirection);

	void deleteTaskById(Long taskId);

	List<Task> findIdleTasks();

	List<Task> filterTasks(Boolean completed, Date dueDate, Long categoryId, Long userId);

	List<UserResponse> getUsersAssignedToTask(Long taskId);

	void assignToUser(Long taskId, Long userId);

	void unassignTask(Long taskId, Long userId);

	
}
