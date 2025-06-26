package MainApp.Services;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import MainApp.DTOs.OrderDto;
import MainApp.DTOs.OrderRequestDto;
import MainApp.DTOs.StripeDto;
import MainApp.Entities.Address;
import MainApp.Entities.Order;
import MainApp.Entities.OrderItem;
import MainApp.Entities.Product;
import MainApp.Entities.ShippingMethod;
import MainApp.Entities.User;
import MainApp.Enums.OrderStatus;
import MainApp.Repositories.AddressRepository;
import MainApp.Repositories.OrderRepository;
import MainApp.Repositories.ProductsRepository;
import MainApp.Repositories.ShippingMethodRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor


public class OrderService {
  
	private final OrderRepository orderRepository;
	private final UserService userService;
	private final AddressRepository addressRepository;
	private final ProductsRepository productsRepository;
	private final ShippingMethodRepository shippingMethodRepository;

	@Transactional
    public OrderDto getOrderDataHandler(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        OrderDto dto = new OrderDto();
        dto.setOrderNumber("LUXE-" + String.format("%08d", order.getId()));
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUserEmail(order.getUser().getEmail());
        dto.setTotalAmount(order.getAmount());
        dto.setPaymentMethod("Credit Card (•••• 4242)");
        dto.setShippingMethod(order.getShippingMethod().getName());

        List<OrderDto.OrderItemDto> itemDtos = order.getItems().stream()
            .map(item -> new OrderDto.OrderItemDto(
                item.getId(),
                item.getProduct().getName(),
                item.getPrice()
            )).collect(Collectors.toList());

        dto.setItems(itemDtos);

        return dto;
    }

	@Transactional
	public Long saveOrderHandler(OrderRequestDto orderRequest, Authentication authentication) {
		 Order order = new Order();
		    order.setAmount(orderRequest.getAmount());
		    order.setCreatedAt(Instant.now());
		    order.setCurrency(orderRequest.getCurrency());
		    order.setStatus(OrderStatus.PENDING);
		    
		    String email = CartService.extractEmail(authentication);
		    
		    if(email == null){
				throw new RuntimeException("User email not found in authentication");
		    }
		    
		   User user = userService.getUserByEmail(email);
		   order.setUser(user);
		   
		   
		   
		   Address address = addressRepository.findById(orderRequest.getAddressId())
				   .orElseThrow(() -> new EntityNotFoundException("there is no address with this Id my friend"));
		   order.setAddress(address);			   
		   
		   ShippingMethod shippingMethod = shippingMethodRepository.findById(1);
		   order.setShippingMethod(shippingMethod);
		   
		   
		   List<OrderItem> orderItems = orderRequest.getItems().stream().map(itemDto -> {
			    OrderItem item = new OrderItem();
			    Product product = productsRepository.findById(itemDto.getProductId())
			        .orElseThrow(() -> new RuntimeException("Product not found"));
			    item.setProduct(product);
			    item.setQuantity(itemDto.getQuantity());
			    item.setPrice(product.getPrice());
			    item.setOrder(order); 
			    return item;
			}).collect(Collectors.toList());

			order.setItems(orderItems);
			

		  orderRepository.save(order);  
		  return order.getId();
	}

}
