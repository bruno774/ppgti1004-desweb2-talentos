package com.example.talentos.repository.negocio;

import com.example.talentos.model.Inscricao;
import com.example.talentos.model.enums.StatusInscricao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    // ------------------------------------------------------------------
    // Consultas JPQL com @Query
    // ------------------------------------------------------------------

    /**
     * JPQL com JOIN FETCH: carrega inscrições de um servidor já com os dados
     * de {@link com.example.talentos.model.Servidor} e
     * {@link com.example.talentos.model.Oportunidade} em uma única consulta SQL,
     * evitando o problema N+1 causado pelo FetchType.LAZY dos @ManyToOne.
     *
     * @param servidorId identificador do servidor
     * @return lista de inscrições com servidor e oportunidade já carregados
     */
    @Query("""
            SELECT i FROM Inscricao i
            JOIN FETCH i.servidor s
            JOIN FETCH i.oportunidade o
            WHERE s.id = :servidorId
            ORDER BY i.dataInscricao DESC
            """)
    List<Inscricao> buscarComServidorEOportunidade(@Param("servidorId") Long servidorId);

    /**
     * JPQL filtrado por status: retorna inscrições de uma oportunidade
     * com um determinado status, usando parâmetros nomeados.
     *
     * @param oportunidadeId identificador da oportunidade
     * @param status         status desejado das inscrições
     * @return lista de inscrições filtradas
     */
    @Query("""
            SELECT i FROM Inscricao i
            WHERE i.oportunidade.id = :oportunidadeId
              AND i.status = :status
            """)
    List<Inscricao> buscarPorOportunidadeEStatus(
            @Param("oportunidadeId") Long oportunidadeId,
            @Param("status") StatusInscricao status);

    // ------------------------------------------------------------------
    // Consultas SQL nativo com @Query(nativeQuery = true)
    // ------------------------------------------------------------------

    /**
     * SQL nativo: conta quantas inscrições existem por oportunidade,
     * retornando pares [id_oportunidade, total] ordenados pelo total decrescente.
     * Útil para relatórios e dashboards sem precisar carregar entidades completas.
     *
     * <p>Retorna {@code Object[]} onde {@code [0]} = id_oportunidade (Long)
     * e {@code [1]} = total (Long).</p>
     *
     * @return lista de arrays com id da oportunidade e contagem de inscrições
     */
    @Query(
        value = """
                SELECT i.id_oportunidade,
                       COUNT(i.id) AS total_inscricoes
                FROM inscricoes i
                GROUP BY i.id_oportunidade
                ORDER BY total_inscricoes DESC
                """,
        nativeQuery = true
    )
    List<Object[]> contarInscricoesPorOportunidade();
}
