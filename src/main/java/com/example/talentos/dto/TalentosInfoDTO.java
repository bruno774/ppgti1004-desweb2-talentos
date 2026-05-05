package com.example.talentos.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO de resposta para o endpoint público {@code GET /talentos/info}.
 * Contém informações gerais sobre o sistema — acessível sem autenticação.
 */
@Data
@Builder
public class TalentosInfoDTO {

    /** Nome do sistema. */
    private String sistema;

    /** Versão atual da API. */
    private String versao;

    /** Descrição resumida da finalidade do sistema. */
    private String descricao;

    /** Status operacional da API. */
    private String status;
}
