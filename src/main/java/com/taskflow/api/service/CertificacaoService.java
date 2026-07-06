package com.taskflow.api.service;

import com.taskflow.api.dto.AtualizarCertificacoesDTO;
import com.taskflow.api.dto.CertificacaoDTO;
import com.taskflow.api.dto.UsuarioAutenticadoDTO;
import com.taskflow.api.entity.Certificacao;
import com.taskflow.api.entity.ProjectManager;
import com.taskflow.api.repository.CertificacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import com.taskflow.api.repository.ProjectManagerRepository;
import com.taskflow.api.repository.CertificacaoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.taskflow.api.repository.ProjectManagerRepository;

@Service
@RequiredArgsConstructor
public class CertificacaoService {
    private final ProjectManagerRepository projectManagerRepository;
    private final CertificacaoRepository certificacaoRepository;

    // adicionar lista de certificações (cadastro)
    @Transactional
    public void adicionar (UUID idManager, List<CertificacaoDTO> dtos){
        ProjectManager pm = projectManagerRepository.findById(idManager).orElseThrow(() -> new RuntimeException("PM não encontrado"));

        for (CertificacaoDTO dto : dtos) {
            Certificacao cert = new Certificacao();
            cert.setCertificacao(dto.certificacao());
            cert.setInstituicao(dto.instituicao());
            cert.setCodigoCertificacao(dto.codCertificacao());
            cert.setUrlComprovante(dto.urlComprovante());
            cert.setProjectManager(pm);

            certificacaoRepository.save(cert);
            pm.getCertificacoes().add(cert);
        }
        projectManagerRepository.save(pm);
    }

    public void remover(UUID idCertificacao){
        Certificacao certificacao = certificacaoRepository.findById(idCertificacao).orElseThrow(() -> new RuntimeException("Certificação não encontrada"));
        certificacaoRepository.delete(certificacao);
    }
    
    {/* controller para listar certificações por email */}
    public List<CertificacaoDTO> listarPorEmail(String email){
        ProjectManager pm = projectManagerRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            return pm.getCertificacoes()
                .stream()
                .map(cert -> new CertificacaoDTO(
                    cert.getCertificacao(),
                    cert.getInstituicao(),
                    cert.getCodigoCertificacao(),
                    cert.getUrlComprovante()
                ))
                .toList();
    }
}
