package com.example.talentos.repository.negocio;

import com.example.talentos.model.Inscricao;
import com.example.talentos.model.enums.StatusInscricao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositório JPA para {@link Inscricao} — banco de negócio (Supabase primário).
 * Usa navegação de propriedades aninhadas (servidor.id, oportunidade.id)
 * compatível com os @ManyToOne declarados na entidade.
 */
public interface InscricaoJpaRepository extends JpaRepository<Inscricao, Long> {

    List<Inscricao> findByStatus(StatusInscricao status);

    List<Inscricao> findByServidorId(Long servidorId);

    /**
     * Verifica duplicidade pelo ID do servidor e da oportunidade.
     * Spring Data deriva a query a partir dos @ManyToOne mapeados.
     */
    boolean existsByServidorIdAndOportunidadeId(Long servidorId, Long oportunidadeId);
}
