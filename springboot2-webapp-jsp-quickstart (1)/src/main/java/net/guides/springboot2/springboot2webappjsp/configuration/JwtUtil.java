package net.guides.springboot2.springboot2webappjsp.configuration;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import net.guides.springboot2.springboot2webappjsp.controllers.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JwtUtil {
    //the time is one hour
    public static final long EXPIRE_TIME = 60 * 60 * 1000;

    //using verify whether they are the same token
    public static boolean verify(String token, String email, String secret) {
        try {

            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("email", email).build();

            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }



    public static String sign(String email, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create().withClaim("email", email).withExpiresAt(date).sign(algorithm);

    }


    public static String getUserEmailByToken(HttpServletRequest request)  {

        String token = request.getHeader("Authorization");

        DecodedJWT jwt = JWT.decode(token);
        if (jwt.getExpiresAt().before(new Date())) {
            return ("token is expired");
        }
        return jwt.getClaim("email").asString();
    }

}
