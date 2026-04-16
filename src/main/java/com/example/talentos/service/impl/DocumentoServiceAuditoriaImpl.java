package com.example.talentos.service.impl;

import com.example.talentos.dto.DocumentoRequestDTO;
import com.example.talentos.dto.DocumentoResponseDTO;
import com.example.talentos.model.enums.TipoDocumento;
import com.example.talentos.service.DocumentoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementação de <strong>auditoria</strong> de DocumentoService.
 */
@Service
@Qualifier("auditoria")
public class DocumentoServiceAuditoriaImpl implements DocumentoService {

    private static final Logger log = LoggerFactory.getLogger(DocumentoServiceAuditoriaImpl.class);
    private final DocumentoService delegate;

    public DocumentoServiceAuditoriaImpl(@Qualifier("padrao") DocumentoService delegate) {
        this.delegate = delegate;
    }

    @Override
    public DocumentoResponseDTO criar(DocumentoRequestDTO dto) {
        log.info("[AUDITORIA] {} - criar documento tipo={} para servidor ID={}", LocalDateTime.now(), dto.getTipo(), dto.getIdServidor());
        DocumentoResponseDTO resultado = delegate.criar(dto);
        log.info("[AUDITORIA] {} - documento criado ID={}", LocalDateTime.now(), resultado.getId());
        return resultado;
    }

    @Override
    public DocumentoResponseDTO buscarPorId(Long id) {
        log.info("[AUDITORIA] {} - buscar documento ID={}", LocalDateTime.now(), id);
        return delegate.buscarPorId(id);
    }

    @Override
    public List<DocumentoResponseDTO> buscarTodos() {
        log.info("[AUDITORIA] {} - listar todos os documentos", LocalDateTime.now());
        return delegate.buscarTodos();
    }

    @Override
    public List<DocumentoResponseDTO> buscarPorTipo(TipoDocumento tipo) {
        log.info("[AUDITORIA] {} - buscar documentos por tipo={}", LocalDateTime.now(), tipo);
        return delegate.buscarPorTipo(tipo);
    }

    @Override
    public DocumentoResponseDTO atualizar(Long id, DocumentoRequestDTO dto) {
        log.info("[AUDITORIA] {} - atualizar documento ID={}", LocalDateTime.now(), id);
        return delegate.atualizar(id, dto);
    }

    @Override
    public void deletar(Long id) {
        log.info("[AUDITORIA] {} - deletar documento ID={}", LocalDateTime.now(), id);
        delegate.deletar(id);
        log.info("[AUDITORIA] {} - documento ID={} deletado", LocalDateTime.now(), id);
    }
}
