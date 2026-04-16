package com.example.talentos.service.impl;

import com.example.talentos.dto.FormacaoRequestDTO;
import com.example.talentos.dto.FormacaoResponseDTO;
import com.example.talentos.model.enums.NivelFormacao;
import com.example.talentos.service.FormacaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementação de <strong>auditoria</strong> de FormacaoService.
 * Delega para {@link FormacaoServiceImpl} adicionando logging de auditoria.
 */
@Service
@Qualifier("auditoria")
public class FormacaoServiceAuditoriaImpl implements FormacaoService {

    private static final Logger log = LoggerFactory.getLogger(FormacaoServiceAuditoriaImpl.class);
    private final FormacaoService delegate;

    public FormacaoServiceAuditoriaImpl(@Qualifier("padrao") FormacaoService delegate) {
        this.delegate = delegate;
    }

    @Override
    public FormacaoResponseDTO criar(FormacaoRequestDTO dto) {
        log.info("[AUDITORIA] {} - criar formação para servidor ID={}", LocalDateTime.now(), dto.getIdServidor());
        FormacaoResponseDTO resultado = delegate.criar(dto);
        log.info("[AUDITORIA] {} - formação criada ID={}", LocalDateTime.now(), resultado.getId());
        return resultado;
    }

    @Override
    public FormacaoResponseDTO buscarPorId(Long id) {
        log.info("[AUDITORIA] {} - buscar formação ID={}", LocalDateTime.now(), id);
        return delegate.buscarPorId(id);
    }

    @Override
    public List<FormacaoResponseDTO> buscarTodos() {
        log.info("[AUDITORIA] {} - listar todas as formações", LocalDateTime.now());
        return delegate.buscarTodos();
    }

    @Override
    public List<FormacaoResponseDTO> buscarPorNivel(NivelFormacao nivel) {
        log.info("[AUDITORIA] {} - buscar formações por nível={}", LocalDateTime.now(), nivel);
        return delegate.buscarPorNivel(nivel);
    }

    @Override
    public FormacaoResponseDTO atualizar(Long id, FormacaoRequestDTO dto) {
        log.info("[AUDITORIA] {} - atualizar formação ID={}", LocalDateTime.now(), id);
        return delegate.atualizar(id, dto);
    }

    @Override
    public void deletar(Long id) {
        log.info("[AUDITORIA] {} - deletar formação ID={}", LocalDateTime.now(), id);
        delegate.deletar(id);
        log.info("[AUDITORIA] {} - formação ID={} deletada", LocalDateTime.now(), id);
    }
}
