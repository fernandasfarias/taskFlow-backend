package com.taskflow.api.controller;

import com.taskflow.api.dto.projectManagerDTO.ProjectManagerResponseDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.taskflow.api.dto.projectManagerDTO.CadastroProjectManagerDTO;
import com.taskflow.api.entity.ProjectManager;
import com.taskflow.api.service.ProjectManagerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/project-manager")
@RequiredArgsConstructor
public class ProjectManagerController {
    private final ProjectManagerService service;

    @PostMapping
    public ResponseEntity<ProjectManagerResponseDTO> cadastrar(@RequestBody CadastroProjectManagerDTO dto){

            return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(dto));
        }

}
