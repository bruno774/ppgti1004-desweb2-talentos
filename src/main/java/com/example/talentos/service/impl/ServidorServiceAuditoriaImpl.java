package com.example.talentos.service.impl;

import com.example.talentos.dto.ServidorRequestDTO;
import com.example.talentos.dto.ServidorResponseDTO;
import com.example.talentos.model.auditoria.RegistroAuditoria;
import com.example.talentos.model.enums.AreaAtuacao;
import com.example.talentos.repository.auditoria.AuditoriaJpaRepository;
import com.example.talentos.service.ServidorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementação de <strong>auditoria</strong> de ServidorService.
 * Delega toda lógica para {@link ServidorServiceImpl} (padrão Decorator),
 * loga via SLF4J e <strong>persiste um {@link RegistroAuditoria}</strong>
 * no banco de auditoria (Supabase secundário).
 *
 * <p>Qualificador: {@code "auditoria"} — injetável via {@code @Qualifier("auditoria")}.</p>
 */
@Service
@Qualifier("auditoria")
public class ServidorServiceAuditoriaImpl implements ServidorService {

    private static final Logger log = LoggerFactory.getLogger(ServidorServiceAuditoriaImpl.class);

    private final ServidorService delegate;
    private final AuditoriaJpaRepository auditoriaRepository;

    public ServidorServiceAuditoriaImpl(@Qualifier("padrao") ServidorService delegate,
                                        AuditoriaJpaRepository auditoriaRepository) {
        this.delegate = delegate;
        this.auditoriaRepository = auditoriaRepository;
    }

    @Override
    public ServidorResponseDTO criar(ServidorRequestDTO dto) {
        log.info("[AUDITORIA] {} - criar servidor: nome={}", LocalDateTime.now(), dto.getNome());
        ServidorResponseDTO resultado = delegate.criar(dto);
        registrar("CRIAR", resultado.getId(), "nome=" + dto.getNome() + ", cpf=" + dto.getCpf());
        log.info("[AUDITORIA] {} - servidor criado com ID={}", LocalDateTime.now(), resultado.getId());
        return resultado;
    }

    @Override
    public ServidorResponseDTO buscarPorId(Long id) {
        log.info("[AUDITORIA] {} - buscar servidor ID={}", LocalDateTime.now(), id);
        registrar("BUSCAR", id, null);
        return delegate.buscarPorId(id);
    }

    @Override
    public List<ServidorResponseDTO> buscarTodos() {
        log.info("[AUDITORIA] {} - listar todos os servidores", LocalDateTime.now());
        registrar("BUSCAR", null, "listagem completa");
        return delegate.buscarTodos();
    }

    @Override
    public List<ServidorResponseDTO> buscarPorAreaAtuacao(AreaAtuacao area) {
        log.info("[AUDITORIA] {} - buscar servidores por area={}", LocalDateTime.now(), area);
        registrar("BUSCAR", null, "area=" + area);
        return delegate.buscarPorAreaAtuacao(area);
    }

    @Override
    public ServidorResponseDTO atualizar(Long id, ServidorRequestDTO dto) {
        log.info("[AUDITORIA] {} - atualizar servidor ID={}", LocalDateTime.now(), id);
        ServidorResponseDTO resultado = delegate.atualizar(id, dto);
        registrar("ATUALIZAR", id, "nome=" + dto.getNome());
        log.info("[AUDITORIA] {} - servidor ID={} atualizado", LocalDateTime.now(), id);
        return resultado;
    }

    @Override
    public void deletar(Long id) {
        log.info("[AUDITORIA] {} - deletar servidor ID={}", LocalDateTime.now(), id);
        delegate.deletar(id);
        registrar("DELETAR", id, null);
        log.info("[AUDITORIA] {} - servidor ID={} deletado", LocalDateTime.now(), id);
    }

    /**
     * Persiste um {@link RegistroAuditoria} no banco de auditoria.
     * Usa transação independente para garantir que o registro seja salvo
     * mesmo que a transação principal falhe.
     */
    @Transactional("auditoriaTransactionManager")
    protected void registrar(String operacao, Long idEntidade, String dados) {
        auditoriaRepository.save(RegistroAuditoria.builder()
                .entidade("Servidor")
                .operacao(operacao)
                .idEntidade(idEntidade)
                .dados(dados)
                .timestamp(LocalDateTime.now())
                .build());
    }
}
