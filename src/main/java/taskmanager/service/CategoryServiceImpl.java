package taskmanager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import taskmanager.entity.Category;
import taskmanager.exceptions.ResourceNotFoundException;
import taskmanager.repositories.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService{

	private final CategoryRepository categoryRepository;
	
	
	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		super();
		this.categoryRepository = categoryRepository;
	}


	@Override
	public Category createCategory(@Valid Category category) {
		if (categoryRepository.existsByName(category.getName())) {
			throw new IllegalArgumentException("Category already exists!");
		}
		return categoryRepository.save(category);
	}


	@Override
	public List<Category> findAllCategories() {
		List<Category> categories = categoryRepository.findAll();
		if (categories.isEmpty()) {
			throw new RuntimeException("No categories exist!");
		}
		return categories;
	}


	@Override
	public Category updateCategory(@Valid Long categoryId, Category category) {
		Category updatedCategory = categoryRepository.findById(categoryId)
				.orElseThrow(()-> new ResourceNotFoundException("Category", "category id", categoryId));
		if (categoryRepository.existsByName(category.getName())) {
			throw new IllegalArgumentException("Category name already exists!");
		}
		updatedCategory.setName(category.getName());
		categoryRepository.save(updatedCategory);
		return updatedCategory;
	}


	@Override
	public void deleteCategory(Long categoryId) {
	Category category =	categoryRepository.findById(categoryId)
				.orElseThrow(()-> new ResourceNotFoundException("Category", "category id", categoryId));
		categoryRepository.delete(category);
	}

	
	
	
}
