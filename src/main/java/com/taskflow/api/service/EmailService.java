package com.taskflow.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void enviarEmailRecuperacaoSenha(String email, String token) {
        String link = "http://localhost:5173/redefinir-senha?token=" + token;

        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(email);
        mensagem.setSubject("Recuperação de senha");
        mensagem.setText(
                "Olá!\n\n" +
                        "Clique no link abaixo para redefinir sua senha:\n\n" +
                        link + "\n\n" +
                        "Esse link expira em 15 minutos."
        );

        mailSender.send(mensagem);
    }
}
