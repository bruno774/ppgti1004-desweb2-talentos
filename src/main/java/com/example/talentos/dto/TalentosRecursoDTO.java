package com.example.talentos.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO de resposta para os endpoints restritos {@code GET /talentos/recursos}
 * e {@code GET /talentos/recursos/{id}}.
 *
 * <p>Representa um recurso/módulo funcional da API, retornando informações
 * sobre as permissões exigidas e a rota correspondente.</p>
 */
@Data
@Builder
public class TalentosRecursoDTO {

    /** Identificador único do recurso. */
    private Long id;

    /** Nome do módulo/recurso da API. */
    private String nome;

    /** Rota base do recurso (ex: /servidores). */
    private String rota;

    /** Papéis que possuem acesso a este recurso. */
    private String rolesPermitidas;

    /** Descrição do que o recurso oferece. */
    private String descricao;
}
