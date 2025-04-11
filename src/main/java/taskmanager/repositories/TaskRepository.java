package taskmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import taskmanager.entity.Task;
import taskmanager.entity.User;

import java.util.List;
import java.util.Date;



public interface TaskRepository extends JpaRepository<Task, Long>{

	List<Task> findByCompleted(boolean completed);
	
	List<Task> findByDueDateBefore(Date dueDate);
	
	List<Task> findByCategory_CategoryId(Long categoryId);
	
	List<Task> findByUser_UserId(Long userId);

	@Query("SELECT u FROM User u JOIN u.tasks t WHERE t.taskId = :taskId")
	List<User> findUsersByTaskId(Long taskId);

	List<Task> findByAssignedUsersIsEmpty();

	boolean existsByTaskName(String taskName);
	
	
	
}
