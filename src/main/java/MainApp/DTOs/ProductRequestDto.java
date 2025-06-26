package MainApp.DTOs;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDto {
	private Long productId;
    private String name;
    private double price;
    private String description;
    private String imageUrl;
    private boolean isFeatured;
    private double rating = 0.0;

    
    private Integer reviewCount = 0;
    private boolean isNew = false;
    private boolean inStock = true;
    private LocalDate createdAt = LocalDate.now();
    private String CategoryName;

    private ProductDetailsDto details;
    private List<String> features;
    private List<String> images;
}
