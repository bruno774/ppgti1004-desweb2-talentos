package com.example.talentos.model;

import com.example.talentos.model.enums.NivelFormacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade de domínio que representa a formação acadêmica de um servidor.
 * Não deve ser exposta diretamente na API — use FormacaoResponseDTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Formacao {

    private Long id;
    private Long idServidor;
    private String instituicao;
    private String curso;
    private NivelFormacao nivel;
    private Integer anoConclusao;
}
