package com.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;


@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
   @Autowired
   private JwtUtil point;
	
   @Autowired
   private JwtFilter filter;
   
   

   @Value("${permit.urls}")
   private String[] permittedUrls;
   
   @Value("${permit.auth_white_list}")
   private String[] AUTH_WHITE_LIST;
          
//
//   @Bean
//   public GrantedAuthorityDefaults grantedAuthorityDefaults() {
//       return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
//   }

   
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
   
//   @Override
//   protected void configure(HttpSecurity http) throws Exception {
//       http.csrf(csrf -> csrf.disable())
//               .authorizeRequests(requests -> requests.antMatchers("/user/login","/user/register").permitAll()
//            		   .antMatchers(AUTH_WHITE_LIST).permitAll()
//                       .anyRequest().authenticated())
//               			.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//               
//        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
//    
//}     
   
   @Override
   protected void configure(HttpSecurity http) throws Exception {

       http.cors(withDefaults()).csrf(csrf -> csrf.disable())
               .authorizeRequests(requests -> {

                   try {
                       requests.antMatchers(permittedUrls).permitAll()
                       .antMatchers(HttpMethod.GET,"/advertise/all").permitAll()
                       .antMatchers(HttpMethod.POST,"/blood-request").permitAll()
                       .antMatchers(HttpMethod.POST,"/donate").permitAll()
                       .antMatchers(AUTH_WHITE_LIST).permitAll()
                       .anyRequest().authenticated().and().exceptionHandling(ex -> ex.authenticationEntryPoint(point));
                   } catch (Exception e) {

                       e.printStackTrace();
                   }

               })

               .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
               
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
   	}              

   @Bean
   public CorsFilter corsFilter() {
       UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
       CorsConfiguration config = new CorsConfiguration();
//       config.setAllowCredentials(true);
       config.addAllowedOrigin("*");
       config.addAllowedHeader("*");
       config.addAllowedMethod("OPTIONS");
       config.addAllowedMethod("GET");
       config.addAllowedMethod("POST");
       config.addAllowedMethod("PUT");
       config.addAllowedMethod("DELETE");
       source.registerCorsConfiguration("/**", config);
       return new CorsFilter(source);
   }
   
}

