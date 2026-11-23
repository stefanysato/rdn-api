# Raízes do Nordeste API

API REST desenvolvida em Java com Spring Boot para gerenciamento de uma rede de restaurantes, como parte de um projeto acadêmico.

O sistema tem como objetivo centralizar e organizar as operações de diferentes unidades da rede, oferecendo recursos para gerenciamento de usuários, unidades, produtos, estoque, pedidos, pagamento e programa de fidelidade.

## Funcionalidades

O sistema contempla os seguintes módulos:

- **Autenticação e autorização:** login de usuários, geração e validação de tokens JWT e controle de acesso baseado em perfis/roles.
- **Gerenciamento de usuários:** cadastro, consulta, ativação e desativação de usuários.
- **Gerenciamento de unidades:** organização das unidades da rede e suas informações.
- **Gerenciamento de produtos:** cadastro e consulta dos produtos comercializados.
- **Controle de estoque:** acompanhamento das quantidades disponíveis e movimentações de estoque.
- **Gerenciamento de pedidos:** registro de pedidos, itens, valores e estados.
- **Pagamentos:** registro e acompanhamento das solicitações e resultados de pagamento.
- **Programa de fidelidade:** adesão ao programa de fidelidade, acúmulo e resgate de pontos.

## Tecnologias utilizadas

| Tecnologia | Finalidade |
|---|---|
| Java 21 | Linguagem de programação |
| Spring Boot 4.1.2 | Framework principal |
| Spring Web MVC | Construção dos endpoints REST |
| Spring Data JPA | Persistência e consultas ao banco |
| Spring Security | Autenticação e autorização |
| JWT | Autenticação baseada em tokens |
| BCrypt | Hash de senhas |
| PostgreSQL | Banco de dados relacional |
| Maven | Gerenciamento de dependências |
| Springdoc OpenAPI | Documentação da API |
| Swagger UI | Exploração e testes dos endpoints |

## Arquitetura do projeto

A aplicação utiliza uma arquitetura em camadas, separando as responsabilidades para facilitar a manutenção, organização e evolução do código.

```text
src/
└── main/
    ├── java/
    │   └── br/com/raizesdonordeste/api/
    │       ├── config/
    │       ├── controller/
    │       ├── dto/
    │       ├── exception/
    │       ├── model/
    │       ├── repository/
    │       ├── security/
    │       └── service/
    │
    └── resources/
        └── application.properties
```

### Responsabilidades das camadas

| Camada | Responsabilidade |
|---|---|
| `controller` | Receber requisições HTTP e disponibilizar os endpoints|
| `service` | Implementar as regras de negócio|
| `repository` | Realizar operações de persistência no banco de dados|
| `model` | Representar as entidades do sistema|
| `dto` | Definir os objetos utilizados na comunicação da API|
| `security` | Implementar os mecanismos de autenticação e segurança|
| `config` | Centralizar configurações da aplicação|
| `exception` | Tratar exceções e padronizar respostas de erro|

## Segurança

A API utiliza Spring Security e autenticação baseada em JSON Web Tokens (JWT).

Entre os mecanismos de segurança utilizados estão:

- Senhas armazenadas utilizando hash BCrypt.
- Autenticação por meio de tokens JWT.
- Controle de acesso baseado em perfis de usuário.
- Proteção de endpoints conforme as permissões necessárias.
- Bloqueio de usuários inativos durante a autenticação.
- Validação dos dados recebidos pela API.
- Tratamento padronizado de erros HTTP.

Para acessar endpoints protegidos, é necessário informar o token no cabeçalho da requisição:

```http
Authorization: Bearer SEU_TOKEN_JWT
```

## Como executar o projeto

### Pré-requisitos

Antes de executar a aplicação, é necessário ter instalado:

- Java JDK 21 ou superior compatível com o projeto.
- PostgreSQL.
- Maven (ou utilizar o Maven Wrapper disponibilizado pelo projeto).

### 1. Clonar o repositório

```bash
git clone https://github.com/stefanysato/rdn-api.git
```

Entre na pasta do projeto:

```bash
cd rdn-api
```

### 2. Configurar o banco de dados

Crie um banco de dados PostgreSQL para a aplicação.

Em seguida, configure as credenciais de conexão no arquivo `application.properties` ou por meio das variáveis de ambiente utilizadas pelo projeto.

Exemplo de configuração:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/rdn_api
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

### 3. Executar a aplicação

Utilizando o Maven Wrapper:

```bash
./mvnw spring-boot:run
```

Ou utilizando o Maven instalado:

```bash
mvn spring-boot:run
```

Após a inicialização, a API estará disponível em:

```text
http://localhost:8080
```

## Documentação da API

A documentação dos endpoints é disponibilizada por meio do Swagger UI, utilizando a especificação OpenAPI.

Com a aplicação em execução, acesse:

**Swagger UI:**

http://localhost:8080/swagger-ui.html

**Especificação OpenAPI:**

http://localhost:8080/v3/api-docs

Para testar endpoints protegidos:

1. Realize o login pelo endpoint de autenticação.
2. Copie o token JWT retornado.
3. Utilize a opção **Authorize** no Swagger UI.
4. Informe o token para autenticar as requisições.

## Considerações sobre o projeto

O Raízes do Nordeste API foi desenvolvido com foco no aprendizado e na aplicação prática de conceitos de desenvolvimento back-end, arquitetura de software, modelagem de dados, segurança e construção de APIs REST.

O projeto também considera aspectos relacionados à proteção de dados pessoais, incluindo princípios da Lei Geral de Proteção de Dados (LGPD), especialmente no contexto do programa de fidelidade.

## Links

- Repositório: https://github.com/stefanysato/rdn-api
- Coleção de testes (postman): `postman/RdnAPI.postman_collection.json`

---

*Projeto acadêmico - UNINTER, Projeto Multidisciplinar - Trilha Back-End (2026)*