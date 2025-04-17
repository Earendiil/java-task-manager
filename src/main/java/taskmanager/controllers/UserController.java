package taskmanager.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import taskmanager.dto.TaskResponse;
import taskmanager.dto.UserDTO;
import taskmanager.dto.UserResponse;
import taskmanager.entity.User;
import taskmanager.service.UserService;

//@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class UserController {


	
	private final UserService userService;
	
	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/user")
	public ResponseEntity<String> addUser(@Valid @RequestBody UserDTO userDTO){
		 userService.createUser(userDTO);
		return new ResponseEntity<>("User created", HttpStatus.CREATED);
	}
	
	@GetMapping("/users")
	public ResponseEntity<List<UserResponse>> getAllUsers(){
		List<UserResponse> users = new ArrayList<>();
		users = userService.findAllUsers();
		return new ResponseEntity<List<UserResponse>>(users, HttpStatus.OK);
	}
	
	@GetMapping("/users/{userId}")
	public ResponseEntity<UserDTO> findUser(@PathVariable Long userId){
		UserDTO userDTO = userService.findByUserId(userId);
		return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
	}
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/users/{userId}")
	public ResponseEntity<String> updateUser(@Valid @PathVariable Long userId, @RequestBody User user){
		userService.updateUser(userId, user);
		return ResponseEntity.ok("User updated!");
	}
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/users/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable Long userId){
		userService.deleteUserById(userId);
		return new ResponseEntity<String>("User deleted!", HttpStatus.OK);
	}
	
	@GetMapping("/users/{userId}/tasks")
	public ResponseEntity<List<TaskResponse>> getUserTasks(@PathVariable Long userId){
		List<TaskResponse> tasks = userService.findTasks(userId);
		
		return new ResponseEntity<List<TaskResponse>>(tasks, HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
