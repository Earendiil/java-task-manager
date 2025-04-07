package taskmanager.service;

import java.util.List;

import jakarta.validation.Valid;
import taskmanager.dto.TaskResponse;
import taskmanager.dto.UserDTO;
import taskmanager.dto.UserResponse;
import taskmanager.entity.Task;
import taskmanager.entity.User;

public interface UserService {


	void createUser(@Valid UserDTO userDTO);

	List<UserResponse> findAllUsers();

	UserDTO findByUserId(Long userId);

	User updateUser(Long userId, User user);

	void deleteUserById(Long userId);

	List<TaskResponse> findTasks(Long userId);

	

	

	
}
