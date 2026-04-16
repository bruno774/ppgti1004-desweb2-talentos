package com.example.talentos.dto;

import com.example.talentos.model.Oportunidade;
import com.example.talentos.model.enums.AreaAtuacao;
import com.example.talentos.model.enums.StatusOportunidade;
import lombok.Data;

/**
 * DTO de saída para Oportunidade.
 */
@Data
public class OportunidadeResponseDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private AreaAtuacao areaAtuacao;
    private Integer vagas;
    private StatusOportunidade status;

    /** Converte a entidade de domínio para o DTO de resposta. */
    public static OportunidadeResponseDTO de(Oportunidade oportunidade) {
        OportunidadeResponseDTO dto = new OportunidadeResponseDTO();
        dto.id = oportunidade.getId();
        dto.titulo = oportunidade.getTitulo();
        dto.descricao = oportunidade.getDescricao();
        dto.areaAtuacao = oportunidade.getAreaAtuacao();
        dto.vagas = oportunidade.getVagas();
        dto.status = oportunidade.getStatus();
        return dto;
    }
}
