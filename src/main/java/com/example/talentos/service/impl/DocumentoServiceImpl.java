package com.example.talentos.service.impl;

import com.example.talentos.dto.DocumentoRequestDTO;
import com.example.talentos.dto.DocumentoResponseDTO;
import com.example.talentos.exception.RegraNegocioException;
import com.example.talentos.model.Documento;
import com.example.talentos.model.Servidor;
import com.example.talentos.model.enums.TipoDocumento;
import com.example.talentos.repository.negocio.DocumentoJpaRepository;
import com.example.talentos.repository.negocio.ServidorJpaRepository;
import com.example.talentos.service.DocumentoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Implementação <strong>padrão</strong> de DocumentoService.
 */
@Service
@Qualifier("padrao")
@Transactional
public class DocumentoServiceImpl implements DocumentoService {

    private final DocumentoJpaRepository repository;
    private final ServidorJpaRepository servidorRepository;

    public DocumentoServiceImpl(DocumentoJpaRepository repository, ServidorJpaRepository servidorRepository) {
        this.repository = repository;
        this.servidorRepository = servidorRepository;
    }

    @Override
    public DocumentoResponseDTO criar(DocumentoRequestDTO dto) {
        // Regra de negócio: o servidor deve existir
        Servidor servidor = servidorRepository.findById(dto.getIdServidor())
                .orElseThrow(() -> new RegraNegocioException(
                        "Servidor com ID " + dto.getIdServidor() + " não encontrado."));

        Documento documento = Documento.builder()
                .servidor(servidor)
                .nome(dto.getNome())
                .tipo(dto.getTipo())
                .urlArquivo(dto.getUrlArquivo())
                .dataUpload(LocalDateTime.now())
                .build();

        return DocumentoResponseDTO.de(repository.save(documento));
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentoResponseDTO buscarPorId(Long id) {
        return DocumentoResponseDTO.de(repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Documento não encontrado com ID: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoResponseDTO> buscarTodos() {
        return repository.findAll().stream()
                .map(DocumentoResponseDTO::de)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoResponseDTO> buscarPorTipo(TipoDocumento tipo) {
        return repository.findByTipo(tipo).stream()
                .map(DocumentoResponseDTO::de)
                .collect(Collectors.toList());
    }

    @Override
    public DocumentoResponseDTO atualizar(Long id, DocumentoRequestDTO dto) {
        Documento existente = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Documento não encontrado com ID: " + id));

        existente.setServidor(servidorRepository.getReferenceById(dto.getIdServidor()));
        existente.setNome(dto.getNome());
        existente.setTipo(dto.getTipo());
        existente.setUrlArquivo(dto.getUrlArquivo());

        return DocumentoResponseDTO.de(repository.save(existente));
    }

    @Override
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Documento não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}
