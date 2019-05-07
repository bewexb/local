package com.asiainfo.util;

import com.asiainfo.config.LocalCoreApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtTokenUtil {
	
	
	private static String jwtKey = LocalCoreApplication.getInstance().getProperty("jwt.key");

	private static Key generatorKey(){
        SignatureAlgorithm saa = SignatureAlgorithm.HS256;
        
        byte[] bin = DatatypeConverter.parseBase64Binary(jwtKey);
        Key key = new SecretKeySpec(bin,saa.getJcaName());
        return key;
    }

    public static String generatorToken(Map<String,Object> payLoad) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();

    	return Jwts.builder().setPayload(objectMapper.writeValueAsString(payLoad))
    			.signWith(SignatureAlgorithm.HS256,generatorKey()).compact();
    }

    public static Claims phaseToken(String token){
        Jws<Claims> claimsJwt = Jwts.parser().setSigningKey(generatorKey())
        		.parseClaimsJws(token);

        return claimsJwt.getBody();
    }
    
    
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().
	    	toString().replace("-",""));
	}
}
