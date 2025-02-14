package taskmanager.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
	
	 @Transient // This field is not stored in the database
	 @JsonProperty("userId") // Makes sure JSON contains userId instead of full user object
	 Long userId;
	 
    // Getter to reflect userId instead of user object in response
    @JsonProperty("userId")
    public Long getUserId() {
        return user != null ? user.getUserId() : null; // Return userId if user is set
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
        if (userId != null) {
            this.user = new User();
            this.user.setUserId(userId);
        }
    }
    
    @JsonIgnore
    public User getUser() {
        return user; // Don't serialize the full User object
    }

    public void setUser(User user) {
        this.user = user;
    }

	public Task(
			@NotBlank @Size(min = 3, max = 20, message = "Task name must be within 3-20 characters") String taskName,
			@NotBlank(message = "Tittle is required") @Size(min = 5, max = 20, message = "Title must be within 5-20 characters") String title,
			@Size(max = 150, message = "Cannot exceed 150 characters") String description,
			@NotNull(message = "Due date is required") @Future(message = "Due date must be in the future!") Date dueDate,
			boolean completed, User user) {
		super();
		this.taskName = taskName;
		this.title = title;
		this.description = description;
		this.dueDate = dueDate;
		this.completed = completed;
		this.user = user;
	}
	
	
	
}
