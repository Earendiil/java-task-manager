package taskmanager.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import taskmanager.entity.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
	
	private Long taskId;
    private String taskName;
    private String title;
    private String description;
    private Date dueDate;
    private boolean completed;
    private Long categoryId;
    private List<UserIdDTO> assignedUsers = new ArrayList<>();
    
   
    
}
