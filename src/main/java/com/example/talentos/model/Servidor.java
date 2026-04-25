package com.example.talentos.model;

import com.example.talentos.model.enums.AreaAtuacao;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade de domínio que representa um servidor público.
 * Não deve ser exposta diretamente na API — use ServidorResponseDTO.
 *
 * <p>Relacionamentos:</p>
 * <ul>
 *   <li>1-N com {@link Inscricao} — um servidor pode ter várias inscrições</li>
 *   <li>1-N com {@link Documento} — um servidor pode ter vários documentos</li>
 *   <li>1-N com {@link Formacao}  — um servidor pode ter várias formações</li>
 * </ul>
 * Cascade ALL + orphanRemoval nas três coleções: excluir um Servidor exclui
 * automaticamente todas as suas inscrições, documentos e formações.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "servidores")
public class Servidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String cargo;

    @Enumerated(EnumType.STRING)
    @Column(name = "area_atuacao", nullable = false)
    private AreaAtuacao areaAtuacao;

    @Column(nullable = false)
    private boolean ativo;

    // ----------------------------------------------------------------
    // Relacionamentos 1-N (lado inverso — mappedBy define a FK no filho)
    // ----------------------------------------------------------------

    /**
     * Inscrições do servidor (1-N).
     * Cascade ALL: operações propagadas às inscrições filhas.
     * orphanRemoval: remover da lista deleta a inscrição do banco.
     */
    @OneToMany(mappedBy = "servidor",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Inscricao> inscricoes = new ArrayList<>();

    /**
     * Documentos do servidor (1-N).
     * Cascade ALL + orphanRemoval: documentos sem dono são removidos do banco.
     */
    @OneToMany(mappedBy = "servidor",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Documento> documentos = new ArrayList<>();

    /**
     * Formações acadêmicas do servidor (1-N).
     * Cascade ALL + orphanRemoval: formações sem dono são removidas do banco.
     */
    @OneToMany(mappedBy = "servidor",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Formacao> formacoes = new ArrayList<>();
}
