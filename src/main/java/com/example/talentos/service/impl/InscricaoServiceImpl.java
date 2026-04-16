package com.example.talentos.service.impl;

import com.example.talentos.dto.InscricaoRequestDTO;
import com.example.talentos.dto.InscricaoResponseDTO;
import com.example.talentos.exception.RegraNegocioException;
import com.example.talentos.model.Inscricao;
import com.example.talentos.model.enums.StatusInscricao;
import com.example.talentos.model.enums.StatusOportunidade;
import com.example.talentos.repository.InscricaoRepository;
import com.example.talentos.repository.OportunidadeRepository;
import com.example.talentos.repository.ServidorRepository;
import com.example.talentos.service.InscricaoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Implementação <strong>padrão</strong> de InscricaoService.
 */
@Service
@Qualifier("padrao")
public class InscricaoServiceImpl implements InscricaoService {

    private final InscricaoRepository repository;
    private final ServidorRepository servidorRepository;
    private final OportunidadeRepository oportunidadeRepository;

    public InscricaoServiceImpl(InscricaoRepository repository,
                                ServidorRepository servidorRepository,
                                OportunidadeRepository oportunidadeRepository) {
        this.repository = repository;
        this.servidorRepository = servidorRepository;
        this.oportunidadeRepository = oportunidadeRepository;
    }

    @Override
    public InscricaoResponseDTO criar(InscricaoRequestDTO dto) {
        // Regra de negócio: servidor deve existir e estar ativo
        var servidor = servidorRepository.buscarPorId(dto.getIdServidor())
                .orElseThrow(() -> new RegraNegocioException(
                        "Servidor com ID " + dto.getIdServidor() + " não encontrado."));
        if (!servidor.isAtivo()) {
            throw new RegraNegocioException("Servidor inativo não pode se inscrever em oportunidades.");
        }

        // Regra de negócio: oportunidade deve existir e estar aberta
        var oportunidade = oportunidadeRepository.buscarPorId(dto.getIdOportunidade())
                .orElseThrow(() -> new RegraNegocioException(
                        "Oportunidade com ID " + dto.getIdOportunidade() + " não encontrada."));
        if (oportunidade.getStatus() != StatusOportunidade.ABERTA) {
            throw new RegraNegocioException("Só é possível se inscrever em oportunidades com status ABERTA.");
        }

        // Regra de negócio: não permitir inscrição duplicada
        if (repository.existeInscricao(dto.getIdServidor(), dto.getIdOportunidade())) {
            throw new RegraNegocioException("Servidor já está inscrito nesta oportunidade.");
        }

        Inscricao inscricao = Inscricao.builder()
                .idServidor(dto.getIdServidor())
                .idOportunidade(dto.getIdOportunidade())
                .dataInscricao(LocalDate.now())
                .status(StatusInscricao.PENDENTE)
                .build();

        return InscricaoResponseDTO.de(repository.salvar(inscricao));
    }

    @Override
    public InscricaoResponseDTO buscarPorId(Long id) {
        return InscricaoResponseDTO.de(repository.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Inscrição não encontrada com ID: " + id)));
    }

    @Override
    public List<InscricaoResponseDTO> buscarTodos() {
        return repository.buscarTodos().stream()
                .map(InscricaoResponseDTO::de)
                .collect(Collectors.toList());
    }

    @Override
    public List<InscricaoResponseDTO> buscarPorStatus(StatusInscricao status) {
        return repository.buscarPorStatus(status).stream()
                .map(InscricaoResponseDTO::de)
                .collect(Collectors.toList());
    }

    @Override
    public InscricaoResponseDTO atualizar(Long id, InscricaoRequestDTO dto) {
        Inscricao existente = repository.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Inscrição não encontrada com ID: " + id));

        existente.setIdServidor(dto.getIdServidor());
        existente.setIdOportunidade(dto.getIdOportunidade());

        return InscricaoResponseDTO.de(repository.salvar(existente));
    }

    @Override
    public void deletar(Long id) {
        if (!repository.deletarPorId(id)) {
            throw new NoSuchElementException("Inscrição não encontrada com ID: " + id);
        }
    }
}
