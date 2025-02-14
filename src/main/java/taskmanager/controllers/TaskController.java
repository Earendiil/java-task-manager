package taskmanager.controllers;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import taskmanager.entity.Task;
import taskmanager.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
	
	private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    
    
    @PostMapping("")
	public ResponseEntity<Task> addTask(@Valid @RequestBody Task task){
		Task newTask = taskService.createTask(task);
		return new ResponseEntity<Task>(newTask, HttpStatus.CREATED);
	}
    
	@PutMapping("/{taskId}")
	public ResponseEntity<Task> updateTask(@Valid @PathVariable Long taskId, @RequestBody Task task
											){
		
		Task updatedTask = taskService.updateTask(taskId, task);
		return new ResponseEntity<Task>(updatedTask, HttpStatus.OK);
	}
	
	@GetMapping("")
	public ResponseEntity<List<Task>> getAllTasks(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String sortBy,  
            @RequestParam(required = false) String sortDirection
    ) {
        List<Task> tasks = taskService.findAllTasks(userId, completed, categoryId, sortBy, sortDirection);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
		
		
	}
	@DeleteMapping("/{taskId}")
	public ResponseEntity<String> deleteTask(@PathVariable Long taskId){
		taskService.deleteTaskById(taskId);
		return ResponseEntity.ok("Task deleted!");
		
	}
	
	@GetMapping("/idle")
	public ResponseEntity<List<Task>> getUnassignedTasks(){
		List<Task> tasks = taskService.findIdleTasks();
		return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
}
