package com.taskflow.api.service;

import com.taskflow.api.dto.*;

import com.taskflow.api.enums.TipoUsuario;
import com.taskflow.api.entity.*;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import com.taskflow.api.repository.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import javax.management.RuntimeErrorException;

import com.taskflow.api.service.JwtService;

@Service
public class AuthService {
    private final ProjectManagerRepository projectManagerRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final ClienteRepository clienteRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthService(
            ProjectManagerRepository projectManagerRepository,
            ColaboradorRepository colaboradorRepository,
            ClienteRepository clienteRepository,
            CertificacaoRepository certificacaoRepository,
            EspecialidadeRepository especialidadeRepository,
            EmpresaRepository empresaRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            EmailService emailService
    ) {
        this.projectManagerRepository = projectManagerRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    // metodo para buscar por email
    public UsuarioAutenticadoDTO buscarPorEmail(String email){

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

    // buscar por id
    public UsuarioAutenticadoDTO buscarPorId(UUID id){

        var pm = projectManagerRepository.findById(id);
        if (pm.isPresent()){
        ProjectManager p = pm.get();
        return new UsuarioAutenticadoDTO(p.getIdManager(),p.getNomeManager(),p.getEmail(),p.getSenha(),TipoUsuario.PROJECT_MANAGER);
        }

        var cliente = clienteRepository.findById(id);
        if (cliente.isPresent()){
            Cliente c = cliente.get();
            return new UsuarioAutenticadoDTO(c.getIdCliente(),c.getNomeCliente(),c.getEmail(),c.getSenha(),TipoUsuario.CLIENTE);
        }

        var colab = colaboradorRepository.findById(id);
        if (colab.isPresent()){
            Colaborador c = colab.get();
            return new UsuarioAutenticadoDTO(c.getIdColaborador(),c.getNome(),c.getEmail(),c.getSenha(),TipoUsuario.COLABORADOR);
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

                ProjectManager manager = new ProjectManager();
                manager.setNomeManager(dto.nome());
                manager.setEmail(dto.email());
                manager.setSenha(passwordEncoder.encode(dto.senha()));
                ProjectManager saved = projectManagerRepository.save(manager);


                return new CadastroResponseDTO(
                        saved.getIdManager(),
                        saved.getNomeManager(),
                        saved.getEmail(),
                        TipoUsuario.PROJECT_MANAGER
                );
            }

            case CLIENTE -> {

                Cliente cliente = new Cliente();
                cliente.setNomeCliente(dto.nome());
                cliente.setEmail(dto.email());
                cliente.setSenha(passwordEncoder.encode(dto.senha()));

                Cliente saved = clienteRepository.save(cliente);

                return new CadastroResponseDTO(
                        saved.getIdCliente(),
                        saved.getNomeCliente(),
                        saved.getEmail(),
                        TipoUsuario.CLIENTE
                );
            }

            case COLABORADOR -> {
                Colaborador colaborador = new Colaborador();
                colaborador.setNome(dto.nome());
                colaborador.setEmail(dto.email());
                colaborador.setSenha(passwordEncoder.encode(dto.senha()));

                Colaborador saved = colaboradorRepository.save(colaborador);

                colaboradorRepository.save(saved);

                return new CadastroResponseDTO(
                        saved.getIdColaborador(),
                        saved.getNome(),
                        saved.getEmail(),
                        TipoUsuario.COLABORADOR
                );
            }
            default -> throw new RuntimeException("Tipo de usuário inválido");
        }
    }

    // metodo de solicitar recuperação de senha
    @Transactional
    public String solicitarRecuperacaoSenha(String email) {
        Optional<ProjectManager> projectManagerOptional = projectManagerRepository.findByEmail(email);
        Optional<Cliente> clienteOptional = clienteRepository.findByEmail(email);
        Optional<Colaborador> colaboradorOptional = colaboradorRepository.findByEmail(email);

        // verificando se já existe esse email no banco de dados dos 3 tipos de usuários
        if (
                projectManagerOptional.isEmpty() && clienteOptional.isEmpty() && colaboradorOptional.isEmpty()
        ) {
            throw new ServiceException("Não foi encontrado nenhum Email!");
        }

        if (colaboradorOptional.isPresent()) {
            Colaborador colaborador = colaboradorOptional.get();

            UsuarioAutenticadoDTO usuario = buscarPorEmail(email);
            // gerar token do usuario
            String token = jwtService.gerarToken(usuario);

            colaborador.setTokenRecuperacaoSenha(token);
            colaborador.setExpiracaoTokenRecuperacaoSenha(
                    LocalDateTime.now().plusMinutes(15)
            );

            colaboradorRepository.save(colaborador);

            emailService.enviarEmailRecuperacaoSenha(
                    colaborador.getEmail(),
                    token
            );
        } else if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();

            UsuarioAutenticadoDTO usuario = buscarPorEmail(email);
            // gerar token do usuario
            String token = jwtService.gerarToken(usuario);

            cliente.setTokenRecuperacaoSenha(token);
            cliente.setExpiracaoTokenRecuperacaoSenha(
                    LocalDateTime.now().plusMinutes(15)
            );

            clienteRepository.save(cliente);

            emailService.enviarEmailRecuperacaoSenha(
                    cliente.getEmail(),
                    token
            );
        } else {
            ProjectManager projectManager = projectManagerOptional.get();

            UsuarioAutenticadoDTO usuario = buscarPorEmail(email);
            // gerar token do usuario
            String token = jwtService.gerarToken(usuario);

            projectManager.setTokenRecuperacaoSenha(token);
            projectManager.setExpiracaoTokenRecuperacaoSenha(
                    LocalDateTime.now().plusMinutes(15)
            );

            projectManagerRepository.save(projectManager);

            emailService.enviarEmailRecuperacaoSenha(
                    projectManager.getEmail(),
                    token
            );
        }

        return "Será enviado instruções de recuperação, para o email informado";
    }

