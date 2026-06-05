package com.taskflow.api.service;

import com.taskflow.api.dto.*;

import com.taskflow.api.enums.TipoUsuario;
import com.taskflow.api.repository.CertificacaoRepository;
import com.taskflow.api.repository.EspecialidadeRepository;
import com.taskflow.api.repository.EmpresaRepository;
import com.taskflow.api.repository.ProjectManagerRepository;

import com.taskflow.api.entity.*;
import org.springframework.stereotype.Service;

import com.taskflow.api.repository.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.taskflow.api.service.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final ProjectManagerRepository projectManagerRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final ClienteRepository clienteRepository;

    private final CertificacaoRepository certificacaoRepository;
    private final EspecialidadeRepository especialidadeRepository;
    private final EmpresaRepository empresaRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    // metodo para buscar por email (Criei um DTO para um usuário genérico a fim de facilitar)
    private UsuarioAutenticadoDTO buscarPorEmail(String email){

        // verificando se o usuário é do tipo ProjectManager
        var projectManager = projectManagerRepository.findByEmail(email);
        if (projectManager.isPresent()){
            ProjectManager pm = projectManager.get();
            return new UsuarioAutenticadoDTO(
                    pm.getIdManager(),
                    pm.getNomeManager(),
                    pm.getEmail(),
                    pm.getSenha(),
                    TipoUsuario.PROJECT_MANAGER
            );
        }

        // verificando se o usuário é do tipo Cliente
        var cliente = clienteRepository.findByEmail(email);
        if (cliente.isPresent()){
            Cliente client = cliente.get();
            return new UsuarioAutenticadoDTO(
                    client.getIdCliente(),
                    client.getNomeCliente(),
                    client.getEmail(),
                    client.getSenha(),
                    TipoUsuario.CLIENTE
            );
        }

        // verificando se o usuário é do tipo Colaborador
        var colaborador = colaboradorRepository.findByEmail(email);
        if (colaborador.isPresent()){
            Colaborador colaborador1 = colaborador.get();
            return new UsuarioAutenticadoDTO(
                    colaborador1.getIdColaborador(),
                    colaborador1.getNome(),
                    colaborador1.getEmail(),
                    colaborador1.getSenha(),
                    TipoUsuario.COLABORADOR
            );
        }

        throw new RuntimeException("Usuário não encontrado");
    }

    // metodo de login
    public LoginResponseDTO login(LoginDTO dto){
        UsuarioAutenticadoDTO usuario = buscarPorEmail(dto.email());

        if(!passwordEncoder.matches(dto.senha(), usuario.senha())){
            throw new RuntimeException("Senha incorreta!");
        }

        // gerar token do usuario
        String token = jwtService.gerarToken(usuario);

        return new LoginResponseDTO(token);
    }

    // metodo de cadastro
    @Transactional
    public CadastroResponseDTO cadastrar(CadastroDTO dto) {

        // verificando se já existe esse email no banco de dados dos 3 tipos de usuários
        if (
                projectManagerRepository.existsByEmail(dto.email()) || clienteRepository.existsByEmail(dto.email()) || colaboradorRepository.existsByEmail(dto.email())
        ) {
            throw new RuntimeException("Email já cadastrado.");
        }

        // salvando os dados de acordo com o tipo do usuário
        switch(dto.tipo()) {
            case PROJECT_MANAGER -> {
                if (dto.certificacoes() == null || dto.certificacoes().isEmpty()){
                    throw new RuntimeException("Certificação é obrigatória para Project Manager.");
                }

                ProjectManager manager = new ProjectManager();
                manager.setNomeManager(dto.nome());
                manager.setEmail(dto.email());
                manager.setSenha(passwordEncoder.encode(dto.senha()));
                ProjectManager saved = projectManagerRepository.save(manager);

                List<Certificacao> certificacoes = new ArrayList<>();
                for (CertificacaoDTO certDTO : dto.certificacoes()){
                    Certificacao cert = new Certificacao();

                    cert.setCertificacao(certDTO.certificacao());
                    cert.setInstituicao(certDTO.instituicao());
                    cert.setCodigoCertificacao(certDTO.codCertificacao());
                    cert.setUrlComprovante(certDTO.urlComprovante());
                    cert.setProjectManager(saved);

                    certificacaoRepository.save(cert);
                    certificacoes.add(cert);
                }
                saved.setCertificacoes(certificacoes);

                return new CadastroResponseDTO(
                        saved.getIdManager(),
                        saved.getNomeManager(),
                        saved.getEmail()
                );
            }

            case CLIENTE -> {
                if (dto.empresa() == null){
                    throw new RuntimeException("Empresa é obrigatória para Cliente.");
                }

                Cliente cliente = new Cliente();
                cliente.setNomeCliente(dto.nome());
                cliente.setEmail(dto.email());
                cliente.setSenha(passwordEncoder.encode(dto.senha()));

                Empresa empresa = empresaRepository.findByCnpj(dto.empresa().cnpj()).orElseGet(() -> {
                    Empresa novaEmpresa = new Empresa();

                    novaEmpresa.setNomeEmpresa(dto.empresa().nome().trim());
                    novaEmpresa.setCnpj(dto.empresa().cnpj());

                    return empresaRepository.save(novaEmpresa);
                });
                cliente.setEmpresa(empresa);
                Cliente saved = clienteRepository.save(cliente);

                return new CadastroResponseDTO(
                        saved.getIdCliente(),
                        saved.getNomeCliente(),
                        saved.getEmail()
                );
            }

            case COLABORADOR -> {
                if (dto.especialidades() == null || dto.especialidades().isEmpty()){
                    throw new RuntimeException("Especialidades são obrigatórias para Colaboradores.");
                }

                Colaborador colaborador = new Colaborador();
                colaborador.setNome(dto.nome());
                colaborador.setEmail(dto.email());
                colaborador.setSenha(passwordEncoder.encode(dto.senha()));

                Colaborador saved = colaboradorRepository.save(colaborador);
                Set<Especialidade> especialidades = new HashSet<>();

                for (EspecialidadeDTO espec : dto.especialidades()){
                    String nomeEspecialidade = espec.nomeEspecialidade().trim().toLowerCase();

                    Especialidade especialidade = especialidadeRepository.findByNomeEspecialidade(nomeEspecialidade).orElseGet(() -> {
                        Especialidade novaEspecialidade = new Especialidade();
                        novaEspecialidade.setNomeEspecialidade(nomeEspecialidade);
                        return especialidadeRepository.save(novaEspecialidade);
                    });
                    especialidades.add(especialidade);
                }
                saved.setEspecialidades(new ArrayList<>(especialidades));
                colaboradorRepository.save(saved);

                return new CadastroResponseDTO(
                        saved.getIdColaborador(),
                        saved.getNome(),
                        saved.getEmail()
                );
            }
            default -> throw new RuntimeException("Tipo de usuário inválido");
        }
    }
}
