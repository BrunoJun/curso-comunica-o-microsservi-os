package org.me.productapi.module.jwt.service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.me.productapi.config.exception.AuthenticationException;
import org.me.productapi.module.jwt.dto.JwtResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class JwtService {

    @Value("${app-config.secrets.api-secret}")
    private String apiSecret;

    public void isAuthorized(String token){

        var accessToken = extractToken(token);

        try {

            var claims = Jwts
                    .parser()
                    .setSigningKey(Keys.hmacShaKeyFor(apiSecret.getBytes()))
                    .build()
                    .parseSignedClaims(accessToken)
                    .getBody();

            var user = JwtResponse.getUser(claims);

            if (isEmpty(user) || isEmpty(user.getId())){

                throw new AuthenticationException("User not valid.");
            }
        } catch (Exception e){

            e.printStackTrace();
            throw new AuthenticationException("Error while trying to process the access token.");
        }
    }

    private String extractToken(String token){

        if (isEmpty(token)){

            throw new AuthenticationException("The access token was not informed.");
        }

        if (token.contains(" ")){

            return token.split(" ")[1];
        }

        return token;
    }
}
