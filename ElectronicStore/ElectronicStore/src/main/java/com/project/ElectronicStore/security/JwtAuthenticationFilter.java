package com.project.ElectronicStore.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

      @Autowired
      private JwtHelper jwtHelper;

      @Autowired
       private UserDetailsService service;

      Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);
      @Override
      protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

            //Authorization
            String requestHeader = request.getHeader("Authorization");
            logger.info("Header: {}", requestHeader);
            String username = null;
            String token = null;
            if(requestHeader!=null && requestHeader.startsWith("Bearer")){
                  token = requestHeader.substring(7);
                  try {

                        username = jwtHelper.getUsernameFromToken(token);

                  }catch(IllegalArgumentException e){
                        logger.info("Illegal Argument while fetching username");
                        e.printStackTrace();
                  }catch(ExpiredJwtException ej){
                        logger.info("given jwt token is expired");
                        ej.printStackTrace();
                  }catch (MalformedJwtException me){
                        logger.info("Invalid token");
                        me.printStackTrace();
                  }catch (Exception ee){
                        ee.printStackTrace();
                  }

            }else{
                  logger.info("Invalid Header Value !!");
            }
            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                  //fetch user details from user name to validate token before setting authentication
                  UserDetails userDetails = service.loadUserByUsername(username);
                  Boolean validateToken = jwtHelper.validateToken(token, userDetails);
                  if(validateToken){
                        //set authentication
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));//i think binding is done fr this request
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                  }else{
                        logger.info("Validation fails !!");
                  }
            }

            filterChain.doFilter(request, response);
      }
}
