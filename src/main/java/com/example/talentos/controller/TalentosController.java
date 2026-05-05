package com.example.talentos.controller;

import com.example.talentos.dto.TalentosInfoDTO;
import com.example.talentos.dto.TalentosRecursoDTO;
import com.example.talentos.exception.RegraNegocioException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller de demonstração da camada de segurança.
 *
 * <p>Exposição dos quatro níveis de acesso exigidos pelo trab3:</p>
 * <ul>
 *   <li><b>GET /talentos/info</b>              — público (sem autenticação)</li>
 *   <li><b>GET /talentos/recursos</b>           — qualquer role autenticado (ADMIN, GESTOR, USUARIO)</li>
 *   <li><b>GET /talentos/recursos/{id}</b>      — apenas ROLE_USUARIO e ROLE_GESTOR</li>
 *   <li><b>DELETE /talentos/recursos/{id}</b>   — apenas ROLE_ADMIN</li>
 * </ul>
 *
 * <p>O controle granular é feito exclusivamente via {@code @PreAuthorize},
 * delegando a decisão de autorização ao Spring Security AOP.</p>
 */
@RestController
@RequestMapping("/talentos")
public class TalentosController {

    /**
     * Catálogo estático de recursos do sistema, indexado por ID.
     * Simula um repositório de metadados sem persistência em banco.
     */
    private static final Map<Long, TalentosRecursoDTO> RECURSOS = Map.of(
        1L, TalentosRecursoDTO.builder()
                .id(1L).nome("Servidores").rota("/servidores")
                .rolesPermitidas("ADMIN, GESTOR")
                .descricao("Gerenciamento do cadastro de servidores públicos")
                .build(),
        2L, TalentosRecursoDTO.builder()
                .id(2L).nome("Oportunidades").rota("/oportunidades")
                .rolesPermitidas("ADMIN, GESTOR, USUARIO")
                .descricao("Publicação e consulta de oportunidades internas")
                .build(),
        3L, TalentosRecursoDTO.builder()
                .id(3L).nome("Inscrições").rota("/inscricoes")
                .rolesPermitidas("ADMIN, GESTOR, USUARIO")
                .descricao("Inscrição de servidores em oportunidades")
                .build(),
        4L, TalentosRecursoDTO.builder()
                .id(4L).nome("Documentos").rota("/documentos")
                .rolesPermitidas("ADMIN, GESTOR, USUARIO")
                .descricao("Upload e gestão de documentos comprobatórios")
                .build(),
        5L, TalentosRecursoDTO.builder()
                .id(5L).nome("Formações").rota("/formacoes")
                .rolesPermitidas("ADMIN, GESTOR, USUARIO")
                .descricao("Histórico de formações acadêmicas do servidor")
                .build()
    );

    // ----------------------------------------------------------------
    // Nível 0 — público (sem autenticação, configurado no SecurityConfig)
    // ----------------------------------------------------------------

    /**
     * Retorna informações gerais públicas sobre o sistema.
     * Acesso: <b>todos</b> (sem autenticação).
     *
     * @return 200 OK com dados informativos do sistema
     */
    @GetMapping("/info")
    public ResponseEntity<TalentosInfoDTO> info() {
        TalentosInfoDTO dto = TalentosInfoDTO.builder()
                .sistema("Talentos — Sistema de Gestão de Talentos Institucionais")
                .versao("1.0.0")
                .descricao("API REST para cadastro de servidores, formações, documentos e inscrições em oportunidades internas.")
                .status("operacional")
                .build();
        return ResponseEntity.ok(dto);
    }

    // ----------------------------------------------------------------
    // Nível 1 — qualquer role autenticado
    // ----------------------------------------------------------------

    /**
     * Lista todos os recursos/módulos disponíveis na API.
     * Acesso: <b>ROLE_ADMIN, ROLE_GESTOR, ROLE_USUARIO</b>.
     *
     * @return 200 OK com lista de recursos
     */
    @GetMapping("/recursos")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR', 'USUARIO')")
    public ResponseEntity<List<TalentosRecursoDTO>> listarRecursos() {
        return ResponseEntity.ok(List.copyOf(RECURSOS.values()));
    }

    // ----------------------------------------------------------------
    // Nível 2 — ROLE_USUARIO e ROLE_GESTOR
    // ----------------------------------------------------------------

    /**
     * Retorna detalhes de um recurso específico pelo ID.
     * Acesso: <b>ROLE_USUARIO, ROLE_GESTOR</b>.
     *
     * @param id identificador do recurso (1–5)
     * @return 200 OK com dados do recurso, ou 404 se não encontrado
     */
    @GetMapping("/recursos/{id}")
    @PreAuthorize("hasAnyRole('USUARIO', 'GESTOR')")
    public ResponseEntity<TalentosRecursoDTO> buscarRecurso(@PathVariable Long id) {
        TalentosRecursoDTO recurso = RECURSOS.get(id);
        if (recurso == null) {
            throw new RegraNegocioException("Recurso com ID " + id + " não encontrado.");
        }
        return ResponseEntity.ok(recurso);
    }

    // ----------------------------------------------------------------
    // Nível 3 — apenas ROLE_ADMIN
    // ----------------------------------------------------------------

    /**
     * Remove (desativa) um recurso do sistema.
     * Acesso: <b>ROLE_ADMIN</b> exclusivamente.
     *
     * @param id identificador do recurso a remover
     * @return 204 No Content em caso de sucesso, ou 404 se não encontrado
     */
    @DeleteMapping("/recursos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removerRecurso(@PathVariable Long id) {
        if (!RECURSOS.containsKey(id)) {
            throw new RegraNegocioException("Recurso com ID " + id + " não encontrado.");
        }
        // Em produção, persistiria a remoção/desativação no banco.
        return ResponseEntity.noContent().build();
    }
}
