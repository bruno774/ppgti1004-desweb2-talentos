package com.example.talentos.service.impl;

import com.example.talentos.dto.DocumentoRequestDTO;
import com.example.talentos.dto.DocumentoResponseDTO;
import com.example.talentos.exception.RegraNegocioException;
import com.example.talentos.model.Documento;
import com.example.talentos.model.enums.TipoDocumento;
import com.example.talentos.repository.DocumentoRepository;
import com.example.talentos.repository.ServidorRepository;
import com.example.talentos.service.DocumentoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Implementação <strong>padrão</strong> de DocumentoService.
 */
@Service
@Qualifier("padrao")
public class DocumentoServiceImpl implements DocumentoService {

    private final DocumentoRepository repository;
    private final ServidorRepository servidorRepository;

    public DocumentoServiceImpl(DocumentoRepository repository, ServidorRepository servidorRepository) {
        this.repository = repository;
        this.servidorRepository = servidorRepository;
    }

    @Override
    public DocumentoResponseDTO criar(DocumentoRequestDTO dto) {
        // Regra de negócio: o servidor deve existir
        servidorRepository.buscarPorId(dto.getIdServidor())
                .orElseThrow(() -> new RegraNegocioException(
                        "Servidor com ID " + dto.getIdServidor() + " não encontrado."));

        Documento documento = Documento.builder()
                .idServidor(dto.getIdServidor())
                .nome(dto.getNome())
                .tipo(dto.getTipo())
                .urlArquivo(dto.getUrlArquivo())
                .dataUpload(LocalDateTime.now())
                .build();

        return DocumentoResponseDTO.de(repository.salvar(documento));
    }

    @Override
    public DocumentoResponseDTO buscarPorId(Long id) {
        return DocumentoResponseDTO.de(repository.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Documento não encontrado com ID: " + id)));
    }

    @Override
    public List<DocumentoResponseDTO> buscarTodos() {
        return repository.buscarTodos().stream()
                .map(DocumentoResponseDTO::de)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentoResponseDTO> buscarPorTipo(TipoDocumento tipo) {
        return repository.buscarPorTipo(tipo).stream()
                .map(DocumentoResponseDTO::de)
                .collect(Collectors.toList());
    }

    @Override
    public DocumentoResponseDTO atualizar(Long id, DocumentoRequestDTO dto) {
        Documento existente = repository.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Documento não encontrado com ID: " + id));

        existente.setIdServidor(dto.getIdServidor());
        existente.setNome(dto.getNome());
        existente.setTipo(dto.getTipo());
        existente.setUrlArquivo(dto.getUrlArquivo());

        return DocumentoResponseDTO.de(repository.salvar(existente));
    }

    @Override
    public void deletar(Long id) {
        if (!repository.deletarPorId(id)) {
            throw new NoSuchElementException("Documento não encontrado com ID: " + id);
        }
    }
}
