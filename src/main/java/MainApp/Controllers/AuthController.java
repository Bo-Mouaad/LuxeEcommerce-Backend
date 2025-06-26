package MainApp.Controllers;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MainApp.Entities.User;
import MainApp.Enums.Role;
import MainApp.Services.AuthService;
import MainApp.Services.JWTService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(path="/auth")

public class AuthController {
	
	AuthService authService;
	JWTService jwtService; 
	

	
	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(
	    @RequestBody Map<String, String> payload,
	    HttpServletRequest request,
	    HttpServletResponse response
	) {
	    User user = new User();
	    user.setEmail(payload.get("email"));
	    user.setPassword(payload.get("password"));
	    user.setUserRole(authService.getUser(user.getEmail()).getUserRole());

	    Map<String, String> data = new HashMap<>();

	    try {
	        Cookie[] cookies = request.getCookies();
	        if (cookies != null) {
	            for (Cookie cookie : cookies) {
	                if ("refreshToken".equals(cookie.getName())) {
	                    ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
	                        .path("/")
	                        .httpOnly(true)
	                        .maxAge(0) 
	                        .build();
	                    response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
	                    break;
	                }
	            }
	        }

	        String accessToken = authService.verifyLogin(user);
	        String refToken = authService.getRefreshToken(user);
	        data.put("accessToken", accessToken);

	        if (authService.getUser(payload.get("email")) != null)
	            data.put("name", authService.getUser(payload.get("email")).getUsername());

	        ResponseCookie newRefreshToken = ResponseCookie.from("refreshToken", refToken)
	            .httpOnly(true)
	            .path("/")
	            .maxAge(Duration.ofDays(15))
	            .build();

	        response.addHeader(HttpHeaders.SET_COOKIE, newRefreshToken.toString());

	        return ResponseEntity.ok(data);

	    } catch (BadCredentialsException e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	            .body(Map.of("error", "Invalid email or password"));
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(Map.of("error", "An unexpected error occurred!"));
	    }
	}


	@GetMapping("/refresh")
	public ResponseEntity<Map<String,String>> refreshToken(@CookieValue(name="refreshToken", required=false) String token) {
		System.out.println(token);
		if(token == null || !jwtService.validateRefreshToken(token)) {
		 return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","the token had expired!"));
		}
		
		
		String email = jwtService.extractEmail(token);
		 System.out.println(email);
		 
		 	
		  User user = new User();
			user.setEmail(email);
			user.setUserRole(authService.getUser(user.getEmail()).getUserRole());
			
		return  ResponseEntity.ok(Map.of("accessToken", jwtService.generateAccessToken(user)));
	}
	
	

	@PostMapping("/registration") 
	public ResponseEntity<Map<String,String>> registerUser(@RequestBody Map<String,String> payload){
	try {	
		User user = new User();
		user.setUsername(payload.get("username"));
		user.setEmail(payload.get("email"));
		user.setPassword(payload.get("password"));
		user.setUserRole(Role.USER);
		authService.registerUser(user);
		return ResponseEntity.ok(Map.of("state","Succsess!"));
	}catch(Exception e) {
		e.printStackTrace();
	return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("state","Registration Failed !")); 
	}
		
	}
	@PostMapping("/logout")
	public  ResponseEntity<Map<String,String>> logout(HttpServletResponse response, HttpServletRequest request){
		try {
			 HttpSession session = request.getSession(false);
		        if (session != null) {
		            session.invalidate();
		        }
		        SecurityContextHolder.clearContext();
			ResponseCookie refreshToken = ResponseCookie.from("refreshToken", "")
			        .httpOnly(true)
			        .path("/") 
			        .maxAge(0)
			        .build();
			response.addHeader(HttpHeaders.SET_COOKIE, refreshToken.toString());
		    return ResponseEntity.ok(Map.of("Success","Log out Succeeded!"));
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("Failed","Log out Failed !")); 
		}
		
	}
	
	@GetMapping("/")
	String test () {
		return " hello "; 
	}
	
	
	
	
}
