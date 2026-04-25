package com.example.talentos.service.impl;

import com.example.talentos.dto.ServidorRequestDTO;
import com.example.talentos.dto.ServidorResponseDTO;
import com.example.talentos.exception.RegraNegocioException;
import com.example.talentos.model.Servidor;
import com.example.talentos.model.enums.AreaAtuacao;
import com.example.talentos.repository.negocio.ServidorJpaRepository;
import com.example.talentos.service.ServidorService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Implementação <strong>padrão</strong> de ServidorService.
 * Contém a lógica de negócio real sem overhead adicional.
 *
 * <p>Qualificador: {@code "padrao"} — injetado por padrão nos controllers.</p>
 */
@Service
@Qualifier("padrao")
@Transactional
public class ServidorServiceImpl implements ServidorService {

    private final ServidorJpaRepository repository;

    public ServidorServiceImpl(ServidorJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public ServidorResponseDTO criar(ServidorRequestDTO dto) {
        // Regra de negócio: CPF deve ter 11 dígitos
        String cpfDigitos = dto.getCpf().replaceAll("\\D", "");
        if (cpfDigitos.length() != 11) {
            throw new RegraNegocioException("CPF inválido: deve conter exatamente 11 dígitos.");
        }

        Servidor servidor = Servidor.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .email(dto.getEmail())
                .cargo(dto.getCargo())
                .areaAtuacao(dto.getAreaAtuacao())
                .ativo(dto.isAtivo())
                .build();

        return ServidorResponseDTO.de(repository.save(servidor));
    }

    @Override
    @Transactional(readOnly = true)
    public ServidorResponseDTO buscarPorId(Long id) {
        Servidor servidor = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Servidor não encontrado com ID: " + id));
        return ServidorResponseDTO.de(servidor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServidorResponseDTO> buscarTodos() {
        return repository.findAll().stream()
                .map(ServidorResponseDTO::de)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServidorResponseDTO> buscarPorAreaAtuacao(AreaAtuacao area) {
        return repository.findByAreaAtuacao(area).stream()
                .map(ServidorResponseDTO::de)
                .collect(Collectors.toList());
    }

    @Override
    public ServidorResponseDTO atualizar(Long id, ServidorRequestDTO dto) {
        Servidor existente = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Servidor não encontrado com ID: " + id));

        String cpfDigitos = dto.getCpf().replaceAll("\\D", "");
        if (cpfDigitos.length() != 11) {
            throw new RegraNegocioException("CPF inválido: deve conter exatamente 11 dígitos.");
        }

        existente.setNome(dto.getNome());
        existente.setCpf(dto.getCpf());
        existente.setEmail(dto.getEmail());
        existente.setCargo(dto.getCargo());
        existente.setAreaAtuacao(dto.getAreaAtuacao());
        existente.setAtivo(dto.isAtivo());

        return ServidorResponseDTO.de(repository.save(existente));
    }

    @Override
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Servidor não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}
