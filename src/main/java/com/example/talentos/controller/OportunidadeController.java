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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller REST para operações sobre Oportunidade.
 *
 * <ul>
 *   <li>GET    /oportunidades                    → lista todas</li>
 *   <li>GET    /oportunidades/{id}               → busca por ID</li>
 *   <li>GET    /oportunidades/categoria?status=X → filtra por status (categoria)</li>
 *   <li>GET    /oportunidades/area?area=X        → filtra por área</li>
 *   <li>POST   /oportunidades                    → cria (201)</li>
 *   <li>PUT    /oportunidades/{id}               → atualiza</li>
 *   <li>DELETE /oportunidades/{id}               → remove</li>
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
    public ResponseEntity<List<OportunidadeResponseDTO>> listarTodas() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OportunidadeResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/categoria")
    public ResponseEntity<List<OportunidadeResponseDTO>> buscarPorCategoria(
            @RequestParam StatusOportunidade status) {
        return ResponseEntity.ok(service.buscarPorStatus(status));
    }

    @GetMapping("/area")
    public ResponseEntity<List<OportunidadeResponseDTO>> buscarPorArea(
            @RequestParam AreaAtuacao area) {
        return ResponseEntity.ok(service.buscarPorAreaAtuacao(area));
    }

    @PostMapping
    public ResponseEntity<OportunidadeResponseDTO> criar(@Valid @RequestBody OportunidadeRequestDTO dto) {
        OportunidadeResponseDTO criada = service.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criada.getId())
                .toUri();
        return ResponseEntity.created(location).body(criada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OportunidadeResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody OportunidadeRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
