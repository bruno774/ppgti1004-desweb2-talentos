package com.example.talentos.repository.negocio;

import com.example.talentos.model.Oportunidade;
import com.example.talentos.model.enums.AreaAtuacao;
import com.example.talentos.model.enums.StatusOportunidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositório JPA para {@link Oportunidade} — banco de negócio (Supabase primário).
 */
public interface OportunidadeJpaRepository extends JpaRepository<Oportunidade, Long> {

    /** Busca oportunidades por status. */
    List<Oportunidade> findByStatus(StatusOportunidade status);

    /** Busca oportunidades por área de atuação. */
    List<Oportunidade> findByAreaAtuacao(AreaAtuacao areaAtuacao);
}
