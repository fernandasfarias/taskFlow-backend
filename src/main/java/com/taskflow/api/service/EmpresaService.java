package com.taskflow.api.service;

import com.taskflow.api.dto.EmpresaDTO;
import com.taskflow.api.repository.ClienteRepository;
import com.taskflow.api.repository.EmpresaRepository;

import com.taskflow.api.entity.Cliente;
import com.taskflow.api.entity.Empresa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final ClienteRepository clienteRepository;
    private final EmpresaRepository empresaRepository;

    // vincular empresa ao cliente durante o cadastro
    @Transactional
    public void vincularEmpresaCadastro(UUID idCliente, EmpresaDTO dto) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Empresa empresa = empresaRepository.findByCnpj(dto.cnpj())
                .orElseGet(() -> {
                    Empresa nova = new Empresa();
                    nova.setNomeEmpresa(dto.nome());
                    nova.setCnpj(dto.cnpj());
                    return empresaRepository.save(nova);
                });

        cliente.setEmpresa(empresa);
        clienteRepository.save(cliente);
    }

    // adicionar empresa caso o cliente remova na tela de perfil
    @Transactional
    public void adicionarEmpresa(String email, EmpresaDTO dto) {
        Cliente cliente = clienteRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Empresa empresa = empresaRepository.findByCnpj(dto.cnpj()).orElseGet(() -> {
            Empresa novaEmpresa = new Empresa();
            novaEmpresa.setNomeEmpresa(dto.nome());
            novaEmpresa.setCnpj(dto.cnpj());
            return empresaRepository.save(novaEmpresa);
        });
        cliente.setEmpresa(empresa);
        clienteRepository.save(cliente);
    }

    // remover empresa na tela de perfil
    @Transactional
    public void removerEmpresa(String email){
        Cliente cliente = clienteRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
        cliente.setEmpresa(null);
        clienteRepository.save(cliente);
    }

    // listar empresa
    public EmpresaDTO listarEmpresa(String email){
        Cliente cliente = clienteRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        Empresa empresa = cliente.getEmpresa();
        return new EmpresaDTO(empresa.getNomeEmpresa(), empresa.getCnpj());
    }
}