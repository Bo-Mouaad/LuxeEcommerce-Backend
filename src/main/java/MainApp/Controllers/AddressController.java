package MainApp.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MainApp.Entities.Address;
import MainApp.Services.AddressService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor 
@RequestMapping("/api/address")
public class AddressController {
	
	private final AddressService addressService;
	
	@PostMapping("/add")
	public ResponseEntity<Long> saveAddress(@RequestBody Address payload, Authentication authentication ) {
		try {
				return ResponseEntity.ok(addressService.saveAddressHandler(payload, authentication));
		} catch(Exception e) {
		  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
}
