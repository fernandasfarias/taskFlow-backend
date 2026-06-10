package com.taskflow.api.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import com.taskflow.api.dto.UsuarioAutenticadoDTO;
import io.jsonwebtoken.Claims;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    // metodo para obter a chave
    private SecretKey getSignKey(){
        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    // metodo para gerar token
    public String gerarToken(UsuarioAutenticadoDTO usuario){
        return Jwts.builder()
                .subject(usuario.id().toString())
                .claim("role", usuario.tipo().name()) // papel do usuario (CLIENTE, COLABORADOR ou PM)
                .issuedAt(new Date()) // momento em que o token foi gerado
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // data de expiração (24h)
                .signWith(getSignKey())
                .compact();
    }

    // metodo para extrair os claims (dados armazenados dentro do token)
    private Claims extrairClaims(String token){
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // metodo para extrair email
    public String extrairEmail(String token){
        return extrairClaims(token).getSubject();
    }

    // metodo para verificar se token está expirado
    private boolean tokenExpirado(String token){
        return extrairClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // metodo para validar token
    public boolean validarToken(String token){
        try {
            return !tokenExpirado(token);
        } catch (Exception e){
            return false;
        }
    }

    // metodo para extrair o papel do Usuario
    public String extrairRole(String token){
        return extrairClaims(token)
                .get("role", String.class);
    }
}
