package MainApp.Entities;

import java.time.Instant;
import java.util.List;

import MainApp.Enums.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="orders")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter

public class Order {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private Instant createdAt;

	    @Column(nullable = false)
	    private Long amount; 

	    @Column(nullable = false)
	    private String currency = "usd";

	    @Enumerated(EnumType.STRING)
	    private OrderStatus status;

	    // ----------------------
	    // Relationships
	    // ----------------------

	    @ManyToOne
	    @JoinColumn(name = "user_id", nullable = false)
	    private User user;

	    @ManyToOne
	    @JoinColumn(name = "address_id", nullable = false)
	    private Address address;

	    @ManyToOne
	    @JoinColumn(name = "shipping_method_id", nullable = false)
	    private ShippingMethod shippingMethod;

	    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<OrderItem> items;
}
