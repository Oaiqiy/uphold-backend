package dev.oaiqiy.uphold.security;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
public class TokenCreator{
    private final Long tokenTime= 24 * 60 * 60 * 1000 * 7L;
    private final String tokenSign="token";



    public String createToken(String phone, String password){
        return Jwts.builder().setSubject(phone)
                .setExpiration(new Date(System.currentTimeMillis()+tokenTime))
                .setHeaderParam("password",password)
                .signWith(SignatureAlgorithm.HS512, tokenSign)
                .compressWith(CompressionCodecs.GZIP).compact();
    }

    public String[] decodeTokenWithPassword(String token){
        try {
            var jwt = Jwts.parser().setSigningKey(tokenSign).parseClaimsJws(token);
            String password = (String) jwt.getHeader().get("password");
            String username = jwt.getBody().getSubject();

            if(username == null || password == null)
                throw new Exception();

            return new String[]{username,password};
        }catch (Exception e){
            return null;
        }
    }


}
