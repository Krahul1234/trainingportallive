package com.nic.trainingportal.utility;

import com.nic.trainingportal.jwt.JWT;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class Utility {
	
	@Autowired
	public JWT jwt;
	/**
	 * check null condition
	 * @param data
	 * @return
	 */
	public static final boolean checkNull(Object data)
	{
        return data == null || data.toString().length() == 0;
    }

	public static final boolean checkNotNull(Object data)
	{
        return data != null && data.toString().length() != 0;
    }


	/**
	 * @param httpservletrequest
	 * @param username
	 * @return
	 */
	public  boolean getHeaderValue(HttpServletRequest httpservletrequest)
	{
		try
		{
		  String username=jwt.extractUsername(httpservletrequest.getHeader("token"));
            return !jwt.validateToken(httpservletrequest.getHeader("token"), username);
	  }  catch(Exception e)
		{
		  return true;
		}
	}

	private static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	private static final long EXPIRATION_MS = 5 * 60 * 1000; // 5 minutes

	public static String createCaptchaToken(String captchaText) {
		return Jwts.builder()
				.claim("captcha", captchaText)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
				.signWith(key)
				.compact();
	}
	private static Set<String> usedCaptchaTokens = Collections.synchronizedSet(new HashSet<>());

	public static boolean isCaptchaTokenUsed(String token) {
		return usedCaptchaTokens.contains(token);
	}

	public static void markCaptchaTokenAsUsed(String token) {
		usedCaptchaTokens.add(token);
	}

	public static String getCaptchaFromToken(String token) {
		if (isCaptchaTokenUsed(token)) {
//			System.out.println("Captcha token already used");
			return null;
		}
		try {
			Claims claims = Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token)
					.getBody();
			return claims.get("captcha", String.class);
		} catch (ExpiredJwtException e) {
//			System.out.println("Captcha token expired");
			return null;
		} catch (JwtException e) {
//			System.out.println("Invalid captcha token");
			return null;
		}
	}

}
