# 🚗 Sistema de Gestão de Oficina

API REST desenvolvida para gerenciamento completo de oficinas mecânicas, permitindo controle de clientes, veículos, ordens de serviço e automatizações do sistema.
O projeto foi desenvolvido com foco em boas práticas de arquitetura, segurança, escalabilidade e preparação para ambiente produtivo.

---

# 📌 Funcionalidades do Sistema

**Gestão de Clientes**

- Cadastro de clientes (Pessoa Física ou Jurídica) com validação de CPF/CNPJ
- Registro completo de endereço vinculado ao cliente
- Atualização de dados cadastrais
- Ativação e desativação lógica (controle de status)
- Ativação e desativação lógica (controle de status
**Busca por:**
    - CPF/CNPJ
    - Nome
    - Status (ativo/inativo)
- Associação automática de clientes às Ordens de Serviço
- Histórico completo de OS vinculadas ao cliente

---
**Gestão de Veículos**

- Ativação e desativação lógica (controle de status
- Cadastro de veículos vinculados a um cliente
- Associação do veículo à Ordem de Serviço
- Controle de status (ativo/inativo)
**Busca por:**
    - Placa
    - Nome do responsável
    - Status
- Relacionamento Cliente → Veículo → OS
  
---
**Gestão de Ordens de Serviço (OS)**

**Criação e Estrutura**
- Abertura de OS vinculando:
    - Cliente (via CPF)
    - Veículo
    - Funcionário responsável

- Registro de:
    - Problema relatado
    - Quilometragem de entrada
    - Observações
    - Desconto inicial

- Composição da OS
    - Inclusão de serviços cadastrados
    - Inclusão de produtos/peças com quantidade
    - Remoção dinâmica de serviços e produtos
    - Atualização automática do estoque ao adicionar produtos

- Fluxo de Status
    - Aberta
    - Em execução
    - Aguardando peças
    - Finalizada
    - Cancelada
    - Finalização

- Registro de:
    - Quilometragem de saída
    - Diagnóstico final
    - Forma de pagamento
    - Parcelamento
    - Garantia
    - Desconto final

**Consultas**
- Listagem geral
- Listagem por status
- Listagem por funcionário
- Listagem por ID
- Preparação futura para dashboard analítico

---
**Gestão de Itens (Estoque)**

- Cadastro de peças/produtos com:
    - Código interno
    - Preço de compra e venda
    - Unidade de medida
    - Controle de estoque mínimo e máximo
- Atualização de dados do item
- Ativação e desativação lógica
  
- Buscar por:
    - Código
    - Nome
    - Status
      
- Integração direta com Ordens de Serviço
- Controle automático de estoque ao utilizar produtos na OS

---
**Gestão de Serviços**

- Cadastro de serviços com:
    - Descrição
    - Observação técnica
    - Valor de mão de obra
    - Tempo estimado
- Atualização de serviços
- Controle de status (ativo/inativo)
- Busca por descrição
- Associação direta às Ordens de Serviço

---
**Gestão de Funcionários**

- Cadastro completo com endereço
- Controle de cargo/função
- Ativação e desativação
- Busca por CPF e Nome
- Associação automática à abertura de OS
- Base para futura implementação de controle de permissões

---
**Gestão da Oficina**

- Consulta automática de dados via Receita Federal (CNPJ)
- Cadastro automatizado a partir da consulta
- Atualização de dados cadastrais
- Controle da oficina ativa no sistema

---
**Diferenciais do Sistema**

- Relacionamento estruturado entre Cliente → Veículo → OS
- Fluxo completo de ciclo de vida da Ordem de Serviço
- Controle de status com múltiplos estados operacionais
- Baixa automática de estoque ao utilizar produtos
- Ativação/desativação lógica para preservação histórica
- Preparação para dashboard gerencial
- Estrutura preparada para emissão futura de NF-e
- Sistema pronto para evolução analítica e escalável
  
---

# 🧱 Arquitetura do Projeto

Arquitetura em camadas:

- **API** → Estrutura e utilização para consumo de API
- **Configurações** → Configurações (Security, Swagger, WebClient, etc..)
- **Controller** → Camada de entrada (API REST)
- **DTO** → Transferência de dados
- **Entity** → Representação das tabelas
- **Enum** → Padronizações em retornos
- **Exception** → Tratamento global de erros
- **Mapper** → Para mapeamentos ágeis e escaláveis entre Entidade x DTO
- **Repository** → Acesso ao banco de dados
- **Service** → Regras de negócio

Padrões utilizados:

- RESTful API
- DTO Pattern
- Camadas bem definidas
- Inversão de Dependência
- Separação de responsabilidades

---

# 🛠️ Tecnologias Utilizadas

- Java 17
- Spring Boot
- PostgreSQL
- Docker
- Apache Kafka

# 📦 Principais Dependências

- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- Bean Validation
- MapStruct
- Lombok
- JUnit 5
- Mockito

# 📚 Documentações

- Swagger / OpenAPI
- JavaDoc
  
---

# 🚀 Diferenciais Técnicos

- Autenticação baseada em JWT com controle de roles
- Arquitetura orientada a eventos com Kafka
- Mapeamento desacoplado com MapStruct
- Validação robusta com Bean Validation
- Documentação interativa com Swagger
- Containerização completa com Docker
- Estrutura preparada para escalabilidade

---

# 🧪 Qualidade e Boas Práticas

- Separação clara de responsabilidades
- Tratamento global de exceções
- Padronização de respostas HTTP
- Testes unitários em regras de negócio
- Código limpo e organizado

---

# 🔐 Autenticação e Segurança

O sistema utiliza autenticação baseada em **JWT (Bearer Token)**.

Fluxo:

1. Usuário realiza login
2. Recebe um token JWT
3. Envia o token no header das requisições protegidas:

---

# Para saber mais

Caso você sinta interesse em saber mais sobre o projeto ou conversar sobre a temática, estou sempre disponível.
Você pode me encontra pelo email: **pedrohenriqueborba1@gmail.com**

Obrigado pela atenção.
