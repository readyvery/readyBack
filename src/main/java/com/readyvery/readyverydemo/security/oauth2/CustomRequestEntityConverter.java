package com.readyvery.readyverydemo.security.oauth2;

import java.io.IOException;
import java.io.StringReader;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.util.MultiValueMap;

import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class CustomRequestEntityConverter implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {

	private OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter;

	public CustomRequestEntityConverter() {
		defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
	}

	@Value("${app.apple.url}")
	private String appleUrl;

	@Value("${app.apple.private-key}")
	private String privateKeyString;
	@Value("${app.apple.client-id}")
	private String appleClientId;

	@Value("${app.apple.team-id}")
	private String appleTeamId;

	@Value("${app.apple.key-id}")
	private String appleKeyId;

	@Override
	public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest req) {
		RequestEntity<?> entity = defaultConverter.convert(req);
		String registrationId = req.getClientRegistration().getRegistrationId();
		MultiValueMap<String, String> params = (MultiValueMap<String, String>)entity.getBody();
		if (registrationId.contains("apple")) {
			try {
				params.set("client_secret", createClientSecret());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return new RequestEntity<>(params, entity.getHeaders(),
			entity.getMethod(), entity.getUrl());
	}

	public PrivateKey getPrivateKey() throws IOException {
		PEMParser pemParser = new PEMParser(new StringReader(privateKeyString));
		PrivateKeyInfo object = (PrivateKeyInfo)pemParser.readObject();
		JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
		return converter.getPrivateKey(object);
	}

	public String createClientSecret() throws IOException {
		Date expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
		Map<String, Object> jwtHeader = new HashMap<>();
		jwtHeader.put("kid", appleKeyId);
		jwtHeader.put("alg", "ES256");

		return Jwts.builder()
			.setHeaderParams(jwtHeader)
			.setIssuer(appleTeamId)
			.setIssuedAt(new Date(System.currentTimeMillis())) // 발행 시간 - UNIX 시간
			.setExpiration(expirationDate) // 만료 시간
			.setAudience(appleUrl)
			.setSubject(appleClientId)
			.signWith(getPrivateKey())
			.compact();
	}
}
