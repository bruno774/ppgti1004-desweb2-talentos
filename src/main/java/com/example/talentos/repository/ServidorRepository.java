package com.example.talentos.repository;

import com.example.talentos.model.Servidor;
import com.example.talentos.model.enums.AreaAtuacao;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Repositório em memória para Servidor.
 * Utiliza um Map para acesso rápido por ID e suporta busca por AreaAtuacao.
 */
@Repository
public class ServidorRepository {

    private final Map<Long, Servidor> armazenamento = new LinkedHashMap<>();
    private final AtomicLong sequencia = new AtomicLong(1);

    /** Persiste ou atualiza um servidor. Se id==null, gera novo ID. */
    public Servidor salvar(Servidor servidor) {
        if (servidor.getId() == null) {
            servidor.setId(sequencia.getAndIncrement());
        }
        armazenamento.put(servidor.getId(), servidor);
        return servidor;
    }

    /** Retorna o servidor pelo ID, ou Optional.empty() se não existir. */
    public Optional<Servidor> buscarPorId(Long id) {
        return Optional.ofNullable(armazenamento.get(id));
    }

    /** Retorna todos os servidores cadastrados. */
    public List<Servidor> buscarTodos() {
        return new ArrayList<>(armazenamento.values());
    }

    /**
     * Busca servidores por área de atuação (filtro de categoria).
     *
     * @param area a área de atuação desejada
     * @return lista de servidores que correspondem à área
     */
    public List<Servidor> buscarPorAreaAtuacao(AreaAtuacao area) {
        return armazenamento.values().stream()
                .filter(s -> s.getAreaAtuacao() == area)
                .collect(Collectors.toList());
    }

    /** Remove o servidor pelo ID. Retorna true se existia. */
    public boolean deletarPorId(Long id) {
        return armazenamento.remove(id) != null;
    }
}
