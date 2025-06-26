package MainApp.Services;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import MainApp.Entities.User;
import MainApp.Entities.UserPrincipals;
import MainApp.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;

@Service

public class MyUserDetailsService implements UserDetailsService{

	
	private final  UserRepository userRepo;
		
	public MyUserDetailsService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
   if(userRepo.findByEmail(email) == null) throw new UsernameNotFoundException("User not found with email: " + email);
   
	User user =  userRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(""));
	
		return new UserPrincipals(user);
	}
	

}
