package MainApp.DTOs;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
	private String orderNumber;
    private Instant createdAt;
    private String userEmail;
    private double totalAmount;
    private String paymentMethod;
    private String shippingMethod;
    private List<OrderItemDto> items;
    
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
     public static class OrderItemDto {
    	    private long id;
    	    private String productName;
    	    private double price;
     }
}
