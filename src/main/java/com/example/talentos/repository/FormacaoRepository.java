package com.example.talentos.repository;

import com.example.talentos.model.Formacao;
import com.example.talentos.model.enums.NivelFormacao;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Repositório em memória para Formacao.
 * Suporta busca por NivelFormacao (categoria) e por servidor.
 */
@Repository
public class FormacaoRepository {

    private final Map<Long, Formacao> armazenamento = new LinkedHashMap<>();
    private final AtomicLong sequencia = new AtomicLong(1);

    public Formacao salvar(Formacao formacao) {
        if (formacao.getId() == null) {
            formacao.setId(sequencia.getAndIncrement());
        }
        armazenamento.put(formacao.getId(), formacao);
        return formacao;
    }

    public Optional<Formacao> buscarPorId(Long id) {
        return Optional.ofNullable(armazenamento.get(id));
    }

    public List<Formacao> buscarTodos() {
        return new ArrayList<>(armazenamento.values());
    }

    /**
     * Busca formações por nível (categoria).
     *
     * @param nivel o nível de formação desejado
     * @return lista de formações que correspondem ao nível
     */
    public List<Formacao> buscarPorNivel(NivelFormacao nivel) {
        return armazenamento.values().stream()
                .filter(f -> f.getNivel() == nivel)
                .collect(Collectors.toList());
    }

    /** Retorna todas as formações de um servidor específico. */
    public List<Formacao> buscarPorServidor(Long idServidor) {
        return armazenamento.values().stream()
                .filter(f -> Objects.equals(f.getIdServidor(), idServidor))
                .collect(Collectors.toList());
    }

    public boolean deletarPorId(Long id) {
        return armazenamento.remove(id) != null;
    }
}
