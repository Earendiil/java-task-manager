package taskmanager.service;

import java.util.List;

import jakarta.validation.Valid;
import taskmanager.dto.CategoryDTO;

public interface CategoryService {

	public void createCategory(@Valid CategoryDTO categoryDTO);

	List<CategoryDTO> findAllCategories();

	CategoryDTO updateCategory(@Valid Long categoryId, CategoryDTO categoryDTO);

	void deleteCategory(Long categoryId);

	public CategoryDTO getCategoryById(Long categoryId);

	
	
		
	

	
	

}
