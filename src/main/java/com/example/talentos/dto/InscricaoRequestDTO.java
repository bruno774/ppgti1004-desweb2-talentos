package com.example.talentos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO de entrada para criação/atualização de Inscricao.
 */
@Data
public class InscricaoRequestDTO {

    @NotNull(message = "ID do servidor é obrigatório")
    @Min(value = 1, message = "ID do servidor deve ser maior que zero")
    private Long idServidor;

    @NotNull(message = "ID da oportunidade é obrigatório")
    @Min(value = 1, message = "ID da oportunidade deve ser maior que zero")
    private Long idOportunidade;
}
