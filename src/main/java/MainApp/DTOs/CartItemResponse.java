package MainApp.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class CartItemResponse {
	    private long productId;
	    private String name;
	    private double price;
	    private long quantity;
	    private String image;
}
