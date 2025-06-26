package MainApp.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.exception.StripeException;

import MainApp.DTOs.OrderDto;
import MainApp.DTOs.OrderRequestDto;
import MainApp.DTOs.StripeDto;
import MainApp.Services.OrderService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

	private final OrderService orderService;
	
	  @PostMapping("/add")
	  public ResponseEntity<Long> saveOrder(@RequestBody OrderRequestDto paymentRequest, Authentication authentication) {
		try {
			  return ResponseEntity.ok(orderService.saveOrderHandler(paymentRequest, authentication));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	  } 
	  
	  @GetMapping("/get/{id}")
	  public ResponseEntity<OrderDto> getOrderData(@PathVariable long id){
		  try { 
			return ResponseEntity.ok(orderService.getOrderDataHandler(id));
			
		  }catch(Exception e) {
			  e.printStackTrace();
			  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		  }
	  }
}
