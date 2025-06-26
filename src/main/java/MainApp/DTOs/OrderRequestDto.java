package MainApp.DTOs;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class OrderRequestDto {
	   private Long amount;
	   private String currency;
	    private Long shippingMethodId;
	    private Long addressId; 
	    private List<OrderItemDto> items;

	    
	    @Setter
	    @Getter
	    @AllArgsConstructor
	    public static class OrderItemDto {
	        private Long productId;
	        private Long quantity;

	    }
}
