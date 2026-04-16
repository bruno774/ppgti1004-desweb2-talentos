package com.example.talentos.repository;

import com.example.talentos.model.Inscricao;
import com.example.talentos.model.enums.StatusInscricao;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Repositório em memória para Inscricao.
 * Suporta busca por StatusInscricao (categoria) e por servidor/oportunidade.
 */
@Repository
public class InscricaoRepository {

    private final Map<Long, Inscricao> armazenamento = new LinkedHashMap<>();
    private final AtomicLong sequencia = new AtomicLong(1);

    public Inscricao salvar(Inscricao inscricao) {
        if (inscricao.getId() == null) {
            inscricao.setId(sequencia.getAndIncrement());
        }
        armazenamento.put(inscricao.getId(), inscricao);
        return inscricao;
    }

    public Optional<Inscricao> buscarPorId(Long id) {
        return Optional.ofNullable(armazenamento.get(id));
    }

    public List<Inscricao> buscarTodos() {
        return new ArrayList<>(armazenamento.values());
    }

    /**
     * Busca inscrições por status (categoria).
     *
     * @param status o status desejado
     * @return lista de inscrições com aquele status
     */
    public List<Inscricao> buscarPorStatus(StatusInscricao status) {
        return armazenamento.values().stream()
                .filter(i -> i.getStatus() == status)
                .collect(Collectors.toList());
    }

    /** Retorna todas as inscrições de um servidor específico. */
    public List<Inscricao> buscarPorServidor(Long idServidor) {
        return armazenamento.values().stream()
                .filter(i -> Objects.equals(i.getIdServidor(), idServidor))
                .collect(Collectors.toList());
    }

    /** Verifica se já existe inscrição do servidor nesta oportunidade. */
    public boolean existeInscricao(Long idServidor, Long idOportunidade) {
        return armazenamento.values().stream()
                .anyMatch(i -> Objects.equals(i.getIdServidor(), idServidor)
                        && Objects.equals(i.getIdOportunidade(), idOportunidade));
    }

    public boolean deletarPorId(Long id) {
        return armazenamento.remove(id) != null;
    }
}
