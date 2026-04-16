package com.example.talentos.repository;

import com.example.talentos.model.Oportunidade;
import com.example.talentos.model.enums.AreaAtuacao;
import com.example.talentos.model.enums.StatusOportunidade;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Repositório em memória para Oportunidade.
 * Suporta busca por Status (categoria) e por AreaAtuacao.
 */
@Repository
public class OportunidadeRepository {

    private final Map<Long, Oportunidade> armazenamento = new LinkedHashMap<>();
    private final AtomicLong sequencia = new AtomicLong(1);

    public Oportunidade salvar(Oportunidade oportunidade) {
        if (oportunidade.getId() == null) {
            oportunidade.setId(sequencia.getAndIncrement());
        }
        armazenamento.put(oportunidade.getId(), oportunidade);
        return oportunidade;
    }

    public Optional<Oportunidade> buscarPorId(Long id) {
        return Optional.ofNullable(armazenamento.get(id));
    }

    public List<Oportunidade> buscarTodos() {
        return new ArrayList<>(armazenamento.values());
    }

    /**
     * Busca oportunidades por status (categoria principal).
     *
     * @param status o status desejado
     * @return lista de oportunidades com aquele status
     */
    public List<Oportunidade> buscarPorStatus(StatusOportunidade status) {
        return armazenamento.values().stream()
                .filter(o -> o.getStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * Busca oportunidades por área de atuação.
     *
     * @param area a área desejada
     * @return lista de oportunidades da área
     */
    public List<Oportunidade> buscarPorAreaAtuacao(AreaAtuacao area) {
        return armazenamento.values().stream()
                .filter(o -> o.getAreaAtuacao() == area)
                .collect(Collectors.toList());
    }

    public boolean deletarPorId(Long id) {
        return armazenamento.remove(id) != null;
    }
}
