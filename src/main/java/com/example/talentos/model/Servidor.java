package com.example.talentos.model;

import com.example.talentos.model.enums.AreaAtuacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade de domínio que representa um servidor público.
 * Não deve ser exposta diretamente na API — use ServidorResponseDTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Servidor {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String cargo;
    private AreaAtuacao areaAtuacao;
    private boolean ativo;
}
