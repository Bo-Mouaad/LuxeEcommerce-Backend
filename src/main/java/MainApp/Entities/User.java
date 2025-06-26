package MainApp.Entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import MainApp.Enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Component
@Getter
@Setter
@NoArgsConstructor	
@AllArgsConstructor
@ToString
@Entity
@Table(name="Users")

public class  User {
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="user_id")
	private long id;
	
	@Column(name="user_Email")
	private String email;
	
	@Column(name="user_Name")
	private String username;
	
	private String phoneNumber;
	
	@Column(name="user_provider")
	private String provider;
	
	@Column(name="user_picture")
	private String profilePicture;
	
	
	@Column(name="user_Password")
	private String password;
	
	@Enumerated(EnumType.STRING)
    @Column(name="Role")
    private Role UserRole;
	
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Cart> cartItems = new ArrayList<>();

    
    
}

