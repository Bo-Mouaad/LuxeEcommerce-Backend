package MainApp.Entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Product")  
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder


public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private long productId;

    @Column(name = "product_name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url")
 
   private String imageUrl;

    @Column(name = "is_featured", nullable = false)
    @Builder.Default
    private boolean isFeatured = false;


    @Column(nullable = true)
    @Builder.Default
    private double rating = 0.0;
    
    @Column(name = "review_count", nullable = true)
    @Builder.Default
    private Integer reviewCount = 0;
    
    @Column(name = "is_new", nullable = true)
    @Builder.Default
    private boolean isNew = false;

    @Column(name = "in_stock", nullable = true)
    @Builder.Default
    private boolean inStock = true;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDate createdAt = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "category_id") 
    @JsonIgnore
    private Category category;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "details_id", referencedColumnName = "id")
    private ProductDetails details;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductFeatures> features = new ArrayList<>();
    
    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImages> images = new ArrayList<>();
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Cart> cartItems = new ArrayList<>();

}
