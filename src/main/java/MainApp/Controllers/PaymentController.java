package MainApp.Controllers;


import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stripe.exception.StripeException;
import MainApp.DTOs.StripeDto;
import MainApp.Services.PaymentService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/payment")

public class PaymentController {
  private final PaymentService paymentService;
  
  @PostMapping("/create-payment-intent")
  public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody StripeDto payload) {
      try { 
          return ResponseEntity.ok(Map.of("clientSecret",paymentService.createPaymentIntentHandler(payload)));
      } catch (StripeException e) {
          e.printStackTrace();
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
      }
  }
   @PostMapping("add")
   public ResponseEntity<String> savePayment(@RequestBody Map<String, ?> payload){
	   try { 
		    paymentService.savePaymentHandler(payload);
		    return ResponseEntity.ok("yaaay Payment is saved");
	   }catch(Exception e) {
		   return ResponseEntity.status(500).body(null);
	   }
   }


	
	 
}
