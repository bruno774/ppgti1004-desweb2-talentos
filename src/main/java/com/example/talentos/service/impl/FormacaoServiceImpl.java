package com.example.talentos.service.impl;

import com.example.talentos.dto.FormacaoRequestDTO;
import com.example.talentos.dto.FormacaoResponseDTO;
import com.example.talentos.exception.RegraNegocioException;
import com.example.talentos.model.Formacao;
import com.example.talentos.model.Servidor;
import com.example.talentos.model.enums.NivelFormacao;
import com.example.talentos.repository.negocio.FormacaoJpaRepository;
import com.example.talentos.repository.negocio.ServidorJpaRepository;
import com.example.talentos.service.FormacaoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Implementação <strong>padrão</strong> de FormacaoService.
 */
@Service
@Qualifier("padrao")
@Transactional
public class FormacaoServiceImpl implements FormacaoService {

    private final FormacaoJpaRepository repository;
    private final ServidorJpaRepository servidorRepository;

    public FormacaoServiceImpl(FormacaoJpaRepository repository, ServidorJpaRepository servidorRepository) {
        this.repository = repository;
        this.servidorRepository = servidorRepository;
    }

    @Override
    public FormacaoResponseDTO criar(FormacaoRequestDTO dto) {
        // Regra de negócio: o servidor deve existir
        Servidor servidor = servidorRepository.findById(dto.getIdServidor())
                .orElseThrow(() -> new RegraNegocioException(
                        "Servidor com ID " + dto.getIdServidor() + " não encontrado. Formação não pode ser cadastrada."));

        // Regra de negócio: ano de conclusão não pode ser futuro
        int anoAtual = java.time.Year.now().getValue();
        if (dto.getAnoConclusao() > anoAtual) {
            throw new RegraNegocioException("Ano de conclusão não pode ser no futuro.");
        }

        Formacao formacao = Formacao.builder()
                .servidor(servidor)
                .instituicao(dto.getInstituicao())
                .curso(dto.getCurso())
                .nivel(dto.getNivel())
                .anoConclusao(dto.getAnoConclusao())
                .build();

        return FormacaoResponseDTO.de(repository.save(formacao));
    }

    @Override
    @Transactional(readOnly = true)
    public FormacaoResponseDTO buscarPorId(Long id) {
        return FormacaoResponseDTO.de(repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Formação não encontrada com ID: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FormacaoResponseDTO> buscarTodos() {
        return repository.findAll().stream()
                .map(FormacaoResponseDTO::de)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FormacaoResponseDTO> buscarPorNivel(NivelFormacao nivel) {
        return repository.findByNivel(nivel).stream()
                .map(FormacaoResponseDTO::de)
                .collect(Collectors.toList());
    }

    @Override
    public FormacaoResponseDTO atualizar(Long id, FormacaoRequestDTO dto) {
        Formacao existente = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Formação não encontrada com ID: " + id));

        int anoAtual = java.time.Year.now().getValue();
        if (dto.getAnoConclusao() > anoAtual) {
            throw new RegraNegocioException("Ano de conclusão não pode ser no futuro.");
        }

        existente.setServidor(servidorRepository.getReferenceById(dto.getIdServidor()));
        existente.setInstituicao(dto.getInstituicao());
        existente.setCurso(dto.getCurso());
        existente.setNivel(dto.getNivel());
        existente.setAnoConclusao(dto.getAnoConclusao());

        return FormacaoResponseDTO.de(repository.save(existente));
    }

    @Override
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Formação não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }
}
