package taskmanager.service;


import java.util.Date;
import java.util.List;

import jakarta.validation.Valid;
import taskmanager.entity.Task;
import taskmanager.entity.User;


public interface TaskService {

	
	Task createTask(@Valid Task task);

	Task updateTask(Long taskId, Task task);

	List<Task> findAllTasks(Long userId, Boolean completed, Long categoryId, String sortBy, String sortDirection);

	void deleteTaskById(Long taskId);

	List<Task> findIdleTasks();

	List<Task> filterTasks(Boolean completed, Date dueDate, Long categoryId, Long userId);

	List<User> getUsersAssignedToTask(Long taskId);

	void assignToUser(Long taskId, Long userId);

	
}
