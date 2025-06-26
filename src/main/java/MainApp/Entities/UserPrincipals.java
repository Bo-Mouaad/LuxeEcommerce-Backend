package MainApp.Entities;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;



public class UserPrincipals implements UserDetails{
	
	private final User user;
	
	
	@Autowired
	public UserPrincipals(User user) {
		this.user = user;
	}
	
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getUserRole().toString()));
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail().toLowerCase();
	}
	
  public String getActualUsername() {
	  return user.getUsername();
  }
  
	
}
