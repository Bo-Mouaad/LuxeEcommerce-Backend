package MainApp.Services;

import org.springframework.stereotype.Service;

import MainApp.Entities.Category;
import MainApp.Repositories.CategoryRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor

public class CategoryService {
     final CategoryRepository categoryRepository;

	public boolean addCategoryHandler(Category category) {
		return categoryRepository.save(category) != null;
		
	} 
     
}
