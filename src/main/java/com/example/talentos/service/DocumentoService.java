package com.example.talentos.service;

import com.example.talentos.dto.DocumentoRequestDTO;
import com.example.talentos.dto.DocumentoResponseDTO;
import com.example.talentos.model.enums.TipoDocumento;

import java.util.List;

/**
 * Interface de serviço para operações sobre Documento.
 */
public interface DocumentoService {

    DocumentoResponseDTO criar(DocumentoRequestDTO dto);

    DocumentoResponseDTO buscarPorId(Long id);

    List<DocumentoResponseDTO> buscarTodos();

    List<DocumentoResponseDTO> buscarPorTipo(TipoDocumento tipo);

    DocumentoResponseDTO atualizar(Long id, DocumentoRequestDTO dto);

    void deletar(Long id);
}
