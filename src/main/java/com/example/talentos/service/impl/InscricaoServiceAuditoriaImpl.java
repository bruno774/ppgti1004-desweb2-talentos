package com.example.talentos.service.impl;

import com.example.talentos.dto.InscricaoRequestDTO;
import com.example.talentos.dto.InscricaoResponseDTO;
import com.example.talentos.model.enums.StatusInscricao;
import com.example.talentos.service.InscricaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementação de <strong>auditoria</strong> de InscricaoService.
 */
@Service
@Qualifier("auditoria")
public class InscricaoServiceAuditoriaImpl implements InscricaoService {

    private static final Logger log = LoggerFactory.getLogger(InscricaoServiceAuditoriaImpl.class);
    private final InscricaoService delegate;

    public InscricaoServiceAuditoriaImpl(@Qualifier("padrao") InscricaoService delegate) {
        this.delegate = delegate;
    }

    @Override
    public InscricaoResponseDTO criar(InscricaoRequestDTO dto) {
        log.info("[AUDITORIA] {} - criar inscrição servidor={} oportunidade={}",
                LocalDateTime.now(), dto.getIdServidor(), dto.getIdOportunidade());
        InscricaoResponseDTO resultado = delegate.criar(dto);
        log.info("[AUDITORIA] {} - inscrição criada ID={}", LocalDateTime.now(), resultado.getId());
        return resultado;
    }

    @Override
    public InscricaoResponseDTO buscarPorId(Long id) {
        log.info("[AUDITORIA] {} - buscar inscrição ID={}", LocalDateTime.now(), id);
        return delegate.buscarPorId(id);
    }

    @Override
    public List<InscricaoResponseDTO> buscarTodos() {
        log.info("[AUDITORIA] {} - listar todas as inscrições", LocalDateTime.now());
        return delegate.buscarTodos();
    }

    @Override
    public List<InscricaoResponseDTO> buscarPorStatus(StatusInscricao status) {
        log.info("[AUDITORIA] {} - buscar inscrições por status={}", LocalDateTime.now(), status);
        return delegate.buscarPorStatus(status);
    }

    @Override
    public InscricaoResponseDTO atualizar(Long id, InscricaoRequestDTO dto) {
        log.info("[AUDITORIA] {} - atualizar inscrição ID={}", LocalDateTime.now(), id);
        return delegate.atualizar(id, dto);
    }

    @Override
    public void deletar(Long id) {
        log.info("[AUDITORIA] {} - deletar inscrição ID={}", LocalDateTime.now(), id);
        delegate.deletar(id);
        log.info("[AUDITORIA] {} - inscrição ID={} deletada", LocalDateTime.now(), id);
    }
}
