package com.project.ElectronicStore.config;

import com.project.ElectronicStore.security.JwtAuthenticationEntryPoint;
import com.project.ElectronicStore.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class  SecurityConfig {

      @Autowired
      private UserDetailsService userDetailsService;
      @Autowired
      private JwtAuthenticationEntryPoint authenticationEntryPoint;
      @Autowired
      private JwtAuthenticationFilter authenticationFilter;

/*  //below commented out code provide in memory implementation of providing user details.
      @Bean
      public UserDetailsService userDetailsService(){
            //create user
            UserDetails normal = User.builder()
                    .username("Karan")
                    .password(passwordEncoder().encode("karan"))
                    .roles("NORMAL")
                    .build();
            UserDetails admin = User.builder()
                    .username("NeelKamal")
                    .password(passwordEncoder().encode("neel"))
                    .roles("ADMIN").build();

            return new InMemoryUserDetailsManager(normal, admin);

      }
*/
      @Bean
      public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .cors().disable()
                    .csrf().disable()
                    .authorizeRequests()
                              .antMatchers("/auth/login")
                              .permitAll()
                              .antMatchers(HttpMethod.POST, "/users")
                              .permitAll()
                    .antMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
                              .anyRequest()
                              .authenticated()
                    .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
      }

      @Bean
      public DaoAuthenticationProvider authenticationProvider(){
            DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
            daoAuthenticationProvider.setUserDetailsService(userDetailsService);
            daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
            return daoAuthenticationProvider;
      }

      @Bean
      public PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
      }

      @Bean
      public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
            return configuration.getAuthenticationManager();
      }
}
