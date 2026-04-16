package com.example.talentos.config;

import com.example.talentos.service.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Componente de configuração e demonstração do uso de {@link ApplicationContext}.
 *
 * <p><strong>Exercitando baixo acoplamento e ApplicationContext:</strong></p>
 * <ol>
 *   <li>Interfaces de serviço (ex: {@link ServidorService}) definem o contrato.</li>
 *   <li>Duas implementações por interface: {@code @Qualifier("padrao")} e
 *       {@code @Qualifier("auditoria")}.</li>
 *   <li>Os controllers injetam a implementação desejada via
 *       {@code @Autowired @Qualifier("padrao")}.</li>
 *   <li>Este componente usa o {@link ApplicationContext} em tempo de inicialização
 *       para listar as implementações disponíveis e logar qual está ativa —
 *       demonstrando uso programático do contexto.</li>
 * </ol>
 *
 * <p>Para trocar a implementação ativa globalmente, altere em
 * {@code application.properties}:
 * <pre>
 *   app.servico.implementacao=auditoria
 * </pre>
 * E substitua todos os {@code @Qualifier("padrao")} nos controllers por
 * {@code @Qualifier("auditoria")} — ou use a lógica abaixo como referência
 * para injeção programática via ApplicationContext.
 * </p>
 */
@Component
public class AppConfig {

    private final ApplicationContext ctx;

    @Value("${app.servico.implementacao:padrao}")
    private String implementacaoAtiva;

    public AppConfig(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Executado após a inicialização do contexto Spring.
     * Usa {@link ApplicationContext} para listar todas as implementações
     * de {@link ServidorService} e loga qual está configurada como ativa,
     * demonstrando inspeção programática do container IoC.
     */
    @PostConstruct
    public void reportarImplementacoes() {
        System.out.println("\n=== [AppConfig] Implementação de serviço configurada: '" + implementacaoAtiva + "' ===");
        System.out.println("=== [AppConfig] Beans de ServidorService disponíveis no ApplicationContext: ===");

        Map<String, ServidorService> beansServidor = ctx.getBeansOfType(ServidorService.class);
        beansServidor.forEach((nome, bean) -> {
            Qualifier q = bean.getClass().getAnnotation(Qualifier.class);
            String qualificador = (q != null) ? q.value() : "sem qualificador";
            boolean ativo = qualificador.equals(implementacaoAtiva);
            System.out.printf("   Bean: %-45s | @Qualifier: %-12s | %s%n",
                    nome, qualificador, ativo ? "<<< ATIVO >>>" : "");
        });
        System.out.println();
    }

    /**
     * Método utilitário público: retorna a implementação de qualquer serviço
     * que possua {@code @Qualifier} igual ao valor de
     * {@code app.servico.implementacao}. Permite injeção programática via
     * ApplicationContext sem depender de nome hardcoded de bean.
     *
     * @param tipo Classe da interface de serviço
     * @param <T>  tipo da interface
     * @return implementação correspondente ao qualificador ativo
     */
    public <T> T obterServico(Class<T> tipo) {
        Map<String, T> candidatos = ctx.getBeansOfType(tipo);
        for (T candidato : candidatos.values()) {
            Qualifier q = candidato.getClass().getAnnotation(Qualifier.class);
            if (q != null && q.value().equals(implementacaoAtiva)) {
                return candidato;
            }
        }
        throw new IllegalStateException(
                "Nenhuma implementação de " + tipo.getSimpleName() +
                " encontrada com @Qualifier(\"" + implementacaoAtiva + "\").");
    }
}
