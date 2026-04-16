package com.example.talentos.dto;

import com.example.talentos.model.enums.AreaAtuacao;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO de entrada para criação/atualização de Servidor.
 * Contém validações Bean Validation aplicadas antes do processamento.
 */
@Data
public class ServidorRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank(message = "Cargo é obrigatório")
    private String cargo;

    @NotNull(message = "Área de atuação é obrigatória")
    private AreaAtuacao areaAtuacao;

    private boolean ativo = true;
}
