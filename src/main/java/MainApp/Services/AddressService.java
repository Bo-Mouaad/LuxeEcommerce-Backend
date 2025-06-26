package MainApp.Services;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import MainApp.Entities.Address;
import MainApp.Entities.User;
import MainApp.Repositories.AddressRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor

public class AddressService {
	
	private final AddressRepository addressRepository;
	private final UserService userService;
	
	public long saveAddressHandler(Address payload, Authentication authentication) {
		String email = CartService.extractEmail(authentication);
		if (email == null) {
			throw new RuntimeException("User email not found in authentication");
		}
   
		User user = userService.getUserByEmail(email);
			payload.setUser(user);
			Address address  = addressRepository.save(payload);
			
			
		return address.getId();	
	}
	
}
