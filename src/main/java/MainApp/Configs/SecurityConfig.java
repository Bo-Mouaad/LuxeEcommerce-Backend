package MainApp.Configs;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import MainApp.Filters.JWTFilter;
import MainApp.Services.MyUserDetailsService;
import MainApp.Services.OAuth2LoginSuccessHandler;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	
	private JWTFilter jwtFilter;
	
	private MyUserDetailsService userDetailsService;

	private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	
	
	@Autowired
	public SecurityConfig(@Lazy JWTFilter jwtFilter, @Lazy OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler
			, @Lazy MyUserDetailsService userDetailsService) {
		this.jwtFilter = jwtFilter;
		this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
		this.userDetailsService = userDetailsService;
	}	
	
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); 
    }
 

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        return http.
             cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
          
	
            
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**","/api/products/public/**")
                .permitAll()
                .requestMatchers("/api/admin/**","/api/products/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            
            
            .oauth2Login(oauth2 -> oauth2
            		.successHandler(oAuth2LoginSuccessHandler)
            		)           	

            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            

            
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            
            .build();
    }
   
    @Bean 
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://192.168.1.37:3000/","http://localhost:3000/")); 
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
    	return new ProviderManager(authenticationProvider);
    }
    
}
