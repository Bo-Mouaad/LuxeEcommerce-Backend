package MainApp.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import MainApp.Entities.ProductDetails;

@Repository

public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Long>{
	

}
