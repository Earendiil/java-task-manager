package taskmanager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import taskmanager.entity.Task;
import taskmanager.entity.User;
import taskmanager.exceptions.ResourceNotFoundException;
import taskmanager.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public User createUser(User user) {
		if (userRepository.existsByUsername(user.getUsername())) {
			throw new IllegalArgumentException("Username already exists!");
		}
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new IllegalArgumentException("Email already exists!");
		}
		userRepository.save(user);
		return user;
	}

	@Override
	public List<User> findAllUsers() {
		List<User> users =	userRepository.findAll();
			if (users.isEmpty()) {
				throw new RuntimeException("No Users exist");
			}
			return users;
	}

	@Override
	public User findByUserId(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "user id", userId));
		return user;
	}

	@Override
	public User updateUser(Long userId, User user) {
		User updatedUser = userRepository.findById(userId).orElseThrow(()->  new ResourceNotFoundException("User", "user id", userId));
		
		if (userRepository.existsByEmail(user.getEmail()) && !updatedUser.getEmail().equals(user.getEmail())) {
			throw new IllegalArgumentException("Email already exists!");
		}
		
		updatedUser.setEmail(user.getEmail());
		updatedUser.setPassword(user.getPassword());
		updatedUser.setRoles(user.getRoles());
		userRepository.save(updatedUser);
		return updatedUser;
	}

	@Override
	public void deleteUserById(Long userId) {
		userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "user id", userId));
		userRepository.deleteById(userId);
	}

	@Override
	public List<Task> findTasks(Long userId) {
		
		return null;
	}


	


	

	
	
	
	
	
}
