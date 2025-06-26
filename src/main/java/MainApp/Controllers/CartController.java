package MainApp.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MainApp.DTOs.CartItemResponse;
import MainApp.Services.CartService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cart")

public class CartController {
	private final CartService cartService;
	
	@PostMapping("/add")
	public ResponseEntity<String> addToCart(@RequestBody Map<String, Long> payload, Authentication authentication){
		try {
			cartService.addToCartHandler(payload, authentication);
			return ResponseEntity.ok("Yes added");
		}catch(Exception e) {
			return ResponseEntity.status(500).body(null);
		}
		
	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteFromCart(@PathVariable Long id, Authentication authentication){ 
		try { 
			cartService.deleteFromCartHandler(id, authentication);
			return ResponseEntity.ok("Yes Deleted");
		}catch(Exception e) {
			return ResponseEntity.status(500).body(null);
		}
	}
	@GetMapping("/getCartProducts")
	public ResponseEntity<List<CartItemResponse>> getCartProducts(Authentication authentication){
		try {
			return ResponseEntity.ok(cartService.getCartProductsHandler(authentication));
			
		}catch(Exception e) { 
			return ResponseEntity.status(500).body(null);
		}
	}
	@PutMapping("/update")
	public ResponseEntity<String> updateCartItemQuantity(@RequestBody Map<String, Long> payload,  Authentication authentication){
		try {
			cartService.updateCartItemQuantityHandler(payload, authentication);
			return ResponseEntity.ok("Yes updated");
			
		}catch(Exception e) {
			return ResponseEntity.status(500).body(null);
		}
	}

}
