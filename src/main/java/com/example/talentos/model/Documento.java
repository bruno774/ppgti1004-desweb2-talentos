package com.example.talentos.model;

import com.example.talentos.model.enums.TipoDocumento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade de domínio que representa um documento anexado a um servidor.
 * Não deve ser exposta diretamente na API — use DocumentoResponseDTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Documento {

    private Long id;
    private Long idServidor;
    private String nome;
    private TipoDocumento tipo;
    private String urlArquivo;
    private LocalDateTime dataUpload;
}
