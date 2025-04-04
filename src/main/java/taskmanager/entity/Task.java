package taskmanager.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long taskId;
	
	@NotBlank
	@Size(min = 3, max = 20, message = "Task name must be within 3-20 characters")
	private String taskName;
	
	@NotBlank(message = "Tittle is required")
	@Size(min = 5, max = 20, message = "Title must be within 5-20 characters")
	private String title;
	@Size(max = 150, message = "Cannot exceed 150 characters")
	private String description;
	
	@NotNull(message = "Due date is required")
	@Future(message = "Due date must be in the future!")
	private Date dueDate;
	
	private boolean completed = false;
	
	@ManyToOne
	@JoinColumn(name = "category_id", nullable = true) // Ensures every task must have a category
	private Category category;

	@JsonProperty("categoryId")
	public Long getCategoryId() {
	    return category != null ? category.getCategoryId() : null;
	}

	@JsonIgnore
	public Category getCategory() {
	    return category;
	}

	public void setCategory(Category category) {
	    this.category = category;
	}

	public void setCategoryId(Long categoryId) {
	    if (categoryId != null) {
	        this.category = new Category();
	        this.category.setCategoryId(categoryId);
	    } else {
	        this.category = null;
	    }
	}
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = true)
	private User user;
	 
	
	@ManyToMany
	@JoinTable(
	    name = "user_tasks",
	    joinColumns = @JoinColumn(name = "task_id"),
	    inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	private List<User> assignedUsers;
	
	    @JsonIgnore
	    public List<User> getAssignedUsers() {
	        return assignedUsers;
	    }

	    public void setAssignedUsers(List<User> assignedUsers) {
	        this.assignedUsers = assignedUsers;
	    }
	    
	    
	    
	
	public Task(
			@NotBlank @Size(min = 3, max = 20, message = "Task name must be within 3-20 characters") String taskName,
			@NotBlank(message = "Tittle is required") @Size(min = 5, max = 20, message = "Title must be within 5-20 characters") String title,
			@Size(max = 150, message = "Cannot exceed 150 characters") String description,
			@NotNull(message = "Due date is required") @Future(message = "Due date must be in the future!") Date dueDate,
			boolean completed, Category category) {
		super();
		this.taskName = taskName;
		this.title = title;
		this.description = description;
		this.dueDate = dueDate;
		this.completed = completed;
		this.category = category;
	}
	
	
	
}
