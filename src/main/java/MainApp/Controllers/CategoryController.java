package MainApp.Controllers;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MainApp.Entities.Category;
import MainApp.Services.CategoryService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

	final CategoryService categoryService;
	
	
	@PostMapping("/add")
	public ResponseEntity<String> addCategory(@RequestBody Category category){
			return categoryService.addCategoryHandler(category) ? ResponseEntity.ok("the Category was added successfully !") 
					: ResponseEntity.status(409).body("There was an Error !"); 
	}
	
	
	
	
}
