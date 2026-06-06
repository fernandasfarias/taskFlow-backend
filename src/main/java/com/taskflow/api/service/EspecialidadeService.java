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

    @Transactional
    public void adicionar(UUID id, List<EspecialidadeDTO> dtos) {

        Colaborador col = colaboradorRepository.findById(id).orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));

        Set<Especialidade> especialidades = new HashSet<>();

        for (EspecialidadeDTO dto : dtos) {
            String nome = dto.nomeEspecialidade().trim().toLowerCase();
            Especialidade esp = especialidadeRepository.findByNomeEspecialidade(nome)
                    .orElseGet(() -> {
                        Especialidade nova = new Especialidade();
                        nova.setNomeEspecialidade(nome);
                        return especialidadeRepository.save(nova);
                    });
            especialidades.add(esp);
        }

        col.setEspecialidades(new ArrayList<>(especialidades));
        colaboradorRepository.save(col);
    }
}