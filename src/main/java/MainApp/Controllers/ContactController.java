package MainApp.Controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MainApp.Services.ContactService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/contact")

public class ContactController {
	 private final ContactService contactService;
	 @PostMapping("/send")
	 public ResponseEntity<String> sendEmail(@RequestBody Map<String, String> map){
		 try {
			 contactService.sendEmailHandler(map.get("email"), map.get("subject"), map.get("message"));
			 return ResponseEntity.ok("Yes It got sent l3z!");
		 }catch(Exception e) {
			 return ResponseEntity.status(500).body(null);
		 }
	 }
}
