package com.example.talentos.controller;

import com.example.talentos.dto.ServidorRequestDTO;
import com.example.talentos.dto.ServidorResponseDTO;
import com.example.talentos.model.enums.AreaAtuacao;
import com.example.talentos.service.ServidorService;
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
 * Controller REST para operações sobre Servidor.
 *
 * <p>Controle de acesso por papel (RBAC):</p>
 * <ul>
 *   <li>ROLE_ADMIN  — acesso total</li>
 *   <li>ROLE_GESTOR — leitura e gerenciamento de servidores</li>
 *   <li>ROLE_USUARIO — sem acesso direto a servidores</li>
 * </ul>
 *
 * <ul>
 *   <li>GET    /servidores              → ADMIN, GESTOR</li>
 *   <li>GET    /servidores/{id}         → ADMIN, GESTOR</li>
 *   <li>GET    /servidores/categoria    → ADMIN, GESTOR</li>
 *   <li>POST   /servidores              → ADMIN, GESTOR</li>
 *   <li>PUT    /servidores/{id}         → ADMIN, GESTOR</li>
 *   <li>DELETE /servidores/{id}         → ADMIN</li>
 * </ul>
 */
@RestController
@RequestMapping("/servidores")
public class ServidorController {

    private final ServidorService service;

    @Autowired
    public ServidorController(@Qualifier("padrao") ServidorService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public ResponseEntity<List<ServidorResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public ResponseEntity<ServidorResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/categoria")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public ResponseEntity<List<ServidorResponseDTO>> buscarPorCategoria(
            @RequestParam AreaAtuacao area) {
        return ResponseEntity.ok(service.buscarPorAreaAtuacao(area));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public ResponseEntity<ServidorResponseDTO> criar(@Valid @RequestBody ServidorRequestDTO dto) {
        ServidorResponseDTO criado = service.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.getId())
                .toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public ResponseEntity<ServidorResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ServidorRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
