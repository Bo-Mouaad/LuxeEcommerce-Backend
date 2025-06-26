package MainApp.Services;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import MainApp.Entities.User;
import MainApp.Repositories.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
	
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepo; 
	private final PasswordEncoder passwordEncoder;
	private final JWTService jwtService;
	


	public String verifyLogin(User user) {
		Authentication auth = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

		return  auth.isAuthenticated() ? jwtService.generateAccessToken(user) : "the user is not Authenticated!";
		
	}
	public String getRefreshToken(User user) {
		try {
		return jwtService.generateRefreshToken(user);
		}catch(JwtException e) {
		e.printStackTrace();
		
		return "";
		}
		
	}
	
	public boolean registerUser(User user) {
		try {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userRepo.save(user);
			return true; 
		}catch(Exception e) {
			e.printStackTrace();
			return false;
			
		}
		
	}
	public boolean refreshTokenExpiration(String token) {
		return jwtService.isTokenExpired(token);
	}
	public User getUser(String email) {
		return userRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(""));
	}

}
