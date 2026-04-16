package com.example.talentos.service;

import com.example.talentos.dto.OportunidadeRequestDTO;
import com.example.talentos.dto.OportunidadeResponseDTO;
import com.example.talentos.model.enums.AreaAtuacao;
import com.example.talentos.model.enums.StatusOportunidade;

import java.util.List;

/**
 * Interface de serviço para operações sobre Oportunidade.
 */
public interface OportunidadeService {

    OportunidadeResponseDTO criar(OportunidadeRequestDTO dto);

    OportunidadeResponseDTO buscarPorId(Long id);

    List<OportunidadeResponseDTO> buscarTodos();

    List<OportunidadeResponseDTO> buscarPorStatus(StatusOportunidade status);

    List<OportunidadeResponseDTO> buscarPorAreaAtuacao(AreaAtuacao area);

    OportunidadeResponseDTO atualizar(Long id, OportunidadeRequestDTO dto);

    void deletar(Long id);
}
