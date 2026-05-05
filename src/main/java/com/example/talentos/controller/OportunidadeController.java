package com.example.talentos.controller;

import com.example.talentos.dto.OportunidadeRequestDTO;
import com.example.talentos.dto.OportunidadeResponseDTO;
import com.example.talentos.model.enums.AreaAtuacao;
import com.example.talentos.model.enums.StatusOportunidade;
import com.example.talentos.service.OportunidadeService;
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
 * Controller REST para operações sobre Oportunidade.
 *
 * <p>Controle de acesso por papel (RBAC):</p>
 * <ul>
 *   <li>ROLE_ADMIN  — acesso total</li>
 *   <li>ROLE_GESTOR — criação e gestão de oportunidades</li>
 *   <li>ROLE_USUARIO — apenas consulta (GET)</li>
 * </ul>
 *
 * <ul>
 *   <li>GET    /oportunidades                    → ADMIN, GESTOR, USUARIO</li>
 *   <li>GET    /oportunidades/{id}               → ADMIN, GESTOR, USUARIO</li>
 *   <li>GET    /oportunidades/categoria?status=X → ADMIN, GESTOR, USUARIO</li>
 *   <li>GET    /oportunidades/area?area=X        → ADMIN, GESTOR, USUARIO</li>
 *   <li>POST   /oportunidades                    → ADMIN, GESTOR</li>
 *   <li>PUT    /oportunidades/{id}               → ADMIN, GESTOR</li>
 *   <li>DELETE /oportunidades/{id}               → ADMIN</li>
 * </ul>
 */
@RestController
@RequestMapping("/oportunidades")
public class OportunidadeController {

    private final OportunidadeService service;

    @Autowired
    public OportunidadeController(@Qualifier("padrao") OportunidadeService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR', 'USUARIO')")
    public ResponseEntity<List<OportunidadeResponseDTO>> listarTodas() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR', 'USUARIO')")
    public ResponseEntity<OportunidadeResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/categoria")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR', 'USUARIO')")
    public ResponseEntity<List<OportunidadeResponseDTO>> buscarPorCategoria(
            @RequestParam StatusOportunidade status) {
        return ResponseEntity.ok(service.buscarPorStatus(status));
    }

    @GetMapping("/area")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR', 'USUARIO')")
    public ResponseEntity<List<OportunidadeResponseDTO>> buscarPorArea(
            @RequestParam AreaAtuacao area) {
        return ResponseEntity.ok(service.buscarPorAreaAtuacao(area));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public ResponseEntity<OportunidadeResponseDTO> criar(@Valid @RequestBody OportunidadeRequestDTO dto) {
        OportunidadeResponseDTO criada = service.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criada.getId())
                .toUri();
        return ResponseEntity.created(location).body(criada);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public ResponseEntity<OportunidadeResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody OportunidadeRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
