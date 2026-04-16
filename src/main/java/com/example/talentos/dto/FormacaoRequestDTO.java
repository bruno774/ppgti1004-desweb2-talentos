package com.example.talentos.dto;

import com.example.talentos.model.enums.NivelFormacao;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO de entrada para criação/atualização de Formacao.
 */
@Data
public class FormacaoRequestDTO {

    @NotNull(message = "ID do servidor é obrigatório")
    @Min(value = 1, message = "ID do servidor deve ser maior que zero")
    private Long idServidor;

    @NotBlank(message = "Instituição é obrigatória")
    private String instituicao;

    @NotBlank(message = "Curso é obrigatório")
    private String curso;

    @NotNull(message = "Nível de formação é obrigatório")
    private NivelFormacao nivel;

    @NotNull(message = "Ano de conclusão é obrigatório")
    @Min(value = 1900, message = "Ano de conclusão deve ser a partir de 1900")
    @Max(value = 2100, message = "Ano de conclusão parece inválido")
    private Integer anoConclusao;
}
