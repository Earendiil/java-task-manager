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
import taskmanager.dto.CategoryDTO;
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
	public ResponseEntity<String> addCategory(@Valid @RequestBody CategoryDTO categoryDTO){
		categoryService.createCategory(categoryDTO);
		return new ResponseEntity<String>("Category created!", HttpStatus.CREATED);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<CategoryDTO>> getAllCategories(){
		List<CategoryDTO> categories = categoryService.findAllCategories();
		return new ResponseEntity<List<CategoryDTO>>(categories, HttpStatus.OK);
	}
	
	@PutMapping("/update/{categoryId}")
	public ResponseEntity<CategoryDTO> updateCategory(@Valid @PathVariable Long categoryId, 
												@RequestBody CategoryDTO categoryDTO){
			CategoryDTO updatedCategory = categoryService.updateCategory(categoryId,categoryDTO);									
		return new ResponseEntity<CategoryDTO>(updatedCategory, HttpStatus.OK);
		
	}
	@DeleteMapping("/delete/{categoryId}")
	public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
		categoryService.deleteCategory(categoryId);
		return new ResponseEntity<String>("Category deleted!" , HttpStatus.OK);
	}
	
	
	
	
	
	
	
}
