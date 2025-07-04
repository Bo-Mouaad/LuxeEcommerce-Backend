package MainApp.Repositories;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import MainApp.Entities.Category;
import jakarta.persistence.EntityNotFoundException;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
	Optional<Category> findByName(String name);

}
