package com.example.talentos.controller;

import com.example.talentos.dto.ServidorRequestDTO;
import com.example.talentos.dto.ServidorResponseDTO;
import com.example.talentos.model.enums.AreaAtuacao;
import com.example.talentos.service.ServidorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller REST para operações sobre Servidor.
 *
 * <p>Usa {@code @Qualifier("padrao")} para injetar a implementação padrão.
 * Para usar auditoria, altere para {@code @Qualifier("auditoria")} ou
 * ajuste {@code app.servico.implementacao} em application.properties.</p>
 *
 * <ul>
 *   <li>GET    /servidores              → lista todos</li>
 *   <li>GET    /servidores/{id}         → busca por ID (404 se não existir)</li>
 *   <li>GET    /servidores/categoria    → filtra por área de atuação</li>
 *   <li>POST   /servidores              → cria (201 + Location)</li>
 *   <li>PUT    /servidores/{id}         → atualiza (404 se não existir)</li>
 *   <li>DELETE /servidores/{id}         → remove (204 ou 404)</li>
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
    public ResponseEntity<List<ServidorResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServidorResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/categoria")
    public ResponseEntity<List<ServidorResponseDTO>> buscarPorCategoria(
            @RequestParam AreaAtuacao area) {
        return ResponseEntity.ok(service.buscarPorAreaAtuacao(area));
    }

    @PostMapping
    public ResponseEntity<ServidorResponseDTO> criar(@Valid @RequestBody ServidorRequestDTO dto) {
        ServidorResponseDTO criado = service.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.getId())
                .toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServidorResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ServidorRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
