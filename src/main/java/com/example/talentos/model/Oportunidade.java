package com.example.talentos.model;

import com.example.talentos.model.enums.AreaAtuacao;
import com.example.talentos.model.enums.StatusOportunidade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade de domínio que representa uma oportunidade/vaga.
 * Não deve ser exposta diretamente na API — use OportunidadeResponseDTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Oportunidade {

    private Long id;
    private String titulo;
    private String descricao;
    private AreaAtuacao areaAtuacao;
    private Integer vagas;
    private StatusOportunidade status;
}
