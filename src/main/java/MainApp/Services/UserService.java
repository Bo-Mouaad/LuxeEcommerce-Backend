package MainApp.Services;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import MainApp.Entities.User;
import MainApp.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor

public class UserService {
	private final UserRepository userRepository; 
	public User findUserByPassword(String password) {
		return userRepository.findByPassword(password).orElseThrow(()-> new EntityNotFoundException(""));
	}
	public User getUserByEmail(String email) {
			return userRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException(""));
	}
	public void updateUserInformationsHandler(Map<String, String> payload, Authentication authentication) {
		
		String email = CartService.extractEmail(authentication);
		if(email == null) { 
			throw new RuntimeException(""); 
		}
		
	      userRepository.findByEmail(email).map(existing -> { 
	    	  existing.setPhoneNumber(payload.get("phoneNumber"));
	    	  existing.setEmail(payload.get("email"));
	    	  existing.setUsername(payload.get("username"));
	    	  return userRepository.save(existing);
	      }).orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
		
	}
	
}
