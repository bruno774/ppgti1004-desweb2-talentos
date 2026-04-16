package com.example.talentos.dto;

import com.example.talentos.model.Documento;
import com.example.talentos.model.enums.TipoDocumento;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO de saída para Documento.
 */
@Data
public class DocumentoResponseDTO {

    private Long id;
    private Long idServidor;
    private String nome;
    private TipoDocumento tipo;
    private String urlArquivo;
    private LocalDateTime dataUpload;

    /** Converte a entidade de domínio para o DTO de resposta. */
    public static DocumentoResponseDTO de(Documento documento) {
        DocumentoResponseDTO dto = new DocumentoResponseDTO();
        dto.id = documento.getId();
        dto.idServidor = documento.getIdServidor();
        dto.nome = documento.getNome();
        dto.tipo = documento.getTipo();
        dto.urlArquivo = documento.getUrlArquivo();
        dto.dataUpload = documento.getDataUpload();
        return dto;
    }
}
