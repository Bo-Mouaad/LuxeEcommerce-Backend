package MainApp.Entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class Category {
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Category_id")
	private long id; 
	
	@Column(name="Category_Name")
	private String name;
	@Column(name="CreatedAt")
	private LocalDate createdAt;
	
	
	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
	List<Product> products;
	
}
