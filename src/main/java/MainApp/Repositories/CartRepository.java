package MainApp.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import MainApp.Entities.Cart;
import MainApp.Entities.Product;
import MainApp.Entities.User;

@Repository

public interface CartRepository extends JpaRepository<Cart, Long>{
	Optional<Cart> findByUserAndProduct(User user, Product productId);
	
	List<Cart> findByUser(User user);
	
	void deleteByUserAndProduct(User user, Product product);

}
