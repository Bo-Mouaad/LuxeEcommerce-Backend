package MainApp.Services;

import java.time.Instant;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import MainApp.DTOs.StripeDto;
import MainApp.Entities.Order;
import MainApp.Entities.Payment;
import MainApp.Repositories.OrderRepository;
import MainApp.Repositories.PaymentRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor

public class PaymentService {
	private final PaymentRepository paymentRepository;
	private final OrderRepository orderRepository;
	private final StripeService stripeService;
	
	
	public String createPaymentIntentHandler(StripeDto stripeRequest) throws StripeException {
		 PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
			        .setAmount(stripeRequest.getAmount())  
			        .setCurrency(stripeRequest.getCurrency())
			        .addPaymentMethodType("card") 
			        .build();

			    
			    PaymentIntent paymentIntent = stripeService.createPaymentIntent(params);
			    return paymentIntent.getClientSecret();
	}			

	public void savePaymentHandler(Map<String, ?> payload) throws StripeException {
		String paymentIntentId = payload.get("paymentIntentId").toString();
		long orderId = (long) payload.get("orderId");
		
		PaymentIntent paymentIntent = stripeService.retrievePaymentIntent(paymentIntentId);
		if (!"succeeded".equals(paymentIntent.getStatus())) {
		    throw new RuntimeException("Payment not successful");
		}
		Payment payment = new Payment();
		payment.setPaymentIntentId(paymentIntent.getId());
		payment.setAmount(paymentIntent.getAmount());
		payment.setCurrency(paymentIntent.getCurrency());
		payment.setStatus(paymentIntent.getStatus());
		payment.setCreatedAt(Instant.now());
			
		Order order = orderRepository.findById(orderId)
		        .orElseThrow(() -> new RuntimeException("Order not found"));
		payment.setOrder(order);

		paymentRepository.save(payment);
	}





	
}