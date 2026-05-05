package com.example.talentos.controller;

import com.example.talentos.dto.FormacaoRequestDTO;
import com.example.talentos.dto.FormacaoResponseDTO;
import com.example.talentos.model.enums.NivelFormacao;
import com.example.talentos.service.FormacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller REST para operações sobre Formacao.
 *
 * <p>Controle de acesso por papel (RBAC):</p>
 * <ul>
 *   <li>ROLE_ADMIN  — acesso total</li>
 *   <li>ROLE_GESTOR — consulta de formações</li>
 *   <li>ROLE_USUARIO — cadastro e atualização de suas próprias formações</li>
 * </ul>
 *
 * <ul>
 *   <li>GET    /formacoes              → ADMIN, GESTOR</li>
 *   <li>GET    /formacoes/{id}         → ADMIN, GESTOR</li>
 *   <li>GET    /formacoes/categoria    → ADMIN, GESTOR</li>
 *   <li>POST   /formacoes              → ADMIN, USUARIO</li>
 *   <li>PUT    /formacoes/{id}         → ADMIN, USUARIO</li>
 *   <li>DELETE /formacoes/{id}         → ADMIN</li>
 * </ul>
 */
@RestController
@RequestMapping("/formacoes")
public class FormacaoController {

    private final FormacaoService service;

    @Autowired
    public FormacaoController(@Qualifier("padrao") FormacaoService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public ResponseEntity<List<FormacaoResponseDTO>> listarTodas() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public ResponseEntity<FormacaoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/categoria")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public ResponseEntity<List<FormacaoResponseDTO>> buscarPorCategoria(
            @RequestParam NivelFormacao nivel) {
        return ResponseEntity.ok(service.buscarPorNivel(nivel));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<FormacaoResponseDTO> criar(@Valid @RequestBody FormacaoRequestDTO dto) {
        FormacaoResponseDTO criada = service.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criada.getId())
                .toUri();
        return ResponseEntity.created(location).body(criada);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<FormacaoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FormacaoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
