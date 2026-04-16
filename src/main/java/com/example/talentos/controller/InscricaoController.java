package com.example.talentos.controller;

import com.example.talentos.dto.InscricaoRequestDTO;
import com.example.talentos.dto.InscricaoResponseDTO;
import com.example.talentos.model.enums.StatusInscricao;
import com.example.talentos.service.InscricaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller REST para operações sobre Inscricao.
 *
 * <ul>
 *   <li>GET    /inscricoes              → lista todas</li>
 *   <li>GET    /inscricoes/{id}         → busca por ID</li>
 *   <li>GET    /inscricoes/categoria    → filtra por status (categoria)</li>
 *   <li>POST   /inscricoes              → cria (201) — valida servidor ativo + oportunidade aberta</li>
 *   <li>PUT    /inscricoes/{id}         → atualiza</li>
 *   <li>DELETE /inscricoes/{id}         → remove</li>
 * </ul>
 */
@RestController
@RequestMapping("/inscricoes")
public class InscricaoController {

    private final InscricaoService service;

    @Autowired
    public InscricaoController(@Qualifier("padrao") InscricaoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<InscricaoResponseDTO>> listarTodas() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InscricaoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/categoria")
    public ResponseEntity<List<InscricaoResponseDTO>> buscarPorCategoria(
            @RequestParam StatusInscricao status) {
        return ResponseEntity.ok(service.buscarPorStatus(status));
    }

    @PostMapping
    public ResponseEntity<InscricaoResponseDTO> criar(@Valid @RequestBody InscricaoRequestDTO dto) {
        InscricaoResponseDTO criada = service.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criada.getId())
                .toUri();
        return ResponseEntity.created(location).body(criada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InscricaoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody InscricaoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
