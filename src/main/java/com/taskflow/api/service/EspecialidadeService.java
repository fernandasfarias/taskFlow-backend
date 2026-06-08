package com.taskflow.api.service;

import com.taskflow.api.dto.EspecialidadeDTO;
import com.taskflow.api.entity.Colaborador;
import com.taskflow.api.entity.Especialidade;
import com.taskflow.api.repository.ColaboradorRepository;
import com.taskflow.api.repository.EspecialidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EspecialidadeService {

    private final ColaboradorRepository colaboradorRepository;
    private final EspecialidadeRepository especialidadeRepository;

    // metodo utilizado no cadastro do colaborador: adicionar uma ou mais especialidades.
    @Transactional
    public void adicionar(UUID id, List<EspecialidadeDTO> dtos) {
        Colaborador col = colaboradorRepository.findById(id).orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));

        for (EspecialidadeDTO dto : dtos) {
            String nome = dto.nomeEspecialidade().trim().toLowerCase();
            Especialidade esp = especialidadeRepository.findByNomeEspecialidade(nome)
                    .orElseGet(() -> {
                        Especialidade nova = new Especialidade();
                        nova.setNomeEspecialidade(nome);
                        return especialidadeRepository.save(nova);
                    });

            if (!col.getEspecialidades().contains(esp)){
                col.getEspecialidades().add(esp);
            }
        }
        colaboradorRepository.save(col);
    }

    // metodo utilizado na tela de perfil: remover especialidade
    @Transactional
    public void removerEspecialidade(String email, UUID idEspecialidade){
        Colaborador colaborador = colaboradorRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("colaborador não encontrado."));
        Especialidade especialidade = especialidadeRepository.findById(idEspecialidade).orElseThrow(() -> new RuntimeException("especialidade não encontrada."));

        colaborador.getEspecialidades().removeIf(e -> e.getIdEspecialidade().equals(especialidade.getIdEspecialidade()));
        colaboradorRepository.save(colaborador);
    }

    // metodo utilizado na tela de perfil: adicionar uma especialidade por vez
    @Transactional
    public void adicionarEspecialidade(String email, EspecialidadeDTO dto){
        Colaborador colaborador = colaboradorRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("colaborador não encontrado"));

        Especialidade especialidade = new Especialidade();
        especialidade.setNomeEspecialidade(dto.nomeEspecialidade());

        especialidade = especialidadeRepository.save(especialidade);
        colaborador.getEspecialidades().add(especialidade);
        colaboradorRepository.save(colaborador);
    }
}