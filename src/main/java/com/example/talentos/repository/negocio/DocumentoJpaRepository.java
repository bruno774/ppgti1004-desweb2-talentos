package com.example.talentos.repository.negocio;

import com.example.talentos.model.Documento;
import com.example.talentos.model.enums.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositório JPA para {@link Documento} — banco de negócio (Supabase primário).
 */
public interface DocumentoJpaRepository extends JpaRepository<Documento, Long> {

    /** Busca documentos por tipo (filtro de categoria). */
    List<Documento> findByTipo(TipoDocumento tipo);
}
