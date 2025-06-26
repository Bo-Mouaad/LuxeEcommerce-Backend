package MainApp.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import MainApp.Entities.Address;
import MainApp.Entities.User;

@Repository

public interface AddressRepository extends JpaRepository<Address, Long>{
	Address findByUser(User user);
	
}
