package taskmanager.security;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import taskmanager.security.jwt.AuthEntryPointJwt;
import taskmanager.security.jwt.AuthTokenFilter;
import taskmanager.security.jwt.JwtUtils;
import taskmanager.security.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true)  // Enables @PreAuthorize in controllers
public class WebSecurityConfig {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;
    @Autowired
    private JwtUtils jwtUtils;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(jwtUtils, userDetailsService);
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Configuration
    public class WebConfig implements WebMvcConfigurer {
    	
    	@Bean
    	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	    http
    	        .cors(cors -> cors.configurationSource(request -> {
    	            CorsConfiguration config = new CorsConfiguration();
    	            config.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:5174", "http://localhost:3000")); // Allow frontend
    	            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allow all RESTful methods
    	            config.setAllowedHeaders(List.of("Authorization", "Content-Type")); // Allow important headers
    	            config.setAllowCredentials(true); // Allow cookies or JWT authentication
    	            return config;
    	        }))
    	        .csrf(csrf -> csrf.disable()) // Disable CSRF for testing
    	        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
    	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    	        .authorizeHttpRequests(auth ->
    	            auth.requestMatchers("/api/**").permitAll() // Allow all API requests for testing
    	                .requestMatchers("/h2-console/**").permitAll()
    	                .requestMatchers("/swagger-ui/**").permitAll()
    	                .requestMatchers("/actuator/**").permitAll()
    	                .anyRequest().authenticated()
    	        )
    	        .authenticationProvider(authenticationProvider())
    	        .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class) // Enable if using JWT
    	        .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

    	    return http.build();
    	}

    
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"));
    }
    
    
    }
    
    
   
}


    
    
