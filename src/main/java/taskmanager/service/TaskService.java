package taskmanager.service;


import java.util.Date;
import java.util.List;

import taskmanager.dto.TaskDTO;
import taskmanager.dto.TaskResponse;
import taskmanager.dto.UserResponse;
import taskmanager.entity.Task;


public interface TaskService {

	
	TaskDTO createTask(TaskDTO taskDTO);

	List<TaskDTO> findAllTasks(Long userId, Boolean completed, Long categoryId, String sortBy, String sortDirection);

	void deleteTaskById(Long taskId);

	List<TaskResponse> findIdleTasks();

	List<Task> filterTasks(Boolean completed, Date dueDate, Long categoryId, Long userId);

	List<UserResponse> getUsersAssignedToTask(Long taskId);

	void assignToUser(Long taskId, Long userId);

	void unassignTask(Long taskId, Long userId);

	TaskDTO updateTask(Long taskId, TaskDTO taskDTO);

	
}
