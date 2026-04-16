package com.example.talentos.dto;

import com.example.talentos.model.Servidor;
import com.example.talentos.model.enums.AreaAtuacao;
import lombok.Data;

/**
 * DTO de saída para Servidor. Oculta dados sensíveis (CPF completo)
 * e representa apenas o que o cliente precisa ver.
 */
@Data
public class ServidorResponseDTO {

    private Long id;
    private String nome;
    private String cpfMascarado;
    private String email;
    private String cargo;
    private AreaAtuacao areaAtuacao;
    private boolean ativo;

    /** Converte a entidade de domínio para o DTO de resposta. */
    public static ServidorResponseDTO de(Servidor servidor) {
        ServidorResponseDTO dto = new ServidorResponseDTO();
        dto.id = servidor.getId();
        dto.nome = servidor.getNome();
        dto.cpfMascarado = mascararCpf(servidor.getCpf());
        dto.email = servidor.getEmail();
        dto.cargo = servidor.getCargo();
        dto.areaAtuacao = servidor.getAreaAtuacao();
        dto.ativo = servidor.isAtivo();
        return dto;
    }

    private static String mascararCpf(String cpf) {
        if (cpf == null || cpf.length() < 11) return "***.***.***-**";
        String digits = cpf.replaceAll("\\D", "");
        return "***." + digits.substring(3, 6) + ".***.".replace(".", ".") + "**-**";
    }
}
