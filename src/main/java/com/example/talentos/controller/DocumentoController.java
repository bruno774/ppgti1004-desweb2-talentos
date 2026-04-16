package com.example.talentos.controller;

import com.example.talentos.dto.DocumentoRequestDTO;
import com.example.talentos.dto.DocumentoResponseDTO;
import com.example.talentos.model.enums.TipoDocumento;
import com.example.talentos.service.DocumentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller REST para operações sobre Documento.
 *
 * <ul>
 *   <li>GET    /documentos              → lista todos</li>
 *   <li>GET    /documentos/{id}         → busca por ID</li>
 *   <li>GET    /documentos/categoria    → filtra por tipo (categoria)</li>
 *   <li>POST   /documentos              → cria (201)</li>
 *   <li>PUT    /documentos/{id}         → atualiza</li>
 *   <li>DELETE /documentos/{id}         → remove</li>
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
    public ResponseEntity<List<DocumentoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/categoria")
    public ResponseEntity<List<DocumentoResponseDTO>> buscarPorCategoria(
            @RequestParam TipoDocumento tipo) {
        return ResponseEntity.ok(service.buscarPorTipo(tipo));
    }

    @PostMapping
    public ResponseEntity<DocumentoResponseDTO> criar(@Valid @RequestBody DocumentoRequestDTO dto) {
        DocumentoResponseDTO criado = service.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.getId())
                .toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody DocumentoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
