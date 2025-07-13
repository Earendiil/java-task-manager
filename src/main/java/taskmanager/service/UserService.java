package taskmanager.service;

import java.util.List;

import jakarta.validation.Valid;
import taskmanager.dto.TaskResponse;
import taskmanager.dto.UserDTO;
import taskmanager.dto.UserResponse;

public interface UserService {


	void createUser(@Valid UserDTO userDTO);

	List<UserResponse> findAllUsers();

	UserDTO findByUserId(Long userId);

	void updateUser(Long userId, @Valid UserDTO userDTO);

	void deleteUserById(Long userId);

	List<TaskResponse> findTasks(Long userId);

	

	

	
}
