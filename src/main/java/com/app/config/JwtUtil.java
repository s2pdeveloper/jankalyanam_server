package com.app.config;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtUtil  {
//    private static final String SECRET = "ECOMERCE-S2P";
//    private static final long EXPIRATION_TIME = 864_000_000; // 10 days
//    public static String generateToken(User user) {
//        return Jwts.builder()
//            .setSubject(user.toString())
//            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//            .signWith(SignatureAlgorithm.HS512, SECRET)
//            .compact();
//    }
//    public static String extractUsername(String token) {
//        return Jwts.parser()
//            .setSigningKey(SECRET)
//            .parseClaimsJws(token)
//            .getBody()
//            .getSubject();
//            
//
//    }
	
	   public static final String SECRET = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";
	   public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

	    public String extractUsername(String token) {
	        return extractClaim(token, Claims::getSubject);
	    }

	    public Date extractExpiration(String token) {
	        return extractClaim(token, Claims::getExpiration);
	    }

	    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	        final Claims claims = extractAllClaims(token);
	        return claimsResolver.apply(claims);
	    }

	    private Claims extractAllClaims(String token) {
	        return Jwts
	                .parserBuilder()
	                .setSigningKey(getSignKey())
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	    }

	    private Boolean isTokenExpired(String token) {
	        return extractExpiration(token).before(new Date());
	    }

	    public Boolean validateToken(String token, UserDetails userDetails) {
	        final String username = extractUsername(token);
	        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	    }


	    public String generateToken(UserDetails userDetails) {

	        return Jwts.builder()
	                .setSubject(userDetails.getUsername())
	                .setIssuedAt(new Date(System.currentTimeMillis()))
	                .setExpiration(new Date(System.currentTimeMillis()+ JWT_TOKEN_VALIDITY*1000))
	                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	    }

	    private Key getSignKey() {
	        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
	        return Keys.hmacShaKeyFor(keyBytes);
	    }

//		@Override
//		public void commence(HttpServletRequest request, HttpServletResponse response,
//				AuthenticationException authException) throws IOException, ServletException {
//			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
//					   "Unauthorized");
//			
//		}
}
