package com.example.talentos.service.impl;

import com.example.talentos.dto.OportunidadeRequestDTO;
import com.example.talentos.dto.OportunidadeResponseDTO;
import com.example.talentos.model.auditoria.RegistroAuditoria;
import com.example.talentos.model.enums.AreaAtuacao;
import com.example.talentos.model.enums.StatusOportunidade;
import com.example.talentos.repository.auditoria.AuditoriaJpaRepository;
import com.example.talentos.service.OportunidadeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementação de <strong>auditoria</strong> de OportunidadeService.
 */
@Service
@Qualifier("auditoria")
public class OportunidadeServiceAuditoriaImpl implements OportunidadeService {

    private static final Logger log = LoggerFactory.getLogger(OportunidadeServiceAuditoriaImpl.class);

    private final OportunidadeService delegate;
    private final AuditoriaJpaRepository auditoriaRepository;

    public OportunidadeServiceAuditoriaImpl(@Qualifier("padrao") OportunidadeService delegate,
                                            AuditoriaJpaRepository auditoriaRepository) {
        this.delegate = delegate;
        this.auditoriaRepository = auditoriaRepository;
    }

    @Override
    public OportunidadeResponseDTO criar(OportunidadeRequestDTO dto) {
        log.info("[AUDITORIA] {} - criar oportunidade: titulo={}", LocalDateTime.now(), dto.getTitulo());
        OportunidadeResponseDTO resultado = delegate.criar(dto);
        registrar("CRIAR", resultado.getId(), "titulo=" + dto.getTitulo() + ", vagas=" + dto.getVagas());
        log.info("[AUDITORIA] {} - oportunidade criada ID={}", LocalDateTime.now(), resultado.getId());
        return resultado;
    }

    @Override
    public OportunidadeResponseDTO buscarPorId(Long id) {
        log.info("[AUDITORIA] {} - buscar oportunidade ID={}", LocalDateTime.now(), id);
        registrar("BUSCAR", id, null);
        return delegate.buscarPorId(id);
    }

    @Override
    public List<OportunidadeResponseDTO> buscarTodos() {
        log.info("[AUDITORIA] {} - listar todas as oportunidades", LocalDateTime.now());
        registrar("BUSCAR", null, "listagem completa");
        return delegate.buscarTodos();
    }

    @Override
    public List<OportunidadeResponseDTO> buscarPorStatus(StatusOportunidade status) {
        log.info("[AUDITORIA] {} - buscar oportunidades por status={}", LocalDateTime.now(), status);
        registrar("BUSCAR", null, "status=" + status);
        return delegate.buscarPorStatus(status);
    }

    @Override
    public List<OportunidadeResponseDTO> buscarPorAreaAtuacao(AreaAtuacao area) {
        log.info("[AUDITORIA] {} - buscar oportunidades por area={}", LocalDateTime.now(), area);
        registrar("BUSCAR", null, "area=" + area);
        return delegate.buscarPorAreaAtuacao(area);
    }

    @Override
    public OportunidadeResponseDTO atualizar(Long id, OportunidadeRequestDTO dto) {
        log.info("[AUDITORIA] {} - atualizar oportunidade ID={}", LocalDateTime.now(), id);
        OportunidadeResponseDTO resultado = delegate.atualizar(id, dto);
        registrar("ATUALIZAR", id, "titulo=" + dto.getTitulo());
        return resultado;
    }

    @Override
    public void deletar(Long id) {
        log.info("[AUDITORIA] {} - deletar oportunidade ID={}", LocalDateTime.now(), id);
        delegate.deletar(id);
        registrar("DELETAR", id, null);
        log.info("[AUDITORIA] {} - oportunidade ID={} deletada", LocalDateTime.now(), id);
    }

    @Transactional("auditoriaTransactionManager")
    protected void registrar(String operacao, Long idEntidade, String dados) {
        auditoriaRepository.save(RegistroAuditoria.builder()
                .entidade("Oportunidade")
                .operacao(operacao)
                .idEntidade(idEntidade)
                .dados(dados)
                .timestamp(LocalDateTime.now())
                .build());
    }
}
