package taskmanager.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
	
	private Long taskId;
    private String taskName;
    private Date dueDate;
    private Boolean completed;
    private String description;
    private List<UserResponse> assignedUsers;
}
