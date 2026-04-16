package com.example.talentos.dto;

import com.example.talentos.model.Formacao;
import com.example.talentos.model.enums.NivelFormacao;
import lombok.Data;

/**
 * DTO de saída para Formacao.
 */
@Data
public class FormacaoResponseDTO {

    private Long id;
    private Long idServidor;
    private String instituicao;
    private String curso;
    private NivelFormacao nivel;
    private Integer anoConclusao;

    /** Converte a entidade de domínio para o DTO de resposta. */
    public static FormacaoResponseDTO de(Formacao formacao) {
        FormacaoResponseDTO dto = new FormacaoResponseDTO();
        dto.id = formacao.getId();
        dto.idServidor = formacao.getIdServidor();
        dto.instituicao = formacao.getInstituicao();
        dto.curso = formacao.getCurso();
        dto.nivel = formacao.getNivel();
        dto.anoConclusao = formacao.getAnoConclusao();
        return dto;
    }
}
