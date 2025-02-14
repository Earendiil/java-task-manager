package taskmanager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import taskmanager.entity.Task;
import java.util.List;
import java.util.Date;



public interface TaskRepository extends JpaRepository<Task, Long>{

	List<Task> findByCompleted(boolean completed);
	
	List<Task> findByDueDateBefore(Date dueDate);
	
	List<Task> findByCategory_CategoryId(Long categoryId);
	
	@Query("SELECT t FROM Task t WHERE t.user.userId = :userId")
	List<Task> findByUserId(Long userId);
	
	
	
}
