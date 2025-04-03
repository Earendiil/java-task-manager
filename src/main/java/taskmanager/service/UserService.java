package taskmanager.service;

import java.util.List;

import taskmanager.entity.Task;
import taskmanager.entity.User;

public interface UserService {


	User createUser(User user);

	List<User> findAllUsers();

	User findByUserId(Long userId);

	User updateUser(Long userId, User user);

	void deleteUserById(Long userId);

	List<Task> findTasks(Long userId);

	

	

	
}
