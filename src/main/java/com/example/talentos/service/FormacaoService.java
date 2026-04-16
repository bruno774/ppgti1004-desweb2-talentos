package com.example.talentos.service;

import com.example.talentos.dto.FormacaoRequestDTO;
import com.example.talentos.dto.FormacaoResponseDTO;
import com.example.talentos.model.enums.NivelFormacao;

import java.util.List;

/**
 * Interface de serviço para operações sobre Formacao.
 */
public interface FormacaoService {

    FormacaoResponseDTO criar(FormacaoRequestDTO dto);

    FormacaoResponseDTO buscarPorId(Long id);

    List<FormacaoResponseDTO> buscarTodos();

    List<FormacaoResponseDTO> buscarPorNivel(NivelFormacao nivel);

    FormacaoResponseDTO atualizar(Long id, FormacaoRequestDTO dto);

    void deletar(Long id);
}
