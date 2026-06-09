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

    public void atualizar(UUID id, CertificacaoDTO dto){
        Certificacao certificacao = certificacaoRepository.findById(id).orElseThrow(() -> new RuntimeException("Certificação não encontrada"));

        if(dto.certificacao() != null){
            certificacao.setCertificacao(dto.certificacao());
        }
        if(dto.instituicao() != null){
            certificacao.setInstituicao(dto.instituicao());
        }
        if(dto.codCertificacao() != null){
            certificacao.setCodigoCertificacao(dto.codCertificacao());
        }
        if(dto.urlComprovante() != null){
            certificacao.setUrlComprovante(dto.urlComprovante());
        }
        certificacaoRepository.save(certificacao);
    }

    public void remover(UUID idCertificacao){
        Certificacao certificacao = certificacaoRepository.findById(idCertificacao).orElseThrow(() -> new RuntimeException("Certificação não encontrada"));
        certificacaoRepository.delete(certificacao);
    }

    // adicionar somente uma certificacao na tela de perfil
    public void adicionarCertificacao(String email, CertificacaoDTO dto){
        ProjectManager pm = projectManagerRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("PM não encontrado"));

        Certificacao certificacao = new Certificacao();
        certificacao.setCertificacao(dto.certificacao());
        certificacao.setInstituicao(dto.instituicao());
        certificacao.setCodigoCertificacao(dto.codCertificacao());
        certificacao.setUrlComprovante(dto.urlComprovante());
        certificacao.setProjectManager(pm);

        certificacaoRepository.save(certificacao);
    }

    // metodo para listar as certificações
    public List<CertificacaoDTO> listarPorEmail(String email){
        ProjectManager pm = projectManagerRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        return pm.getCertificacoes().stream().map(cert -> new CertificacaoDTO(cert.getCertificacao(), cert.getInstituicao(), cert.getCodigoCertificacao(), cert.getUrlComprovante())).toList();
    }
}
