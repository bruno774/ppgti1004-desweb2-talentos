package com.example.talentos.model;

import com.example.talentos.model.enums.NivelFormacao;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidade que representa a formação acadêmica de um Servidor.
 * Relacionamento N-1 com {@link Servidor} (@ManyToOne, FK: id_servidor).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "formacoes")
public class Formacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Servidor dono da formação (N-1). LAZY para evitar carregamento desnecessário.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_servidor", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Servidor servidor;

    @Column(nullable = false)
    private String instituicao;

    @Column(nullable = false)
    private String curso;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelFormacao nivel;

    @Column(name = "ano_conclusao", nullable = false)
    private Integer anoConclusao;
}
