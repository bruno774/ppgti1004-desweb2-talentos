package com.example.talentos.service;

import com.example.talentos.dto.ServidorRequestDTO;
import com.example.talentos.dto.ServidorResponseDTO;
import com.example.talentos.model.enums.AreaAtuacao;

import java.util.List;

/**
 * Interface de serviço para operações sobre Servidor.
 * Define o contrato; há duas implementações: padrão e auditoria.
 *
 * @see com.example.talentos.service.impl.ServidorServiceImpl
 * @see com.example.talentos.service.impl.ServidorServiceAuditoriaImpl
 */
public interface ServidorService {

    ServidorResponseDTO criar(ServidorRequestDTO dto);

    ServidorResponseDTO buscarPorId(Long id);

    List<ServidorResponseDTO> buscarTodos();

    List<ServidorResponseDTO> buscarPorAreaAtuacao(AreaAtuacao area);

    ServidorResponseDTO atualizar(Long id, ServidorRequestDTO dto);

    void deletar(Long id);
}
