package com.app.config;

import java.io.IOException;

import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.app.dto.ApiError;
import com.app.dto.UserDTO;
import com.app.exception.InvalidInputException;
import com.app.service.imp.CustomUserDetailsService;
import com.app.utilities.Utility;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtFilter extends OncePerRequestFilter {
	   @Autowired
	   private CustomUserDetailsService userDetailsService;
	    
	   @Autowired
	   private JwtUtil tokenManager;
	   
	   @Autowired
	   private AuthenticationExceptionHandler exceptionHandler;
	   
	   @Value("${permit.urls}")
	   private String permittedUrls;
	   
	   @Value("${permit.auth_white_list}")
	   private String permittedSwaggerUrls;
	   
	   @Override
	   protected void doFilterInternal(HttpServletRequest request,
	      HttpServletResponse response, FilterChain filterChain)
	      throws ServletException, IOException{
		   
		   String[] endPoints = permittedSwaggerUrls.split(",");
		   boolean matches = Arrays.stream(endPoints)
		                           .map(endpoint -> endpoint.replace("/", ""))
		                           .anyMatch(request.getRequestURI()::startsWith);


		   if (Arrays.asList(permittedUrls.split(",")).contains(request.getRequestURI())
				   ||  matches) {
			  
		        filterChain.doFilter(request, response);
		        return;
		    }
	      
	      String tokenHeader = request.getHeader("Authorization");
	      String username = null;
	      String token = null;
	      System.out.println("tokenHeader-----"+tokenHeader);
	      if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
	         token = tokenHeader.substring(7);
	         try {
	            username = tokenManager.extractUsername(token);
	         } catch (IllegalArgumentException e) {
	         	 exceptionHandler.handleUnAuthorizedError(response,e,"Unable to get JWT Token!");
	         } catch (ExpiredJwtException e) {
	        	 exceptionHandler.handleUnAuthorizedError(response,e,"JWT Token Has Been Expired!");
	         }
	      } else {
	    	  
	    	  System.out.println("tokenHeader-----"+tokenHeader);
//	    	exceptionHandler.handleUnAuthorizedError(response,null,"Bearer Token Not Provided!");
	      }
	      System.out.println("SecurityContextHolder.getContext().getAuthentication()------"+SecurityContextHolder.getContext().getAuthentication());
	      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	         UserDetails userDetails = userDetailsService.loadUserByUsername(username);
	         System.out.println("userDetails-----"+userDetails.getAuthorities().toString());
	         if (tokenManager.validateToken(token, userDetails)) {
	            UsernamePasswordAuthenticationToken
	            authenticationToken = new UsernamePasswordAuthenticationToken(
	            userDetails, null,
	            userDetails.getAuthorities());
	            authenticationToken.setDetails(new
	            WebAuthenticationDetailsSource().buildDetails(request));
	            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	            Utility.setSessionUser(Utility.mapObject(userDetails, UserDTO.class));
	         }
	      }
	      filterChain.doFilter(request, response);
	   }
	}