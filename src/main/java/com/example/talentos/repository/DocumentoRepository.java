package com.example.talentos.repository;

import com.example.talentos.model.Documento;
import com.example.talentos.model.enums.TipoDocumento;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Repositório em memória para Documento.
 * Suporta busca por TipoDocumento (categoria) e por servidor.
 */
@Repository
public class DocumentoRepository {

    private final Map<Long, Documento> armazenamento = new LinkedHashMap<>();
    private final AtomicLong sequencia = new AtomicLong(1);

    public Documento salvar(Documento documento) {
        if (documento.getId() == null) {
            documento.setId(sequencia.getAndIncrement());
        }
        armazenamento.put(documento.getId(), documento);
        return documento;
    }

    public Optional<Documento> buscarPorId(Long id) {
        return Optional.ofNullable(armazenamento.get(id));
    }

    public List<Documento> buscarTodos() {
        return new ArrayList<>(armazenamento.values());
    }

    /**
     * Busca documentos por tipo (categoria).
     *
     * @param tipo o tipo de documento
     * @return lista de documentos daquele tipo
     */
    public List<Documento> buscarPorTipo(TipoDocumento tipo) {
        return armazenamento.values().stream()
                .filter(d -> d.getTipo() == tipo)
                .collect(Collectors.toList());
    }

    /** Retorna todos os documentos de um servidor específico. */
    public List<Documento> buscarPorServidor(Long idServidor) {
        return armazenamento.values().stream()
                .filter(d -> Objects.equals(d.getIdServidor(), idServidor))
                .collect(Collectors.toList());
    }

    public boolean deletarPorId(Long id) {
        return armazenamento.remove(id) != null;
    }
}
