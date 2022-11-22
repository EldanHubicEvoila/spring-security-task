package com.evoila.springsecuritytask.util;


import com.evoila.springsecuritytask.model.AuthUser;

import io.jsonwebtoken.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtTokenUtil {

    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000;
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
    @Value("${app.jwt.secret}")
    private String SECRET_KEY;


    public String generateAccessToken(AuthUser authUser) {
        return Jwts.builder()
                .setSubject(String.format("%s,%s,%s", authUser.getUser().getId(), authUser.getUser().getUsername(), authUser.getUser().getEmail()))
                .setIssuer("EldanHubic")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            LOGGER.error("JWT expired");
        } catch (IllegalArgumentException | MalformedJwtException ex) {
            LOGGER.error("Token is null, empty or only whitespace");
        } catch (UnsupportedJwtException ex) {
            LOGGER.error("JWT is not supported");
        } catch (SignatureException ex) {
            LOGGER.error("Signature validation failed");
        }

        return false;
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    public String getUsernameFromToken(String token) {
        return getSubject(token).split(",")[1];
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
