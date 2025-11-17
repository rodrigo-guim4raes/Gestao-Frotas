🚜 Gestão de Frotas – Sistema Web

Sistema web completo para gerenciamento de máquinas, com recursos de cadastro, registro de manutenções, abastecimentos, horas trabalhadas e alertas automáticos de revisão.
O projeto possui um backend em Java + Spring Boot e um frontend simples em HTML, CSS e JavaScript.

---

📌 Funcionalidades

🔵 Máquinas

Cadastro, edição e exclusão de máquinas

Controle de:

Horímetro atual

Horímetro da próxima revisão

Datas de manutenção

Máquinas ativas/inativas

Alertas automáticos de revisão (por horas e por data)


🟩 Manutenções

Registro de manutenções por máquina

Campos: data, tipo, descrição, custo, horas de uso

Listagem por máquina

Total gasto automaticamente calculado

🟧 Abastecimentos

Registro de abastecimentos

Campos: data, litros, custo total, horímetro

Listagem por máquina

Cálculo de:

Total de litros

Total gasto

Consumo médio (litros/hora)

🟪 Horas Trabalhadas

Registro diário de horas

Filtro por período

Total de horas trabalhadas por intervalo

---

🌙 Tema Claro/Escuro

Botão no canto superior direito alterna tema

Preferência salva no navegador (localStorage)

---     

⚙️ Tecnologias Utilizadas
Backend

Java 17+

Spring Boot 3.x

Spring Web

Spring Data JPA

MySQL 8+

Frontend

HTML5

CSS3

JavaScript Puro (sem frameworks)

---

🛢️ Configuração do Banco de Dados (MySQL)

Crie o banco:

CREATE DATABASE gestao_frota;


Configure em application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/nome do banco
spring.datasource.username=seu usuario
spring.datasource.password=sua senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

---

🚀 Rodando o Backend

Dentro da pasta backend/:

mvn spring-boot:run

API estará disponível em:

http://localhost:8080

---

🌐 Rodando o Frontend

Basta abrir o arquivo:

frontend/index.html

Não precisa de servidor — funciona direto no navegador.

---

🔥 Principais Endpoints da API
Máquinas

GET    /api/maquinas

GET    /api/maquinas/{id}

POST   /api/maquinas

PUT    /api/maquinas/{id}

DELETE /api/maquinas/{id}

GET    /api/maquinas/alertas/horas?margemHoras=10

GET    /api/maquinas/alertas/datas?diasAntes=7

Manutenções

POST   /api/manutencoes/maquina/{id}

GET    /api/manutencoes/maquina/{id}

GET    /api/manutencoes/maquina/{id}/total-gasto

Abastecimentos

POST   /api/abastecimentos/maquina/{id}

GET    /api/abastecimentos/maquina/{id}

GET    /api/abastecimentos/maquina/{id}/totais

GET    /api/abastecimentos/maquina/{id}/consumo-medio

Horas Trabalhadas

POST   /api/horas/maquina/{id}

GET    /api/horas/maquina/{id}

GET    /api/horas/maquina/{id}/total

---

📖 Como Usar

Inicie o backend em Spring Boot

Abra index.html no navegador

Cadastre suas máquinas

Registre manutenções, abastecimentos e horas

Visualize relatórios e alertas automaticamente

---

🧩 Melhorias Futuras

Sistema de login (JWT)

Dashboard com gráficos

Exportação de relatórios PDF/Excel

Upload de imagem das máquinas

Controle multiusuário

---

📜 Licença

Projeto livre para uso e estudos.
