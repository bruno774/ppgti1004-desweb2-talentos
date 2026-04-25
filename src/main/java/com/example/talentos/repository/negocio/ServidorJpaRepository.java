package com.example.talentos.repository.negocio;

import com.example.talentos.model.Servidor;
import com.example.talentos.model.enums.AreaAtuacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositório JPA para {@link Servidor} — banco de negócio (Supabase primário).
 */
public interface ServidorJpaRepository extends JpaRepository<Servidor, Long> {

    /** Busca servidores pela área de atuação. */
    List<Servidor> findByAreaAtuacao(AreaAtuacao areaAtuacao);
}
