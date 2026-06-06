package com.taskflow.api.service;

import com.taskflow.api.dto.CertificacaoDTO;
import com.taskflow.api.entity.Certificacao;
import com.taskflow.api.entity.ProjectManager;
import com.taskflow.api.repository.CertificacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.taskflow.api.repository.ProjectManagerRepository;
import com.taskflow.api.repository.CertificacaoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificacaoService {
    private final ProjectManagerRepository projectManagerRepository;
    private final CertificacaoRepository certificacaoRepository;

    @Transactional
    public void adicionar (UUID idManager, List<CertificacaoDTO> dtos){
        ProjectManager pm = projectManagerRepository.findById(idManager).orElseThrow(() -> new RuntimeException("PM não encontrado"));

        List<Certificacao> certificacaos = new ArrayList<>();

        for (CertificacaoDTO dto : dtos) {
            Certificacao cert = new Certificacao();
            cert.setCertificacao(dto.certificacao());
            cert.setInstituicao(dto.instituicao());
            cert.setCodigoCertificacao(dto.codCertificacao());
            cert.setUrlComprovante(dto.urlComprovante());
            cert.setProjectManager(pm);

            certificacaoRepository.save(cert);
            certificacaos.add(cert);
        }

        pm.setCertificacoes(certificacaos);
        projectManagerRepository.save(pm);
    }
}
