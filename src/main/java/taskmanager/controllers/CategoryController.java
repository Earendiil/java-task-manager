package taskmanager.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import taskmanager.entity.Category;
import taskmanager.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	private final CategoryService categoryService;
	
	
	
	
	public CategoryController(CategoryService categoryService) {
		super();
		this.categoryService = categoryService;
	}



	@PostMapping("/create")
	public ResponseEntity<Category> addCategory(@Valid @RequestBody Category category){
	Category newCategory =categoryService.createCategory(category);
		return new ResponseEntity<Category>(newCategory, HttpStatus.CREATED);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<Category>> getAllCategories(){
		List<Category> categories = categoryService.findAllCategories();
		return new ResponseEntity<List<Category>>(categories, HttpStatus.OK);
	}
	
	@PutMapping("/update/{categoryId}")
	public ResponseEntity<Category> updateCategory(@Valid @PathVariable Long categoryId, 
												@RequestBody Category category){
			Category updatedCategory = categoryService.updateCategory(categoryId,category);									
		return new ResponseEntity<Category>(updatedCategory, HttpStatus.OK);
		
	}
	@DeleteMapping("/delete/{categoryId}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
		categoryService.deleteCategory(categoryId);
		return new ResponseEntity<String>("Category deleted!" , HttpStatus.OK);
	}
	
	
	
	
	
	
	
}
