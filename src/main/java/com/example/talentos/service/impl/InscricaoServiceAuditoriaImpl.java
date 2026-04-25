package com.example.talentos.service.impl;

import com.example.talentos.dto.InscricaoRequestDTO;
import com.example.talentos.dto.InscricaoResponseDTO;
import com.example.talentos.model.auditoria.RegistroAuditoria;
import com.example.talentos.model.enums.StatusInscricao;
import com.example.talentos.repository.auditoria.AuditoriaJpaRepository;
import com.example.talentos.service.InscricaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final AuditoriaJpaRepository auditoriaRepository;

    public InscricaoServiceAuditoriaImpl(@Qualifier("padrao") InscricaoService delegate,
                                         AuditoriaJpaRepository auditoriaRepository) {
        this.delegate = delegate;
        this.auditoriaRepository = auditoriaRepository;
    }

    @Override
    public InscricaoResponseDTO criar(InscricaoRequestDTO dto) {
        log.info("[AUDITORIA] {} - criar inscrição servidor={} oportunidade={}",
                LocalDateTime.now(), dto.getIdServidor(), dto.getIdOportunidade());
        InscricaoResponseDTO resultado = delegate.criar(dto);
        registrar("CRIAR", resultado.getId(),
                "idServidor=" + dto.getIdServidor() + ", idOportunidade=" + dto.getIdOportunidade());
        log.info("[AUDITORIA] {} - inscrição criada ID={}", LocalDateTime.now(), resultado.getId());
        return resultado;
    }

    @Override
    public InscricaoResponseDTO buscarPorId(Long id) {
        log.info("[AUDITORIA] {} - buscar inscrição ID={}", LocalDateTime.now(), id);
        registrar("BUSCAR", id, null);
        return delegate.buscarPorId(id);
    }

    @Override
    public List<InscricaoResponseDTO> buscarTodos() {
        log.info("[AUDITORIA] {} - listar todas as inscrições", LocalDateTime.now());
        registrar("BUSCAR", null, "listagem completa");
        return delegate.buscarTodos();
    }

    @Override
    public List<InscricaoResponseDTO> buscarPorStatus(StatusInscricao status) {
        log.info("[AUDITORIA] {} - buscar inscrições por status={}", LocalDateTime.now(), status);
        registrar("BUSCAR", null, "status=" + status);
        return delegate.buscarPorStatus(status);
    }

    @Override
    public InscricaoResponseDTO atualizar(Long id, InscricaoRequestDTO dto) {
        log.info("[AUDITORIA] {} - atualizar inscrição ID={}", LocalDateTime.now(), id);
        InscricaoResponseDTO resultado = delegate.atualizar(id, dto);
        registrar("ATUALIZAR", id, null);
        return resultado;
    }

    @Override
    public void deletar(Long id) {
        log.info("[AUDITORIA] {} - deletar inscrição ID={}", LocalDateTime.now(), id);
        delegate.deletar(id);
        registrar("DELETAR", id, null);
        log.info("[AUDITORIA] {} - inscrição ID={} deletada", LocalDateTime.now(), id);
    }

    @Transactional("auditoriaTransactionManager")
    protected void registrar(String operacao, Long idEntidade, String dados) {
        auditoriaRepository.save(RegistroAuditoria.builder()
                .entidade("Inscricao")
                .operacao(operacao)
                .idEntidade(idEntidade)
                .dados(dados)
                .timestamp(LocalDateTime.now())
                .build());
    }
}
