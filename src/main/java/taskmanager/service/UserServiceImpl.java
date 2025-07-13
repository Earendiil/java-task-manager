package taskmanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import taskmanager.dto.TaskResponse;
import taskmanager.dto.UserDTO;
import taskmanager.dto.UserResponse;
import taskmanager.entity.Task;
import taskmanager.entity.User;
import taskmanager.exceptions.ResourceNotFoundException;
import taskmanager.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public void createUser(@Valid UserDTO userDTO) {
	    if (userRepository.existsByUsername(userDTO.getUsername())) {
	        throw new IllegalArgumentException("Username already exists!");
	    }
	    if (userRepository.existsByEmail(userDTO.getEmail())) {
	        throw new IllegalArgumentException("Email already exists!");
	    }

	    User user = modelMapper.map(userDTO, User.class);
	    user.setPassword(passwordEncoder.encode(user.getPassword()));
	    userRepository.save(user);
	}


	@Override
	public List<UserResponse> findAllUsers() {
		List<User> users =	userRepository.findAll();
			if (users.isEmpty()) {
				throw new ResourceNotFoundException("No Users exist");
			}
			
		// this is needed to map an entire list	
	 return users.stream()
				.map(user -> modelMapper.map(user, UserResponse.class))
				.collect(Collectors.toList());
		
			
	}

	@Override
	public UserDTO findByUserId(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "user id", userId));
		return modelMapper.map(user, UserDTO.class);
	}

	public void updateUser(Long id, UserDTO userDTO) {
	    User existingUser = userRepository.findById(id)
	        .orElseThrow(() -> new ResourceNotFoundException("User", "user id", id));

	    if (!existingUser.getEmail().equals(userDTO.getEmail())
	        && userRepository.existsByEmail(userDTO.getEmail())) {
	        throw new IllegalArgumentException("Email already exists!");
	    }

	    modelMapper.map(userDTO, existingUser);
	    existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));

	    userRepository.save(existingUser);
	
	}

	@Override
	public void deleteUserById(Long userId) {
		userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "user id", userId));
		userRepository.deleteById(userId);
	}

	@Override
	public List<TaskResponse> findTasks(Long userId) {
	    List<Task> userTasks = findByUserId(userId).getTasks();
	    if (userTasks.isEmpty()) {
	        return new ArrayList<>();
	    }
	    return userTasks.stream()
	            .map(task -> {
	                TaskResponse response = modelMapper.map(task, TaskResponse.class);
	                
	               
	                List<UserResponse> assignedUsers = task.getAssignedUsers().stream()
	                        .map(user -> modelMapper.map(user, UserResponse.class))
	                        .collect(Collectors.toList());
	                response.setAssignedUsers(assignedUsers);
	                
	                return response;
	            })
	            .collect(Collectors.toList());
	}






	


	
	
	
	
}
