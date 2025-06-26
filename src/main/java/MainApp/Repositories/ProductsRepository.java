package MainApp.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import MainApp.DTOs.ProductListDto;
import MainApp.Entities.Product;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {

	 Optional<Product> findById(long id);
	@Modifying
	@Query("Delete from Product p where p.name = :name")
	int deleteByName(@Param("name") String name);
	List<ProductListDto> findByIsFeatured(boolean isFeatured);
	
    
    
  
   
}
