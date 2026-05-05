package com.example.talentos.controller;

import com.example.talentos.dto.DocumentoRequestDTO;
import com.example.talentos.dto.DocumentoResponseDTO;
import com.example.talentos.model.enums.TipoDocumento;
import com.example.talentos.service.DocumentoService;
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
 * Controller REST para operações sobre Documento.
 *
 * <p>Controle de acesso por papel (RBAC):</p>
 * <ul>
 *   <li>ROLE_ADMIN  — acesso total</li>
 *   <li>ROLE_GESTOR — consulta de documentos</li>
 *   <li>ROLE_USUARIO — upload e gestão de seus próprios documentos</li>
 * </ul>
 *
 * <ul>
 *   <li>GET    /documentos              → ADMIN, GESTOR</li>
 *   <li>GET    /documentos/{id}         → ADMIN, GESTOR</li>
 *   <li>GET    /documentos/categoria    → ADMIN, GESTOR</li>
 *   <li>POST   /documentos              → ADMIN, USUARIO</li>
 *   <li>PUT    /documentos/{id}         → ADMIN, USUARIO</li>
 *   <li>DELETE /documentos/{id}         → ADMIN</li>
 * </ul>
 */
@RestController
@RequestMapping("/documentos")
public class DocumentoController {

    private final DocumentoService service;

    @Autowired
    public DocumentoController(@Qualifier("padrao") DocumentoService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public ResponseEntity<List<DocumentoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public ResponseEntity<DocumentoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/categoria")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public ResponseEntity<List<DocumentoResponseDTO>> buscarPorCategoria(
            @RequestParam TipoDocumento tipo) {
        return ResponseEntity.ok(service.buscarPorTipo(tipo));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<DocumentoResponseDTO> criar(@Valid @RequestBody DocumentoRequestDTO dto) {
        DocumentoResponseDTO criado = service.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.getId())
                .toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<DocumentoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody DocumentoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
