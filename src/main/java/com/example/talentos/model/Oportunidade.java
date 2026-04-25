package com.example.talentos.model;

import com.example.talentos.model.enums.AreaAtuacao;
import com.example.talentos.model.enums.StatusOportunidade;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade de domínio que representa uma oportunidade/vaga.
 * Relacionamento 1-N com {@link Inscricao} — cascade ALL, sem orphanRemoval.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "oportunidades")
public class Oportunidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "area_atuacao", nullable = false)
    private AreaAtuacao areaAtuacao;

    @Column(nullable = false)
    private Integer vagas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOportunidade status;

    /**
     * Inscrições nesta oportunidade (1-N). Cascade ALL: excluir oportunidade
     * propaga às inscrições. Sem orphanRemoval (controlado por Servidor.inscricoes).
     */
    @OneToMany(mappedBy = "oportunidade", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Inscricao> inscricoes = new ArrayList<>();
}
