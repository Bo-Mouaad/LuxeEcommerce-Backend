package MainApp.Services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor


public class ContactService {
	private final JavaMailSender mailSender;
	public void sendEmailHandler(String from, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("Luxe@Luxebrand.com");
        message.setSubject(subject);
        message.setFrom(from);
        message.setText(body);

        mailSender.send(message);
    }
}
