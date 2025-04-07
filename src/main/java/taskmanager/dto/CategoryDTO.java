package taskmanager.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import taskmanager.entity.Task;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

	private Long categoryId;
	private String name;
	private List<Task> tasks;
}
