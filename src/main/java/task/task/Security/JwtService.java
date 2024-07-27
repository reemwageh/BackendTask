package task.task.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import task.task.DTO.UserDTO;
import task.task.Entity.User;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "taskSecretKey";
    public String extractUsername(String token) {
        return extractClaim(token , Claims::getSubject);
    }

    public static String generateToken(User userDetails){
        return generateToken(new HashMap<>() , userDetails);
    }

    public static String generateToken(Map<String, Object> extraClaims, User userDetails){
          return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getEmail())
                  .setIssuedAt(new Date(System.currentTimeMillis()))
                  .setExpiration(new Date(System.currentTimeMillis()+1000 *60 *24))
                  .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                  .compact();
    }

    public boolean isTokenValid(String token , UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token ,Claims::getExpiration);
    }

    public <T> T extractClaim(String token , Function<Claims , T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private static Key getSignKeyIn() {
        byte[] KeyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(KeyBytes);
    }
    public UserDTO getCurrentUser() {
        UserDTO userDto = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDto;
    }
}
