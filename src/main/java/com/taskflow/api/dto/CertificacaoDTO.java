package com.taskflow.api.dto;

public record CertificacaoDTO(
        String certificacao,
        String instituicao,
        String codCertificacao,
        String urlComprovante
) {}