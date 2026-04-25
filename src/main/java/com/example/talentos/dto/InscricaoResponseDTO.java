package com.example.talentos.dto;

import com.example.talentos.model.Inscricao;
import com.example.talentos.model.enums.StatusInscricao;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO de saída para Inscricao.
 */
@Data
public class InscricaoResponseDTO {

    private Long id;
    private Long idServidor;
    private Long idOportunidade;
    private LocalDate dataInscricao;
    private StatusInscricao status;

    /** Converte a entidade de domínio para o DTO de resposta. */
    public static InscricaoResponseDTO de(Inscricao inscricao) {
        InscricaoResponseDTO dto = new InscricaoResponseDTO();
        dto.id = inscricao.getId();
        dto.idServidor = inscricao.getServidor().getId();
        dto.idOportunidade = inscricao.getOportunidade().getId();
        dto.dataInscricao = inscricao.getDataInscricao();
        dto.status = inscricao.getStatus();
        return dto;
    }
}
