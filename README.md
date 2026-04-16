# Talentos — Sistema de Gestão de Talentos Institucionais

> Plataforma de cadastro e relacionamento de servidores, habilidades, capacitações e oportunidades internas de uma instituição pública.

---

## Sobre o Projeto

O **Talentos** é uma API REST desenvolvida em **Spring Boot** para apoiar a gestão estratégica de pessoas em instituições públicas. O sistema centraliza o cadastro de servidores e vincula, de forma estruturada, suas formações acadêmicas, documentos comprobatórios e inscrições em oportunidades internas abertas pela própria instituição.

A solução nasce da necessidade de superar o uso de planilhas e processos manuais dispersos, oferecendo um ponto único e confiável de consulta de talentos disponíveis para iniciativas como:

- Composição de **comissões** e grupos de trabalho
- Indicação para **cargos de confiança** e funções gratificadas
- Participação em **projetos institucionais**
- Inscrição em **cursos de capacitação** e programas de formação internos
- Contribuição em **iniciativas administrativas** e de governança

---

## Justificativa da Solução

### O Problema

Instituições públicas frequentemente enfrentam dificuldades em identificar, de forma ágil e transparente, quais servidores possuem o perfil adequado para uma determinada oportunidade. O processo costuma ser:

- **Descentralizado** — informações fragmentadas em diferentes setores e sistemas legados
- **Manual** — dependente de contatos pessoais e memória organizacional
- **Ineficiente** — sem critérios objetivos de busca por área, formação ou disponibilidade
- **Pouco transparente** — sem registro formal de inscrições e histórico de movimentações

### A Solução Escolhida

Optou-se por uma **API REST com persistência em memória** como ponto de partida, seguindo os princípios de uma arquitetura limpa e evolutiva, com as seguintes justificativas técnicas e estratégicas:

| Decisão | Justificativa |
|---|---|
| **API REST** | Padrão amplamente adotado, agnóstico de frontend; permite integração futura com portais web, sistemas legados ou aplicativos mobile |
| **Spring Boot** | Framework maduro, com ecossistema robusto de validação, injeção de dependências e tratamento de erros — reduz tempo de desenvolvimento sem abrir mão de boas práticas |
| **Persistência em memória** | Fase inicial de prova de conceito: permite validar o modelo de dados e as regras de negócio sem custo de infraestrutura de banco de dados |
| **Separação Model / DTO** | Garante que dados sensíveis (ex: CPF completo) nunca sejam expostos pela API; permite evoluir a representação pública sem quebrar o modelo interno |
| **Enums para categorias** | Categorias como área de atuação, nível de formação e status de inscrição são controladas, evitando inconsistências por digitação livre |
| **Validação na entrada** | Dados inválidos são rejeitados antes de qualquer processamento, reduzindo bugs e garantindo integridade das informações |
| **Baixo acoplamento via interfaces** | Facilita substituição de implementações (ex: trocar mock por banco de dados real) sem impacto nos controllers — preparado para crescer |

---

## Entidades do Domínio

```
Servidor ──────────────── Formacao
    │                    (nível acadêmico, instituição, curso)
    │
    ├──────────────────── Documento
    │                    (CPF, diploma, certificado, laudo)
    │
    └──────────────────── Inscricao ──── Oportunidade
                         (status: PENDENTE,          (ABERTA, EM_ANALISE,
                          EM_ANALISE, APROVADA...)    ENCERRADA, CANCELADA)
```

| Entidade | Finalidade |
|---|---|
| **Servidor** | Cadastro central do servidor: dados funcionais, área e status ativo |
| **Formação** | Histórico acadêmico vinculado ao servidor (graduação, especialização, etc.) |
| **Documento** | Comprovantes e anexos digitais do servidor (diploma, RG, laudos) |
| **Oportunidade** | Vagas, comissões, projetos ou cursos publicados pela instituição |
| **Inscrição** | Relaciona servidor e oportunidade, com rastreamento de status e data |

