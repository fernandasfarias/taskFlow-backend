package com.taskflow.api.dto;

import java.time.LocalDate;
import java.util.UUID;
import java.util.List;

import lombok.Data;

public record ProjetoDTO (
    UUID id,
    String nome,
    String descricao,
    LocalDate dataInicio,
    LocalDate dataEntrega,
    Float orcamento,
    UUID idManager,
    List<UUID> idClientes,
    List<UUID> idColaboradores
){

}
