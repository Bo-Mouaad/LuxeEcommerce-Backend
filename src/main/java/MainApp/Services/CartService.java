package MainApp.Services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import MainApp.DTOs.CartItemResponse;
import MainApp.Entities.Cart;
import MainApp.Entities.Product;
import MainApp.Entities.User;
import MainApp.Entities.UserPrincipals;
import MainApp.Repositories.CartRepository;
import MainApp.Repositories.ProductsRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor

public class CartService {
	private final CartRepository cartRepository;
	private final ProductsRepository productsRepository;
	private final UserService userService;

    public static String extractEmail(Authentication authentication) {

		Object principal = authentication.getPrincipal();
		
		if (principal instanceof OAuth2User) {
		    return ((OAuth2User) principal).getAttribute("email");
		}

	    if (principal instanceof UserPrincipals) {
			return ((UserPrincipals) principal).getUsername();
		}
		return null;
	}

	@Transactional
	public void addToCartHandler(Map<String, Long> payload, Authentication authentication) {

		Product product = productsRepository.findById(payload.get("productId"))
				.orElseThrow(() -> new RuntimeException("Product not found"));

		String email = extractEmail(authentication);
		
		if (email == null) {
			throw new RuntimeException("User email not found in authentication");
		}
   
			User user = userService.getUserByEmail(email);

		Optional<Cart> existingCartOpt = cartRepository.findByUserAndProduct(user, product);

		Cart cart;
		if (existingCartOpt.isPresent()) {
			cart = existingCartOpt.get();
			cart.setQuantity(cart.getQuantity() + payload.get("quantity"));
		} else {
			cart = new Cart();
			cart.setUser(user);
			cart.setProduct(product);
			cart.setQuantity(payload.get("quantity"));
		}

		cartRepository.save(cart);
	}
	
	@Transactional
	public void deleteFromCartHandler(Long productId, Authentication authentication) {
		String email = extractEmail(authentication);
		if (email == null) {
			throw new RuntimeException("User email not found in authentication");
		}
		User user = userService.getUserByEmail(email);
		Product product = productsRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found"));
		cartRepository.deleteByUserAndProduct(user, product);
		
	}

	public CartItemResponse toCartItemResponse(Cart cart) {
		Product product = cart.getProduct();
		return new CartItemResponse(
				product.getProductId(),
				product.getName(),
				product.getPrice(), 
				cart.getQuantity(), 
				product.getImageUrl());
	}

	public List<CartItemResponse> getCartProductsHandler(Authentication authentication) {
		String email = extractEmail(authentication);
		
		if (email == null) {
			throw new RuntimeException("User email not found in authentication");
		}
		
		User user = userService.getUserByEmail(email);
		List<Cart> cartItems = cartRepository.findByUser(user);

		return cartItems.stream().map(this::toCartItemResponse).collect(Collectors.toList());
	}

	@Transactional
	public void updateCartItemQuantityHandler(Map<String, Long> payload, Authentication authentication) {
		Long productId = payload.get("productId");
		Long quantity = payload.get("quantity");
		if (quantity == null || quantity <= 0) {
			throw new RuntimeException("Quantity must be at least 1");
		}
		String email = extractEmail(authentication);
		
		if (email == null) {
			throw new RuntimeException("User email not found in authentication");
		}
		
        User user = userService.getUserByEmail(email);
        

		Product product = productsRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found"));

		Cart cart = cartRepository.findByUserAndProduct(user, product)
				.orElseThrow(() -> new RuntimeException("Cart item not found"));

		cart.setQuantity(quantity);
		cartRepository.save(cart);
	}

}
