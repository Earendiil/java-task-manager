package taskmanager.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import taskmanager.entity.Task;
import taskmanager.entity.User;
import taskmanager.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

	
	private final UserService userService;
	
	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	@PostMapping("/user")
	public ResponseEntity<User> addUser(@Valid @RequestBody User user){
		return new ResponseEntity<User>(userService.createUser(user) , HttpStatus.CREATED);
	}
	
	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers(){
		List<User> users = new ArrayList<>();
		users = userService.findAllUsers();
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
	
	@GetMapping("/users/{userId}")
	public ResponseEntity<User> findUser(@PathVariable Long userId){
		User user = userService.findByUserId(userId);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@PutMapping("/users/{userId}")
	public ResponseEntity<String> updateUser(@Valid @PathVariable Long userId, @RequestBody User user){
		userService.updateUser(userId, user);
		return ResponseEntity.ok("User updated!");
	}

	@DeleteMapping("/users/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable Long userId){
		userService.deleteUserById(userId);
		return new ResponseEntity<String>("User deleted!", HttpStatus.OK);
	}
	
	@GetMapping("/users/{userId}/tasks")
	public ResponseEntity<List<Task>> getTasks(@PathVariable Long userId){
		List<Task> tasks = userService.findTasks(userId);
		
		return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
