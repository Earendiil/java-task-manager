package taskmanager.service;

import java.util.List;

import jakarta.validation.Valid;
import taskmanager.entity.Category;

public interface CategoryService {

	Category createCategory(@Valid Category category);

	List<Category> findAllCategories();

	Category updateCategory(@Valid Long categoryId, Category category);

	void deleteCategory(Long categoryId);

	
	
		
	

	
	

}
