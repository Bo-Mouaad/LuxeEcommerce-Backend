package MainApp.Services;

import java.io.IOException;
import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import MainApp.Entities.User;
import MainApp.Enums.Role;
import MainApp.Repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;


@Component
@AllArgsConstructor

public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	JWTService jwtService; 
	UserRepository userRepo; 
	AuthService userService;
	
	
	
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		String email = oAuth2User.getAttribute("email");
		String userName = oAuth2User.getAttribute("name");
		String pic = oAuth2User.getAttribute("picture");
		
		User user = new User();
		user.setEmail(email);
		user.setProfilePicture(pic);
		user.setProvider("GOOGLE");
		user.setUsername(userName);
		user.setUserRole(Role.USER);
	
		
		String refToken = jwtService.generateRefreshToken(user);
		String accessToken =  jwtService.generateAccessToken(user);
		
		ResponseCookie refreshToken = ResponseCookie.from("refreshToken", refToken)
			    .httpOnly(true) 
			    .path("/")
			    .maxAge(Duration.ofDays(15))
			    .build();

			response.addHeader(HttpHeaders.SET_COOKIE, refreshToken.toString());
		
		if(userRepo.findByEmail(email) != null) {
		response.sendRedirect("http://localhost:3000/auth/callback/?token="+accessToken);
		return;
		}	
		
		userRepo.save(user);
		response.sendRedirect("http://localhost:3000/auth/callback/?token=" + accessToken);
				
	}
}
