package MainApp.Services;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import MainApp.Entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
	
public class JWTService {
	
	String secretKey; 
	
	public JWTService() {
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
			SecretKey sk = keyGen.generateKey();
			secretKey =Base64.getEncoder().encodeToString(sk.getEncoded());	
			
		} catch (NoSuchAlgorithmException e) {
			
			e.printStackTrace();
		}
		
	} 
	
	
	public String generateRefreshToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		
		 String token = Jwts.builder()
				.claims()
				.add(claims)
				.subject(user.getEmail())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000 * 24))
				.and()
				.claim("role", user.getUserRole().toString())
				.signWith(getKey())
				.compact();

		 return token; 
	}
	
	public String generateAccessToken(User user) {
		
		Map<String, Object> claims = new HashMap<>();
		
 String token = Jwts.builder()
		.claims()
		.add(claims)
		.subject(user.getEmail())
		.issuedAt(new Date(System.currentTimeMillis()))
		.expiration(new Date(System.currentTimeMillis() + 60 * 15 * 1000))
		.and()
		.claim("role", user.getUserRole().toString())
		.signWith(getKey())
		.compact();

 return token; 
 

	}

	public SecretKey getKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
		
	}

	public String extractEmail(String token) {
		 return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
    	System.out.println(extractClaim(token, Claims::getExpiration));
    	
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean validateRefreshToken(String token) {	
    		return !isTokenExpired(token);
    	}

}
