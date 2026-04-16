package com.example.talentos.dto;

import com.example.talentos.model.enums.AreaAtuacao;
import com.example.talentos.model.enums.StatusOportunidade;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO de entrada para criação/atualização de Oportunidade.
 */
@Data
public class OportunidadeRequestDTO {

    @NotBlank(message = "Título é obrigatório")
    private String titulo;

    private String descricao;

    @NotNull(message = "Área de atuação é obrigatória")
    private AreaAtuacao areaAtuacao;

    @NotNull(message = "Número de vagas é obrigatório")
    @Min(value = 1, message = "Deve haver ao menos 1 vaga")
    private Integer vagas;

    @NotNull(message = "Status é obrigatório")
    private StatusOportunidade status;
}