---

## Tecnologias Utilizadas

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 17 | Linguagem principal |
| Spring Boot | 4.0.5 | Framework web e de injeção de dependências |
| Spring Web MVC | — | Camada de controllers REST |
| Spring Validation | — | Validação declarativa com Bean Validation (Jakarta) |
| Lombok | — | Redução de boilerplate (getters, setters, builders) |
| SLF4J | — | Logging na implementação de auditoria |

---

## Arquitetura

```
com.example.talentos
├── config/         AppConfig — seleção dinâmica de implementação via ApplicationContext
├── controller/     5 controllers REST (GET, POST, PUT, DELETE + filtros)
├── dto/            Request DTOs (validação) e Response DTOs (dados ao cliente)
├── exception/      RegraNegocioException + GlobalExceptionHandler (@RestControllerAdvice)
├── model/
│   ├── enums/      AreaAtuacao · NivelFormacao · StatusOportunidade · StatusInscricao · TipoDocumento
│   └──             Servidor · Formacao · Oportunidade · Documento · Inscricao
├── repository/     Repositórios em memória (Map<Long, T> + AtomicLong)
└── service/
    ├──             Interfaces: ServidorService · FormacaoService · OportunidadeService ...
    └── impl/       Impl padrão (@Qualifier "padrao") + Impl auditoria (@Qualifier "auditoria")
```

### Padrões e Boas Práticas Aplicados

- **Separação de camadas** — Model → Repository → Service → Controller, sem dependências invertidas
- **DTO Pattern** — entidades internas nunca trafegam na API
- **Strategy/Decorator com @Qualifier** — duas implementações de serviço intercambiáveis sem alterar controllers
- **Tratamento global de erros** — respostas JSON padronizadas para 400, 404 e 500
- **Regras de negócio centralizadas** — todas as validações semânticas ficam na camada de serviço

---

## Endpoints Disponíveis

Cada recurso expõe o conjunto completo de operações REST:

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/servidores` | Lista todos os servidores |
| `GET` | `/servidores/{id}` | Busca servidor por ID |
| `GET` | `/servidores/categoria?area=TECNOLOGIA` | Filtra por área de atuação |
| `POST` | `/servidores` | Cadastra novo servidor |
| `PUT` | `/servidores/{id}` | Atualiza dados do servidor |
| `DELETE` | `/servidores/{id}` | Remove servidor |

> O mesmo padrão se aplica a `/formacoes`, `/oportunidades`, `/documentos` e `/inscricoes`.  
> Oportunidades possuem filtro adicional: `GET /oportunidades/area?area=EDUCACAO`.

---

## Como Executar

**Pré-requisito:** Java 17+ e Maven instalados.

```bash
# Clonar o repositório
git clone <url-do-repositório>
cd talentos

# Iniciar a aplicação
./mvnw spring-boot:run
```

A API estará disponível em: **`http://localhost:8080`**

### Troca de Implementação de Serviço

Para ativar o modo de **auditoria** (logging detalhado de todas as operações), altere `application.properties`:

```properties
# Opções: padrao | auditoria
app.servico.implementacao=auditoria
```

---

## Testes

O arquivo `testesTalentos.json` na raiz do projeto contém uma **collection Postman** com 55 requisições cobrindo:

- Cenários de sucesso (201, 200, 204)
- Erros de validação (400 com mapa de campos)
- Violações de regra de negócio (400 com mensagem clara)
- Recursos não encontrados (404)

---

## Próximos Passos (Roadmap)

- [ ] Persistência em banco de dados relacional (PostgreSQL via Spring Data JPA)
- [ ] Autenticação e controle de acesso por perfil (Spring Security + JWT)
- [ ] Módulo de habilidades e competências do servidor
- [ ] Notificações automáticas ao servidor sobre status da inscrição
- [ ] Painel administrativo para gestão de oportunidades
- [ ] Documentação interativa com Swagger / OpenAPI 3
