package com.example.talentos.service;

import com.example.talentos.dto.InscricaoRequestDTO;
import com.example.talentos.dto.InscricaoResponseDTO;
import com.example.talentos.model.enums.StatusInscricao;

import java.util.List;

/**
 * Interface de serviço para operações sobre Inscricao.
 */
public interface InscricaoService {

    InscricaoResponseDTO criar(InscricaoRequestDTO dto);

    InscricaoResponseDTO buscarPorId(Long id);

    List<InscricaoResponseDTO> buscarTodos();

    List<InscricaoResponseDTO> buscarPorStatus(StatusInscricao status);

    InscricaoResponseDTO atualizar(Long id, InscricaoRequestDTO dto);

    void deletar(Long id);
}
