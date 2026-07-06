package com.taskflow.api.dto;
import java.util.UUID;
public record CertificacaoDTO(
        UUID id,
        String certificacao,
        String instituicao,
        String codCertificacao,
        String urlComprovante
) {}