    // metodo de solicitar recuperação de senha
    @Transactional
    public String redefinirSenha(String token, String novaSenha) {
        if (token == null || token.isBlank()) {
            throw new RuntimeException("Token inválido.");
        }

        if (novaSenha == null || novaSenha.length() < 6) {
            throw new RuntimeException("A senha deve ter pelo menos 6 caracteres.");
        }

        Optional<ProjectManager> projectManager =
                projectManagerRepository.findByTokenRecuperacaoSenha(token);

        if (projectManager.isPresent()) {
            ProjectManager usuario = projectManager.get();
            validarExpiracao(usuario.getExpiracaoTokenRecuperacaoSenha());

            usuario.setSenha(passwordEncoder.encode(novaSenha));
            usuario.setTokenRecuperacaoSenha(null);
            usuario.setExpiracaoTokenRecuperacaoSenha(null);

            projectManagerRepository.save(usuario);
            return "Senha Alterada com sucesso";
        }

        Optional<Cliente> cliente =
                clienteRepository.findByTokenRecuperacaoSenha(token);

        if (cliente.isPresent()) {
            Cliente usuario = cliente.get();
            validarExpiracao(usuario.getExpiracaoTokenRecuperacaoSenha());

            usuario.setSenha(passwordEncoder.encode(novaSenha));
            usuario.setTokenRecuperacaoSenha(null);
            usuario.setExpiracaoTokenRecuperacaoSenha(null);

            clienteRepository.save(usuario);
            return "Senha Alterada com sucesso";
        }

        Optional<Colaborador> colaborador =
                colaboradorRepository.findByTokenRecuperacaoSenha(token);

        if (colaborador.isPresent()) {
            Colaborador usuario = colaborador.get();
            validarExpiracao(usuario.getExpiracaoTokenRecuperacaoSenha());

            usuario.setSenha(passwordEncoder.encode(novaSenha));
            usuario.setTokenRecuperacaoSenha(null);
            usuario.setExpiracaoTokenRecuperacaoSenha(null);

            colaboradorRepository.save(usuario);
            return "Senha Alterada com sucesso";
        }

        throw new RuntimeException("Token inválido.");
    }

    private void validarExpiracao(LocalDateTime expiracao) {
        if (expiracao == null || expiracao.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado.");
        }
    }
}
