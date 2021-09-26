//package net.guides.springboot2.springboot2webappjsp;
//
//import com.sun.org.apache.xml.internal.security.algorithms.Algorithm;
//import net.guides.springboot2.springboot2webappjsp.domain.User;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//public class JwtUtil {
//    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
//
//    //    secret key
//    private static final String SECRET = "my_secret";
//
//    private static final long EXPIRATION = 1800L;
//
//
////    public static String createToken(User user) {
////        //过期时间
////        Date expireDate = new Date(System.currentTimeMillis() + EXPIRATION * 1000);
////        Map<String, Object> map = new HashMap<>();
////        map.put("alg", "HS256");
////        map.put("typ", "JWT");
////        String token = JWT.create()
////                .withHeader(map)// 添加头部
////                //可以将基本信息放到claims中
////                .withClaim("id", user.getId())//userId
////                .withClaim("userName", user.getName())//userName
////                .withClaim("name", user.getName())//name
////                .withExpiresAt(expireDate) //超时设置,设置过期的日期
////                .withIssuedAt(new Date()) //签发时间
////                .sign(Algorithm.HMAC256(SECRET)); //SECRET加密
////        return token;
////    }
////
////    /**
////     * 校验token并解析token
////     */
////    public static Map<String, Claim> verifyToken(String token) {
////        DecodedJWT jwt = null;
////        try {
////            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
////            jwt = verifier.verify(token);
////        } catch (Exception e) {
////            logger.error(e.getMessage());
////            logger.error("token解码异常");
////            //解码异常则抛出异常
////            return null;
////        }
////        return jwt.getClaims();
////    }
//
//
//}
