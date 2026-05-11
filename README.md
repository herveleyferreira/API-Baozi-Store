# Baozi Store API 🥟

API REST para gerenciamento de clientes, produtos e pedidos da **Baozi Store** — a loja de pãozinho chinês.

---

## Tecnologias

- Java 17
- Spring Boot 3.2.5
- Spring Data JPA
- Banco H2 (em memória)
- Bean Validation

---

## Como Executar

```bash
# Na raiz do projeto:
./mvnw spring-boot:run

# Ou com Maven instalado:
mvn spring-boot:run
```

A API sobe em: `http://localhost:8080`
Console H2:    `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:baozidb`
- User: `sa` | Senha: (vazia)

---

## Estrutura do Projeto

```
src/main/java/com/baozi/store/
├── BaoziStoreApplication.java   ← Classe principal
├── model/
│   ├── Produto.java             ← Entidade Produto
│   ├── Cliente.java             ← Entidade Cliente
│   └── Pedido.java              ← Entidade Pedido
├── repository/
│   ├── ProdutoRepository.java
│   ├── ClienteRepository.java
│   └── PedidoRepository.java
├── controller/
│   ├── ProdutoController.java
│   ├── ClienteController.java
│   └── PedidoController.java
└── exception/
    ├── ResourceNotFoundException.java
    └── GlobalExceptionHandler.java
```

---

## Endpoints da API

### Produto — `/produtos`

| Método | URL            | Descrição            | Status |
|--------|----------------|----------------------|--------|
| POST   | /produtos      | Cadastrar produto    | 201    |
| GET    | /produtos      | Listar todos         | 200    |
| GET    | /produtos/{id} | Buscar por ID        | 200    |
| PUT    | /produtos/{id} | Atualizar produto    | 200    |
| DELETE | /produtos/{id} | Remover produto      | 204    |

**Corpo POST/PUT:**
```json
{
  "nome": "Baozi de Carne de Porco",
  "preco": 5.50,
  "estoque": true
}
```

---

### Cliente — `/clientes`

| Método | URL             | Descrição           | Status |
|--------|-----------------|---------------------|--------|
| POST   | /clientes       | Cadastrar cliente   | 201    |
| GET    | /clientes       | Listar todos        | 200    |
| GET    | /clientes/{id}  | Buscar por ID       | 200    |
| PUT    | /clientes/{id}  | Atualizar cliente   | 200    |
| DELETE | /clientes/{id}  | Remover cliente     | 204    |

**Corpo POST/PUT:**
```json
{
  "nome": "Maria Silva",
  "clienteDesde": "2024-01-15"
}
```

---

### Pedido — `/pedidos`

| Método | URL            | Descrição          | Status |
|--------|----------------|--------------------|--------|
| POST   | /pedidos       | Registrar pedido   | 201    |
| GET    | /pedidos       | Listar todos       | 200    |
| GET    | /pedidos/{id}  | Buscar por ID      | 200    |
| PUT    | /pedidos/{id}  | Atualizar pedido   | 200    |
| DELETE | /pedidos/{id}  | Cancelar pedido    | 204    |

**Corpo POST/PUT:**
```json
{
  "clienteId": 1,
  "produtoId": 1,
  "quantidade": 3
}
```

---

## Regras de Negócio

- Um pedido sempre precisa de um cliente e um produto válidos (existentes no banco).
- Não é possível registrar pedido de produto fora de estoque (`estoque: false`).
- Quantidade mínima de 1 por pedido.
- Todos os erros retornam JSON padronizado com `timestamp`, `status` e `mensagem`.

---

## Dados Iniciais (data.sql)

Ao subir a aplicação, o banco é populado automaticamente com:
- 3 produtos (baozi de carne, frango e vegetariano)
- 3 clientes de exemplo

---

## Exemplos de Teste no Postman

### 1. Criar produto
`POST http://localhost:8080/produtos`
```json
{
  "nome": "Baozi Especial",
  "preco": 7.00,
  "estoque": true
}
```

### 2. Criar cliente
`POST http://localhost:8080/clientes`
```json
{
  "nome": "Carlos Mendes",
  "clienteDesde": "2025-05-10"
}
```

### 3. Registrar pedido
`POST http://localhost:8080/pedidos`
```json
{
  "clienteId": 1,
  "produtoId": 1,
  "quantidade": 5
}
```

### 4. Listar pedidos
`GET http://localhost:8080/pedidos`

### 5. Deletar cliente
`DELETE http://localhost:8080/clientes/1`
