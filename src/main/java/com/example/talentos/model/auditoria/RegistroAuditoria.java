package com.example.talentos.model.auditoria;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade persistida no banco de <strong>auditoria</strong> (Supabase secundário).
 * Registra cada operação realizada sobre as entidades de negócio.
 *
 * <p>Campos:</p>
 * <ul>
 *   <li>{@code entidade} — nome da classe afetada (ex: "Servidor", "Inscricao")</li>
 *   <li>{@code operacao} — CRIAR | ATUALIZAR | DELETAR | BUSCAR</li>
 *   <li>{@code idEntidade} — ID do objeto afetado (null para operações de listagem)</li>
 *   <li>{@code dados} — snapshot JSON dos dados enviados ou resultantes</li>
 *   <li>{@code timestamp} — momento exato da operação</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "registro_auditoria")
public class RegistroAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String entidade;

    @Column(nullable = false, length = 20)
    private String operacao;

    @Column(name = "id_entidade")
    private Long idEntidade;

    @Column(columnDefinition = "TEXT")
    private String dados;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
