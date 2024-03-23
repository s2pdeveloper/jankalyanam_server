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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static org.springframework.security.config.Customizer.withDefaults;


@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {



	@Autowired
	private JwtUtil point;
	
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
                       requests.antMatchers("/user/login","/user/register").permitAll()
                               .antMatchers(AUTH_WHITE_LIST).permitAll()
                               .anyRequest().authenticated().and().exceptionHandling(ex -> ex.authenticationEntryPoint(point));
                   } catch (Exception e) {
                       // TODO Auto-generated catch block
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

