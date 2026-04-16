package com.example.talentos.model;

import com.example.talentos.model.enums.StatusInscricao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entidade de domínio que representa a inscrição de um servidor em uma oportunidade.
 * Não deve ser exposta diretamente na API — use InscricaoResponseDTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inscricao {

    private Long id;
    private Long idServidor;
    private Long idOportunidade;
    private LocalDate dataInscricao;
    private StatusInscricao status;
}
