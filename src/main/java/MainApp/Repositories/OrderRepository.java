package MainApp.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import MainApp.DTOs.OrderDto;
import MainApp.Entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
   

    
	      }

