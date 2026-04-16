package com.example.talentos.controller;

import com.example.talentos.dto.FormacaoRequestDTO;
import com.example.talentos.dto.FormacaoResponseDTO;
import com.example.talentos.model.enums.NivelFormacao;
import com.example.talentos.service.FormacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller REST para operações sobre Formacao.
 *
 * <ul>
 *   <li>GET    /formacoes              → lista todas</li>
 *   <li>GET    /formacoes/{id}         → busca por ID</li>
 *   <li>GET    /formacoes/categoria    → filtra por nível de formação</li>
 *   <li>POST   /formacoes              → cria (201)</li>
 *   <li>PUT    /formacoes/{id}         → atualiza</li>
 *   <li>DELETE /formacoes/{id}         → remove</li>
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
    public ResponseEntity<List<FormacaoResponseDTO>> listarTodas() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormacaoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/categoria")
    public ResponseEntity<List<FormacaoResponseDTO>> buscarPorCategoria(
            @RequestParam NivelFormacao nivel) {
        return ResponseEntity.ok(service.buscarPorNivel(nivel));
    }

    @PostMapping
    public ResponseEntity<FormacaoResponseDTO> criar(@Valid @RequestBody FormacaoRequestDTO dto) {
        FormacaoResponseDTO criada = service.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criada.getId())
                .toUri();
        return ResponseEntity.created(location).body(criada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FormacaoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FormacaoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
