package taskmanager.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	
    @Pattern(regexp = ".*[a-zA-Z].*", message = "Must contain at least one alphabetical letter")
	@NotBlank(message = "username required")
	@Column(unique = true, nullable = false )
	private String username;

	@NotBlank(message = "email is required")
	@Email(message = "Invalid email format")
	@Column(unique = true, nullable = false)
	private String email;
	
	
	@NotBlank(message = "password required")
	@Size(min = 6, message = "Password must be at least 6 characters long")
	private String password;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "assignedUsers")
	private List<Task> tasks;

	
	
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
                fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    public User(String userName, String email, String password) {
        this.username = userName;
        this.email = email;
        this.password = password;
        
    }


	public User(
			@Pattern(regexp = ".*[a-zA-Z].*", message = "Must contain at least one alphabetical letter") @NotBlank(message = "username required") String username,
			@NotBlank(message = "email is required") @Email(message = "Invalid email format") String email,
			@NotBlank(message = "password required") @Size(min = 6, message = "Password must be at least 6 characters long") String password,
			List<Task> tasks, Set<Role> roles) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.roles = roles;
		this.tasks = new ArrayList<>();
	}

	
	
	
	
	
	
}
