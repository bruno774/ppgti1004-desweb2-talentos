package com.example.talentos.model;

import com.example.talentos.model.enums.StatusInscricao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Entidade que representa a inscrição de um Servidor em uma Oportunidade.
 * Implementa a cardinalidade N-N entre Servidor e Oportunidade,
 * funcionando como tabela associativa enriquecida (com status e data).
 *
 * Relacionamentos:
 *  - N-1 com {@link Servidor}     (@ManyToOne, FK: id_servidor)
 *  - N-1 com {@link Oportunidade} (@ManyToOne, FK: id_oportunidade)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "inscricoes",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_inscricao_servidor_oportunidade",
        columnNames = {"id_servidor", "id_oportunidade"}
    )
)
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Servidor inscrito (N-1). LAZY para evitar carregamento desnecessário.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_servidor", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Servidor servidor;

    /**
     * Oportunidade da inscrição (N-1). LAZY para evitar carregamento desnecessário.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_oportunidade", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Oportunidade oportunidade;

    @Column(name = "data_inscricao", nullable = false)
    private LocalDate dataInscricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusInscricao status;
}
