# CarExercise

Este projeto é uma aplicação Java Spring Boot para gestão de usuários e veículos.

## Funcionalidades
- Cadastro, atualização, desativação e exclusão de contas de usuário
- Associação de veículos a usuários
- Consulta de contas desativadas e veículos ativos
- Validações de dados e tratamento de exceções customizadas

## Estrutura do Projeto
- `controller/` - Controllers REST para usuários e veículos
- `entity/` - Entidades JPA (UserEntity, VehicleEntity)
- `exceptions/` - Exceções customizadas
- `model/` - DTOs e conversores
- `repository/` - Repositórios Spring Data JPA
- `service/` - Lógica de negócio

## Como rodar o projeto
1. Certifique-se de ter o Java 17+ e Maven instalados.
2. Clone o repositório.
3. Execute `./mvnw spring-boot:run` na raiz do projeto.

## Testes
Para rodar os testes:
```
./mvnw test
```

## Configuração
As configurações estão em `src/main/resources/application.properties`.


