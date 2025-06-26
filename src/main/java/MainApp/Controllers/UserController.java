package MainApp.Controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MainApp.Entities.User;
import MainApp.Entities.UserPrincipals;
import MainApp.Services.UserService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path="/api/user")
@AllArgsConstructor
public class UserController {
    UserService userService; 
    BCryptPasswordEncoder passwordEncoder; 
    
    
    @GetMapping
    public ResponseEntity<User> getUserData(Authentication authentication) {
        try {
            Object principal = authentication.getPrincipal();
            String email;

            if (principal instanceof OAuth2User) {
                email = ((OAuth2User) principal).getAttribute("email");
            } else if (principal instanceof UserPrincipals) {
                email = ((UserPrincipals) principal).getUsername();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            
            User user = userService.getUserByEmail(email);
            System.out.println(user.getEmail());
            
            return ResponseEntity.ok(user);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

	
	@PutMapping("/updatePassword")
	public ResponseEntity<String> updatePassword(@RequestBody Map<String,String> payload){
		User user = userService.findUserByPassword(payload.get("currentPassword"));
			if(user != null) {
				user.setPassword(payload.get(passwordEncoder.encode("newPassword")));
				return ResponseEntity.ok("the user was Updated Successfuly !!");
			}
			return ResponseEntity.status(HttpStatus.CONFLICT).body("the User's Password is not present in the database !");	
	}
	@PutMapping("/update-user")
	public ResponseEntity<String> updateUserInformations(@RequestBody Map<String, String> payload, Authentication auhentication){
		try {
			userService.updateUserInformationsHandler(payload, auhentication);
			
			return ResponseEntity.ok("the infos are updated successfully ");
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
		}
	}
	
}
