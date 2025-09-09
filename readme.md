# To Do List API

![Java](https://img.shields.io/badge/Java-11+-red?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7-green?logo=springboot)
![Maven](https://img.shields.io/badge/Maven-3.6+-orange?logo=apachemaven)
![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)

## 📌 Descrição
To Do List é uma **API REST** desenvolvida em **Java com Spring Boot** para organizar tarefas de forma simples e rápida.  
Permite **criar, listar e atualizar** itens de afazeres usando um banco de dados em memória **H2**.

---

## 🚀 Tecnologias e Dependências
- Java 11 ou superior
- Spring Boot
- Lombok
- H2 Database
- Spring Security com BCrypt
- Maven

---

## 🔧 Pré-requisitos
- JDK 11 ou superior
- Maven 3.6+
- Git (opcional, para clonar o repositório)

---

## ⚙️ Instalação e Execução

### 1. Clone o repositório
```bash
git clone https://github.com/LucasBXavier/ToDoList.git
cd ToDoList
```

### 2. Compile e instale as dependências
```bash
mvn clean install
```

### 3. Execute a aplicação
```bash
mvn spring-boot:run
```

A API ficará disponível em:  
👉 [http://localhost:8080](http://localhost:8080)

---

## 📍 Endpoints
**Base URL:** `http://localhost:8080/tasks/`

| Método | Endpoint          | Descrição                |
|--------|-------------------|--------------------------|
| GET    | `/tasks/`         | Lista todas as tarefas   |
| POST   | `/tasks/`      | Cria nova tarefa         |
| PUT    | `/tasks/{id}` | Atualiza tarefa existente|

---

**Base URL:** `http://localhost:8080/users`

| Método | Endpoint | Descrição                |
|--------|----------|--------------------------|
| GET    | `/users` | Lista todas as tarefas   |
| POST   | `/users` | Cria nova tarefa         |

---

## 📌 Exemplos de Uso

### ➕ Criar tarefa
```bash
curl -X POST http://localhost:8080/tasks/   -H "Content-Type: application/json"   -d 
'{
  "description": "Revisar o conteúdo de Spring Boot",
  "title": "Revisão Spring",
  "startAt": "2025-06-01T09:00:00",
  "endAt": "2025-06-01T11:00:00",
  "priority": "ALTA",
  "idUser": "" (pegar o id do usuário criado no endpoint de usuários)
}'
```

### 📋 Listar tarefas
```bash
curl http://localhost:8080/tasks/
```

### ✏️ Atualizar tarefa
```bash
curl -X PUT http://localhost:8080/api/tasks/1   -H "Content-Type: application/json"   -d 
'{
  "description":"se aprofundando em angular e spring"
  "title":"revisão de angular com spring",
}'
```

---

### ➕ Criar usuario
```bash
curl -X POST http://localhost:8080/users   -H "Content-Type: application/json"   -d 
'{
    "userName": "JohnDoe",
    "name": "john doe",
    "password": "123456"
}'
```

### 📋 Listar usuarios
```bash
curl http://localhost:8080/users
```

---


## 🗄️ Banco de Dados em Memória
A aplicação usa **H2 em modo in-memory**, sem necessidade de configuração adicional.

Para acessar a console H2, abra:  
👉 [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

Configurações:
- **Driver JDBC:** `org.h2.Driver`
- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Usuário:** `admin`
- **Senha:** _(em branco)_

---

## 🧪 Testes
Para rodar os testes, basta clicar com o botão direito nos arquivos de teste dentro da sua IDE e selecionar **“Run Tests”**.  
Todos os casos cobrem **operações CRUD básicas** e **validações**.

---

## 📄 Licença
Este projeto está licenciado sob a **Licença MIT**.  
Veja o arquivo [LICENSE](LICENSE) para mais detalhes.
