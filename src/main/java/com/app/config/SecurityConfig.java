package com.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



//@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


   @Autowired
   private JwtFilter filter;
   
   private static final String[] AUTH_WHITE_LIST = {
           "/v3/api-docs/**",
           "/v2/api-docs/**",
           "/swagger-resources/**",
           "/swagger-ui.html/**",
           "/webjars/**"
   };

   @Bean
   public PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
   }
  
   @Bean
   @Override
   public AuthenticationManager authenticationManagerBean() throws
   Exception {
      return super.authenticationManagerBean();
   }
   
   @Override
   protected void configure(HttpSecurity http) throws Exception {
       http.csrf(csrf -> csrf.disable())
               .authorizeRequests(requests -> requests.antMatchers("/user/register","/user/login").permitAll()
            		   .antMatchers(AUTH_WHITE_LIST).permitAll()
                       .anyRequest().authenticated())
               			.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
               
          
    
      http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
   }
}

