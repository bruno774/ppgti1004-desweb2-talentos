package com.example.talentos.service.impl;

import com.example.talentos.dto.ServidorRequestDTO;
import com.example.talentos.dto.ServidorResponseDTO;
import com.example.talentos.model.enums.AreaAtuacao;
import com.example.talentos.repository.ServidorRepository;
import com.example.talentos.service.ServidorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementação de <strong>auditoria</strong> de ServidorService.
 * Delega toda lógica para {@link ServidorServiceImpl} e adiciona
 * logging detalhado com timestamp de cada operação — demonstra o
 * padrão Decorator e o uso de múltiplos qualificadores com baixo acoplamento.
 *
 * <p>Qualificador: {@code "auditoria"} — injetável via {@code @Qualifier("auditoria")}.</p>
 */
@Service
@Qualifier("auditoria")
public class ServidorServiceAuditoriaImpl implements ServidorService {

    private static final Logger log = LoggerFactory.getLogger(ServidorServiceAuditoriaImpl.class);

    private final ServidorService delegate;

    /**
     * Injeta a implementação padrão como delegate para não duplicar lógica.
     * Usa @Qualifier("padrao") para resolver a ambiguidade.
     */
    public ServidorServiceAuditoriaImpl(@Qualifier("padrao") ServidorService delegate) {
        this.delegate = delegate;
    }

    @Override
    public ServidorResponseDTO criar(ServidorRequestDTO dto) {
        log.info("[AUDITORIA] {} - criar servidor: nome={}", LocalDateTime.now(), dto.getNome());
        ServidorResponseDTO resultado = delegate.criar(dto);
        log.info("[AUDITORIA] {} - servidor criado com ID={}", LocalDateTime.now(), resultado.getId());
        return resultado;
    }

    @Override
    public ServidorResponseDTO buscarPorId(Long id) {
        log.info("[AUDITORIA] {} - buscar servidor ID={}", LocalDateTime.now(), id);
        return delegate.buscarPorId(id);
    }

    @Override
    public List<ServidorResponseDTO> buscarTodos() {
        log.info("[AUDITORIA] {} - listar todos os servidores", LocalDateTime.now());
        return delegate.buscarTodos();
    }

    @Override
    public List<ServidorResponseDTO> buscarPorAreaAtuacao(AreaAtuacao area) {
        log.info("[AUDITORIA] {} - buscar servidores por area={}", LocalDateTime.now(), area);
        return delegate.buscarPorAreaAtuacao(area);
    }

    @Override
    public ServidorResponseDTO atualizar(Long id, ServidorRequestDTO dto) {
        log.info("[AUDITORIA] {} - atualizar servidor ID={}", LocalDateTime.now(), id);
        ServidorResponseDTO resultado = delegate.atualizar(id, dto);
        log.info("[AUDITORIA] {} - servidor ID={} atualizado", LocalDateTime.now(), id);
        return resultado;
    }

    @Override
    public void deletar(Long id) {
        log.info("[AUDITORIA] {} - deletar servidor ID={}", LocalDateTime.now(), id);
        delegate.deletar(id);
        log.info("[AUDITORIA] {} - servidor ID={} deletado", LocalDateTime.now(), id);
    }
}
