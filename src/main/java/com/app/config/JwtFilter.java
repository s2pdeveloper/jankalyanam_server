package com.app.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.app.dto.UserDTO;
import com.app.service.imp.CustomUserDetailsService;
import com.app.utilities.Utility;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtFilter extends OncePerRequestFilter {
	   @Autowired
	   private CustomUserDetailsService userDetailsService;
	    
	   @Autowired
	   private JwtUtil tokenManager;
	   
	   @Override
	   protected void doFilterInternal(HttpServletRequest request,
	      HttpServletResponse response, FilterChain filterChain)
	      throws ServletException, IOException {
		   

		
		   System.out.println("SecurityContextHolder.getContext().getAuthentication()----"+SecurityContextHolder.getContext().getAuthentication());
	      
	      String tokenHeader = request.getHeader("Authorization");
	      String username = null;
	      String token = null;
	      if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
	         token = tokenHeader.substring(7);
	         try {
	            username = tokenManager.extractUsername(token);
	         } catch (IllegalArgumentException e) {
	            System.out.println("Unable to get JWT Token");
	         } catch (ExpiredJwtException e) {
	            System.out.println("JWT Token has expired");
	         }
	      } else {
	         System.out.println("Bearer String not found in token");
	      }
	      if (null != username && SecurityContextHolder.getContext().getAuthentication() == null) {
	         UserDetails userDetails = userDetailsService.loadUserByUsername(username);
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