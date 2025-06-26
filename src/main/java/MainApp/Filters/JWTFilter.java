package MainApp.Filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import MainApp.Services.JWTService;
import MainApp.Services.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Component
public class JWTFilter extends OncePerRequestFilter {

	JWTService jwtService; 
	@Autowired
	ApplicationContext context;
	
	
	@Autowired
	public JWTFilter(JWTService jwtService) {
		this.jwtService = jwtService; 
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authenticationHeader = request.getHeader("Authorization");
		String token = null; 
		String username = null; 
		if(authenticationHeader != null && authenticationHeader.contains("Bearer")) {
			token = authenticationHeader.substring("Bearer ".length());
			username = jwtService.extractEmail(token);
		}
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = (UserDetails) context.getBean(MyUserDetailsService.class).loadUserByUsername(username);
			
			if(jwtService.validateToken(token,userDetails)) {
				UsernamePasswordAuthenticationToken usernamePwdAuthToken = 
						new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
				usernamePwdAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePwdAuthToken);
			}
			
		}
		filterChain.doFilter(request, response);
	}

}
