package MainApp.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import MainApp.Entities.ProductImages;

@Repository
public interface ProductImagesRepository extends JpaRepository<ProductImages, Long>{

}
