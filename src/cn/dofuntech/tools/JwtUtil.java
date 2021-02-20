/**
 * 
 */
package cn.dofuntech.tools;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {

	public String genJWT(String id, String sub, long ttlMillis){
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		long nowMills = System.currentTimeMillis();
		Date now = new Date(nowMills);
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("userid", id);
		SecretKey key = genSecretKey();
		JwtBuilder builder = Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setSubject(sub)
				.signWith(signatureAlgorithm, key);
		if(ttlMillis >= 0){
			long expMillis = nowMills+ttlMillis;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}
		return builder.compact();
	}
	
	public Claims parseJwt(String jwt) throws Exception{
		SecretKey key = genSecretKey();
		Claims claims = Jwts.parser()
				.setSigningKey(key)
				.parseClaimsJws(jwt).getBody();
		return claims;
	}
	
	public SecretKey genSecretKey(){
		byte[] encodeKey = "mfl_da0000".getBytes();
		SecretKey key = new SecretKeySpec(encodeKey, 0, encodeKey.length, "AES");
		return key;
	}
	
	
	
}
