package com.example.talentos.service.impl;

import com.example.talentos.dto.OportunidadeRequestDTO;
import com.example.talentos.dto.OportunidadeResponseDTO;
import com.example.talentos.exception.RegraNegocioException;
import com.example.talentos.model.Oportunidade;
import com.example.talentos.model.enums.AreaAtuacao;
import com.example.talentos.model.enums.StatusOportunidade;
import com.example.talentos.repository.OportunidadeRepository;
import com.example.talentos.service.OportunidadeService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Implementação <strong>padrão</strong> de OportunidadeService.
 */
@Service
@Qualifier("padrao")
public class OportunidadeServiceImpl implements OportunidadeService {

    private final OportunidadeRepository repository;

    public OportunidadeServiceImpl(OportunidadeRepository repository) {
        this.repository = repository;
    }

    @Override
    public OportunidadeResponseDTO criar(OportunidadeRequestDTO dto) {
        // Regra de negócio: não permitir criar oportunidade já cancelada
        if (dto.getStatus() == StatusOportunidade.CANCELADA) {
            throw new RegraNegocioException("Não é possível criar uma oportunidade já cancelada.");
        }

        Oportunidade oportunidade = Oportunidade.builder()
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .areaAtuacao(dto.getAreaAtuacao())
                .vagas(dto.getVagas())
                .status(dto.getStatus())
                .build();

        return OportunidadeResponseDTO.de(repository.salvar(oportunidade));
    }

    @Override
    public OportunidadeResponseDTO buscarPorId(Long id) {
        return OportunidadeResponseDTO.de(repository.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Oportunidade não encontrada com ID: " + id)));
    }

    @Override
    public List<OportunidadeResponseDTO> buscarTodos() {
        return repository.buscarTodos().stream()
                .map(OportunidadeResponseDTO::de)
                .collect(Collectors.toList());
    }

    @Override
    public List<OportunidadeResponseDTO> buscarPorStatus(StatusOportunidade status) {
        return repository.buscarPorStatus(status).stream()
                .map(OportunidadeResponseDTO::de)
                .collect(Collectors.toList());
    }

    @Override
    public List<OportunidadeResponseDTO> buscarPorAreaAtuacao(AreaAtuacao area) {
        return repository.buscarPorAreaAtuacao(area).stream()
                .map(OportunidadeResponseDTO::de)
                .collect(Collectors.toList());
    }

    @Override
    public OportunidadeResponseDTO atualizar(Long id, OportunidadeRequestDTO dto) {
        Oportunidade existente = repository.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Oportunidade não encontrada com ID: " + id));

        existente.setTitulo(dto.getTitulo());
        existente.setDescricao(dto.getDescricao());
        existente.setAreaAtuacao(dto.getAreaAtuacao());
        existente.setVagas(dto.getVagas());
        existente.setStatus(dto.getStatus());

        return OportunidadeResponseDTO.de(repository.salvar(existente));
    }

    @Override
    public void deletar(Long id) {
        if (!repository.deletarPorId(id)) {
            throw new NoSuchElementException("Oportunidade não encontrada com ID: " + id);
        }
    }
}
