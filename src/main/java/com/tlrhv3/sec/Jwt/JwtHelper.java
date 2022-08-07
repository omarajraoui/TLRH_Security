package com.tlrhv3.sec.Jwt;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tlrhv3.sec.Entity.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@Log4j2
public class JwtHelper {

    static final String issuer = "MyApp";

    private long accessTokenExpirationMs;

    private long refreshTokenExpirationMs;
    private Algorithm accessTokenAlgorithm;
    private Algorithm refreshTokenAlgorithm;
    private JWTVerifier accessTokenVerifier;
    private JWTVerifier refreshTokenVerifier;

    public JwtHelper(@Value("${accessTokenSecret}") String accessTokenSecret, @Value("${refreshTokenSecret}") String refreshTokenSecret, @Value("${refreshTokenExpirationDays}") int refreshTokenExpirationDays, @Value("${accessTokenExpirationMinutes}") int accessTokenExpirationMinutes) {
        accessTokenExpirationMs = (long) accessTokenExpirationMinutes * 60 * 1000;
        refreshTokenExpirationMs = (long) refreshTokenExpirationDays*60 *24 *60 *1000;
        accessTokenAlgorithm = Algorithm.HMAC512(accessTokenSecret);
        refreshTokenAlgorithm = Algorithm.HMAC512(refreshTokenSecret);
        accessTokenVerifier = JWT.require(accessTokenAlgorithm)
                .withIssuer(issuer)
                .build();
        refreshTokenVerifier = JWT.require(refreshTokenAlgorithm)
                .withIssuer(issuer)
                .build();
    }

    public String generateAccessToken(User user) {
        return
                JWT.create()
                        .withIssuer(issuer)
                        .withSubject(user.getUsername())
                        .withIssuedAt(new Date())
                        .withExpiresAt(new Date(new Date().getDate() + accessTokenExpirationMs))
                        .sign(accessTokenAlgorithm);


    }

    public String generateRefreshToken(User user, Long tokenId) {
        return
                JWT.create()
                        .withIssuer(issuer)
                        .withSubject(user.getUsername())
                        .withClaim("tokenId", tokenId)
                        .withIssuedAt(new Date())
                        .withExpiresAt(new Date(new Date().getDate() + refreshTokenExpirationMs))
                        .sign(refreshTokenAlgorithm);


    }

    private Optional<DecodedJWT> decodeAccessToken(String token) {
        try {
            return Optional.of(accessTokenVerifier.verify(token));
        } catch (JWTVerificationException e) {
            log.error("Invalid access token", e);

        }
        return Optional.empty();
    }
    private Optional<DecodedJWT> decodeRefreshToken(String token) {
        try {
            return Optional.of(refreshTokenVerifier.verify(token));
        } catch (JWTVerificationException e) {
            log.error("Invalid refresh token", e);
        }
        return Optional.empty();
    }

    public boolean validateAccessToken(String token){
        return decodeAccessToken(token).isPresent();
    }
    public boolean validateRefreshToken(String token){
        return decodeAccessToken(token).isPresent();
    }

    public String getUserIdFromAccessToken(String token){
        return decodeAccessToken(token).get().getSubject();
    }
    public String getUserIdFromRefreshToken(String token){
        return decodeAccessToken(token).get().getSubject();
    }
    public Long getTokenIdFromRefreshToken(String token){
        //error here in string to long
        return decodeRefreshToken(token).get().getClaim("tokenId").asLong();

    }

}

