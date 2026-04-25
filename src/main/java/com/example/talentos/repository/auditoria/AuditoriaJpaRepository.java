package com.example.talentos.repository.auditoria;

import com.example.talentos.model.auditoria.RegistroAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositório JPA para {@link RegistroAuditoria} — banco de auditoria (Supabase secundário).
 *
 * <p>Operações de escrita devem usar
 * {@code @Transactional("auditoriaTransactionManager")} nos serviços.</p>
 */
public interface AuditoriaJpaRepository extends JpaRepository<RegistroAuditoria, Long> {

    /** Busca registros de auditoria de uma entidade específica. */
    List<RegistroAuditoria> findByEntidade(String entidade);

    /** Busca registros de auditoria em um intervalo de tempo. */
    List<RegistroAuditoria> findByTimestampBetween(LocalDateTime inicio, LocalDateTime fim);
}
