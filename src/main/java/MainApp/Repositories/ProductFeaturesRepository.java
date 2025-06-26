package MainApp.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import MainApp.Entities.ProductFeatures;

@Repository
public interface ProductFeaturesRepository extends JpaRepository<ProductFeatures, Long>{

}
