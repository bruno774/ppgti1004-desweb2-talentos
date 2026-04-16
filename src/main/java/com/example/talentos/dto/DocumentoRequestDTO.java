package com.example.talentos.dto;

import com.example.talentos.model.enums.TipoDocumento;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO de entrada para criação/atualização de Documento.
 */
@Data
public class DocumentoRequestDTO {

    @NotNull(message = "ID do servidor é obrigatório")
    @Min(value = 1, message = "ID do servidor deve ser maior que zero")
    private Long idServidor;

    @NotBlank(message = "Nome do documento é obrigatório")
    private String nome;

    @NotNull(message = "Tipo do documento é obrigatório")
    private TipoDocumento tipo;

    @NotBlank(message = "URL do arquivo é obrigatória")
    private String urlArquivo;
}
