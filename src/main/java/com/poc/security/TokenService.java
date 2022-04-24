package com.poc.security;

import java.util.Arrays;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.poc.model.User;
import com.poc.service.UserService;

import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logmanager.Logger;
import org.jose4j.jwt.JwtClaims;

@RequestScoped
public class TokenService {

    @Inject
    UserService userService;

    public static Logger LOGGER = Logger.getLogger(TokenService.class.getSimpleName());

    
    public String generateUserToken(String email, String username, String role) {
        return generateToken(email, username, role);
    }

    public String generateToken(String subject, String username, String... roles) {
        try {
            JwtClaims jwtClaims = new JwtClaims();
            jwtClaims.setIssuer("JWT"); 
            jwtClaims.setJwtId(UUID.randomUUID().toString());
            jwtClaims.setSubject(subject);
            jwtClaims.setClaim(Claims.upn.name(), username);
            jwtClaims.setClaim(Claims.preferred_username.name(), username); 
            jwtClaims.setClaim(Claims.groups.name(), Arrays.asList(roles));
            jwtClaims.setAudience("using-jwt");
            jwtClaims.setExpirationTimeMinutesInTheFuture(60); 

            String token = TokenUtils.generateTokenString(jwtClaims);
            LOGGER.info("TOKEN generated: " + token);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Boolean validateToken(JsonWebToken jwt){
        User user = userService.findByUsername(jwt.getName());
        return (user != null && jwt.getRawToken().equals(user.token));
    }
}
