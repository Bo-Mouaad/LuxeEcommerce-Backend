package MainApp.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;


@Service
public class StripeService {

	@Value("${stripe.secretKey}") private String stripeSecretKey; 
	@Value("${stripe.publicKey}") private String stripePublicKey;
	
	 public PaymentIntent createPaymentIntent(PaymentIntentCreateParams params) throws StripeException {
	   
	    Stripe.apiKey = stripeSecretKey;

	        return PaymentIntent.create(params);
	    }

	public PaymentIntent retrievePaymentIntent(String paymentIntentId) throws StripeException {
        return PaymentIntent.retrieve(paymentIntentId);
    }

	
	
	
}
