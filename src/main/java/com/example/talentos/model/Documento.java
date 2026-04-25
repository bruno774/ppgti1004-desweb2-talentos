package com.example.talentos.model;

import com.example.talentos.model.enums.TipoDocumento;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidade que representa um documento de um Servidor.
 * Relacionamento N-1 com {@link Servidor} (@ManyToOne, FK: id_servidor).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "documentos")
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Servidor dono do documento (N-1). LAZY para evitar carregamento desnecessário.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_servidor", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Servidor servidor;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDocumento tipo;

    @Column(name = "url_arquivo", nullable = false)
    private String urlArquivo;

    @Column(name = "data_upload", nullable = false)
    private LocalDateTime dataUpload;
}
