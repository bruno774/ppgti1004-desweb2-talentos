package com.example.talentos.service.impl;

import com.example.talentos.dto.InscricaoRequestDTO;
import com.example.talentos.dto.InscricaoResponseDTO;
import com.example.talentos.exception.RegraNegocioException;
import com.example.talentos.model.Inscricao;
import com.example.talentos.model.Oportunidade;
import com.example.talentos.model.Servidor;
import com.example.talentos.model.enums.StatusInscricao;
import com.example.talentos.model.enums.StatusOportunidade;
import com.example.talentos.repository.negocio.InscricaoJpaRepository;
import com.example.talentos.repository.negocio.OportunidadeJpaRepository;
import com.example.talentos.repository.negocio.ServidorJpaRepository;
import com.example.talentos.service.InscricaoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Implementação <strong>padrão</strong> de InscricaoService.
 */
@Service
@Qualifier("padrao")
@Transactional
public class InscricaoServiceImpl implements InscricaoService {

    private final InscricaoJpaRepository repository;
    private final ServidorJpaRepository servidorRepository;
    private final OportunidadeJpaRepository oportunidadeRepository;

    public InscricaoServiceImpl(InscricaoJpaRepository repository,
                                ServidorJpaRepository servidorRepository,
                                OportunidadeJpaRepository oportunidadeRepository) {
        this.repository = repository;
        this.servidorRepository = servidorRepository;
        this.oportunidadeRepository = oportunidadeRepository;
    }

    @Override
    public InscricaoResponseDTO criar(InscricaoRequestDTO dto) {
        // Regra de negócio: servidor deve existir e estar ativo
        Servidor servidor = servidorRepository.findById(dto.getIdServidor())
                .orElseThrow(() -> new RegraNegocioException(
                        "Servidor com ID " + dto.getIdServidor() + " não encontrado."));
        if (!servidor.isAtivo()) {
            throw new RegraNegocioException("Servidor inativo não pode se inscrever em oportunidades.");
        }

        // Regra de negócio: oportunidade deve existir e estar aberta
        Oportunidade oportunidade = oportunidadeRepository.findById(dto.getIdOportunidade())
                .orElseThrow(() -> new RegraNegocioException(
                        "Oportunidade com ID " + dto.getIdOportunidade() + " não encontrada."));
        if (oportunidade.getStatus() != StatusOportunidade.ABERTA) {
            throw new RegraNegocioException("Só é possível se inscrever em oportunidades com status ABERTA.");
        }

        // Regra de negócio: não permitir inscrição duplicada
        if (repository.existsByServidorIdAndOportunidadeId(dto.getIdServidor(), dto.getIdOportunidade())) {
            throw new RegraNegocioException("Servidor já está inscrito nesta oportunidade.");
        }

        Inscricao inscricao = Inscricao.builder()
                .servidor(servidor)
                .oportunidade(oportunidade)
                .dataInscricao(LocalDate.now())
                .status(StatusInscricao.PENDENTE)
                .build();

        return InscricaoResponseDTO.de(repository.save(inscricao));
    }

    @Override
    @Transactional(readOnly = true)
    public InscricaoResponseDTO buscarPorId(Long id) {
        return InscricaoResponseDTO.de(repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Inscrição não encontrada com ID: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscricaoResponseDTO> buscarTodos() {
        return repository.findAll().stream()
                .map(InscricaoResponseDTO::de)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscricaoResponseDTO> buscarPorStatus(StatusInscricao status) {
        return repository.findByStatus(status).stream()
                .map(InscricaoResponseDTO::de)
                .collect(Collectors.toList());
    }

    @Override
    public InscricaoResponseDTO atualizar(Long id, InscricaoRequestDTO dto) {
        Inscricao existente = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Inscrição não encontrada com ID: " + id));

        // Carrega as entidades referenciadas para manter a integridade dos relacionamentos
        Servidor servidor = servidorRepository.getReferenceById(dto.getIdServidor());
        Oportunidade oportunidade = oportunidadeRepository.getReferenceById(dto.getIdOportunidade());

        existente.setServidor(servidor);
        existente.setOportunidade(oportunidade);

        return InscricaoResponseDTO.de(repository.save(existente));
    }

    @Override
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Inscrição não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }
}
