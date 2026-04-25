package com.example.talentos.repository.negocio;

import com.example.talentos.model.Formacao;
import com.example.talentos.model.enums.NivelFormacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositório JPA para {@link Formacao} — banco de negócio (Supabase primário).
 */
public interface FormacaoJpaRepository extends JpaRepository<Formacao, Long> {

    /** Busca formações por nível acadêmico (filtro de categoria). */
    List<Formacao> findByNivel(NivelFormacao nivel);
}
