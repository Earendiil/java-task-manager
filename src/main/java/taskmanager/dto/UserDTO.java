package taskmanager.dto;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import taskmanager.entity.Role;
import taskmanager.entity.Task;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	private Long userId;
	private String username; 
	private String password;
	private String email;
	private List<Task> tasks;
	private Set<Role> roles;
	
}
