package taskmanager.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import taskmanager.dto.CategoryDTO;
import taskmanager.entity.Category;
import taskmanager.exceptions.ResourceNotFoundException;
import taskmanager.repositories.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService{

	private final CategoryRepository categoryRepository;
	private final ModelMapper modelMapper;
	
	
	public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
		super();
		this.categoryRepository = categoryRepository;
		this.modelMapper = modelMapper;
	}


	public void createCategory(@Valid CategoryDTO categoryDTO) {
		
		if (categoryRepository.existsByName(categoryDTO.getName())) {
			throw new IllegalArgumentException("Category already exists!");
		}
		Category category = modelMapper.map(categoryDTO, Category.class);
		categoryRepository.save(category);
		
	}


	@Override
	public List<CategoryDTO> findAllCategories() {
		List<Category> categories = categoryRepository.findAll();
		if (categories.isEmpty()) {
			throw new ResourceNotFoundException("No categories exist!");
		}
		return categories.stream()
				.map(category -> modelMapper.map(category, CategoryDTO.class))
				.collect(Collectors.toList());
		
	}


	@Override
	public CategoryDTO updateCategory(@Valid Long categoryId, CategoryDTO categoryDTO) {
		Category updatedCategory = categoryRepository.findById(categoryId)
				.orElseThrow(()-> new ResourceNotFoundException("Category", "category id", categoryId));
		if (categoryRepository.existsByName(categoryDTO.getName())) {
			throw new IllegalArgumentException("Category name already exists!");
		}
		
		updatedCategory.setName(categoryDTO.getName());
		categoryRepository.save(updatedCategory);
		return modelMapper.map(updatedCategory, CategoryDTO.class);
	}


	@Override
	public void deleteCategory(Long categoryId) {
	Category category =	categoryRepository.findById(categoryId)
				.orElseThrow(()-> new ResourceNotFoundException("Category", "category id", categoryId));
		categoryRepository.delete(category);
	}

	
	
	
}
