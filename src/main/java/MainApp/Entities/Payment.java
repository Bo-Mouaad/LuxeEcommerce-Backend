package MainApp.Entities;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  
    private String id;

    @Column(name = "payment_intent_id", nullable = false, unique = true)
    private String PaymentIntentId;

    @Column(nullable = false)
    private long amount;

    @Column(nullable = false, length = 3)
    private String currency; 
    

    @Column(nullable = false)
    private String status; 

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Instant createdAt;
    
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
}
