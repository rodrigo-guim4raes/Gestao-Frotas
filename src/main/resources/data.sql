CREATE DATABASE IF NOT EXISTS frotas;
USE frotas;
DROP TABLE IF EXISTS frotas;

CREATE TABLE maquina (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    tipo VARCHAR(50) NOT NULL,          -- trator, colheitadeira, etc
    modelo VARCHAR(100),
    ano INT,
    numero_serie VARCHAR(100),
    horimetro_atual DECIMAL(10,2) DEFAULT 0,
    horimetro_prox_revisao DECIMAL(10,2), -- alvo da próxima revisão
    data_ultima_manutencao DATE,
    data_prox_manutencao DATE,
    ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE manutencao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    maquina_id BIGINT NOT NULL,
    data DATE NOT NULL,
    tipo VARCHAR(50) NOT NULL,           -- preventiva, corretiva, revisão geral, etc
    descricao TEXT,
    custo DECIMAL(10,2) NOT NULL,
    horas_uso_no_momento DECIMAL(10,2),
    CONSTRAINT fk_manutencao_maquina
        FOREIGN KEY (maquina_id) REFERENCES maquina(id)
);

CREATE TABLE registro_horas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    maquina_id BIGINT NOT NULL,
    data DATE NOT NULL,
    horas_trabalhadas DECIMAL(10,2) NOT NULL,
    observacao TEXT,
    CONSTRAINT fk_registro_horas_maquina
        FOREIGN KEY (maquina_id) REFERENCES maquina(id)
);

CREATE TABLE abastecimento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    maquina_id BIGINT NOT NULL,
    data DATE NOT NULL,
    litros DECIMAL(10,2) NOT NULL,
    custo_total DECIMAL(10,2) NOT NULL,
    horimetro DECIMAL(10,2),             -- horas no momento do abastecimento
    CONSTRAINT fk_abastecimento_maquina
        FOREIGN KEY (maquina_id) REFERENCES maquina(id)
);
