package MainApp.Services;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import MainApp.Entities.User;
import MainApp.Entities.UserPrincipals;
import MainApp.Repositories.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor


public class MyUserDetailsService implements UserDetailsService{

	
	private final  UserRepository userRepository;
		
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
	User user =  userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
	
		return new UserPrincipals(user);
	}
	

}
