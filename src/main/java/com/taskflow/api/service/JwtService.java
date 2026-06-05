package com.taskflow.api.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import com.taskflow.api.dto.UsuarioAutenticadoDTO;

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
                .subject(usuario.email()) // identifica o usuario (email)
                .claim("role", usuario.tipo().name()) // papel do usuario (CLIENTE, COLABORADOR ou PM)
                .issuedAt(new Date()) // momento em que o token foi gerado
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // data de expiração (24h)
                .signWith(getSignKey())
                .compact();
    }
}
