-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema ai_sample
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema ai_sample
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ai_sample` DEFAULT CHARACTER SET latin1 ;
USE `ai_sample` ;

-- -----------------------------------------------------
-- Table `ai_sample`.`auditoria_revisao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`auditoria_revisao` (
  `nro_rev` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `des_apelido` VARCHAR(255) NULL DEFAULT NULL COMMENT '',
  `des_aplicacao` VARCHAR(255) NULL DEFAULT NULL COMMENT '',
  `nro_timestamp` BIGINT(20) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`nro_rev`)  COMMENT '')
ENGINE = MyISAM
AUTO_INCREMENT = 119768
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`unidade_federacao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`unidade_federacao` (
  `cod_uf` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `des_uf` VARCHAR(45) NOT NULL COMMENT '',
  `des_sigla_uf` VARCHAR(2) NOT NULL COMMENT '',
  `nro_versao` INT(11) NOT NULL COMMENT '',
  PRIMARY KEY (`cod_uf`)  COMMENT '')
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`cidade`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`cidade` (
  `cod_cidade` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `nom_cidade` VARCHAR(60) NOT NULL COMMENT '',
  `cod_uf` INT(11) NOT NULL COMMENT '',
  `nro_versao` INT(11) NOT NULL COMMENT '',
  PRIMARY KEY (`cod_cidade`)  COMMENT '',
  INDEX `fk_cidade_unidade_federacao1_idx` (`cod_uf` ASC)  COMMENT '',
  CONSTRAINT `fk_cidade_unidade_federacao1`
    FOREIGN KEY (`cod_uf`)
    REFERENCES `ai_sample`.`unidade_federacao` (`cod_uf`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 9
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`cidade_VER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`cidade_VER` (
  `COD_CIDADE` BIGINT(20) NOT NULL COMMENT '',
  `NRO_REV` INT(11) NOT NULL COMMENT '',
  `IND_REVTYPE` TINYINT(4) NULL DEFAULT NULL COMMENT '',
  `NOM_CIDADE` VARCHAR(50) NULL DEFAULT NULL COMMENT '',
  `COD_UF` BIGINT(20) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`COD_CIDADE`, `NRO_REV`)  COMMENT '',
  INDEX `FK980A9FE8E912C828` (`NRO_REV` ASC)  COMMENT '')
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`classificacao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`classificacao` (
  `cod_classificacao` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `des_classificacao` VARCHAR(45) NOT NULL COMMENT '',
  `nro_ordem` INT(11) NOT NULL DEFAULT '999' COMMENT '',
  `nro_versao` INT(11) NOT NULL COMMENT '',
  PRIMARY KEY (`cod_classificacao`)  COMMENT '')
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`classificacao_VER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`classificacao_VER` (
  `COD_CLASSIFICACAO` BIGINT(20) NOT NULL COMMENT '',
  `NRO_REV` INT(11) NOT NULL COMMENT '',
  `IND_REVTYPE` TINYINT(4) NULL DEFAULT NULL COMMENT '',
  `DES_CLASSIFICACAO` VARCHAR(45) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`COD_CLASSIFICACAO`, `NRO_REV`)  COMMENT '',
  INDEX `FK83974003E912C828` (`NRO_REV` ASC)  COMMENT '')
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`pessoa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`pessoa` (
  `cod_pessoa` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `nom_pessoa` VARCHAR(80) NOT NULL COMMENT '',
  `ind_tipo_pessoa` VARCHAR(1) NOT NULL COMMENT '',
  `des_email` VARCHAR(70) NOT NULL COMMENT '',
  `des_email_faturamento` VARCHAR(70) NULL DEFAULT NULL COMMENT '',
  `des_observacao` VARCHAR(3000) NULL DEFAULT NULL COMMENT '',
  `nro_versao` INT(11) NOT NULL COMMENT '',
  PRIMARY KEY (`cod_pessoa`)  COMMENT '')
ENGINE = InnoDB
AUTO_INCREMENT = 454
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`cliente` (
  `cod_cliente` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `nom_contato` VARCHAR(85) NULL DEFAULT NULL COMMENT '',
  `ind_situacao` VARCHAR(1) NULL DEFAULT NULL COMMENT '',
  `cod_pessoa` INT(11) NOT NULL COMMENT '',
  `cod_pessoa_responsavel_conta` INT(11) NOT NULL COMMENT '',
  `des_observacao` TEXT NULL DEFAULT NULL COMMENT '',
  `ind_periodicidade` VARCHAR(25) NULL DEFAULT NULL COMMENT '',
  `dat_abertura` DATE NULL DEFAULT NULL COMMENT '',
  `qtd_meta_acoes` INT(11) NULL DEFAULT NULL COMMENT '',
  `nro_versao` INT(11) NOT NULL COMMENT '',
  `ind_periodo_fechamento_mensal` INT(11) NOT NULL DEFAULT '0' COMMENT '',
  PRIMARY KEY (`cod_cliente`)  COMMENT '',
  INDEX `fk_cliente_pessoa1_idx` (`cod_pessoa` ASC)  COMMENT '',
  INDEX `fk_cliente_pessoa2_idx` (`cod_pessoa_responsavel_conta` ASC)  COMMENT '',
  CONSTRAINT `fk_cliente_pessoa1`
    FOREIGN KEY (`cod_pessoa`)
    REFERENCES `ai_sample`.`pessoa` (`cod_pessoa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_cliente_pessoa2`
    FOREIGN KEY (`cod_pessoa_responsavel_conta`)
    REFERENCES `ai_sample`.`pessoa` (`cod_pessoa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 196
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`cliente_VER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`cliente_VER` (
  `COD_CLIENTE` BIGINT(20) NOT NULL COMMENT '',
  `NRO_REV` INT(11) NOT NULL COMMENT '',
  `IND_REVTYPE` TINYINT(4) NULL DEFAULT NULL COMMENT '',
  `NOM_CONTATO` VARCHAR(85) NULL DEFAULT NULL COMMENT '',
  `DAT_ABERTURA` DATE NULL DEFAULT NULL COMMENT '',
  `IND_PERIODO_FECHAMENTO_MENSAL` TINYINT(1) NULL DEFAULT NULL COMMENT '',
  `QTD_META_ACOES` BIGINT(20) NULL DEFAULT NULL COMMENT '',
  `DES_OBSERVACAO` LONGTEXT NULL DEFAULT NULL COMMENT '',
  `IND_SITUACAO` VARCHAR(1) NULL DEFAULT NULL COMMENT '',
  `COD_PESSOA` BIGINT(20) NULL DEFAULT NULL COMMENT '',
  `COD_PESSOA_RESPONSAVEL_CONTA` BIGINT(20) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`COD_CLIENTE`, `NRO_REV`)  COMMENT '',
  INDEX `FKF5F073BEE912C828` (`NRO_REV` ASC)  COMMENT '')
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`tipo_endereco`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`tipo_endereco` (
  `cod_tipo_endereco` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `des_tipo_endereco` VARCHAR(45) NOT NULL COMMENT '',
  `nro_versao` INT(11) NOT NULL COMMENT '',
  PRIMARY KEY (`cod_tipo_endereco`)  COMMENT '')
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`endereco`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`endereco` (
  `cod_endereco` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `cod_pessoa` INT(11) NOT NULL COMMENT '',
  `nro_cep` DECIMAL(8,0) NULL DEFAULT NULL COMMENT '',
  `des_logradouro` VARCHAR(70) NULL DEFAULT NULL COMMENT '',
  `nro_logradouro` VARCHAR(5) NULL DEFAULT NULL COMMENT '',
  `des_bairro` VARCHAR(60) NULL DEFAULT NULL COMMENT '',
  `des_complemento` VARCHAR(120) NULL DEFAULT NULL COMMENT '',
  `cod_tipo_endereco` INT(11) NOT NULL COMMENT '',
  `cod_cidade` INT(11) NOT NULL COMMENT '',
  `nro_versao` INT(11) NOT NULL COMMENT '',
  PRIMARY KEY (`cod_endereco`)  COMMENT '',
  INDEX `fk_endereco_tipo_endereco1` (`cod_tipo_endereco` ASC)  COMMENT '',
  INDEX `fk_endereco_cidade1` (`cod_cidade` ASC)  COMMENT '',
  INDEX `fk_endereco_pessoa1` (`cod_pessoa` ASC)  COMMENT '',
  CONSTRAINT `fk_endereco_cidade1`
    FOREIGN KEY (`cod_cidade`)
    REFERENCES `ai_sample`.`cidade` (`cod_cidade`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_endereco_pessoa1`
    FOREIGN KEY (`cod_pessoa`)
    REFERENCES `ai_sample`.`pessoa` (`cod_pessoa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_endereco_tipo_endereco1`
    FOREIGN KEY (`cod_tipo_endereco`)
    REFERENCES `ai_sample`.`tipo_endereco` (`cod_tipo_endereco`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 9
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`endereco_VER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`endereco_VER` (
  `COD_ENDERECO` BIGINT(20) NOT NULL COMMENT '',
  `NRO_REV` INT(11) NOT NULL COMMENT '',
  `IND_REVTYPE` TINYINT(4) NULL DEFAULT NULL COMMENT '',
  `DES_BAIRRO` VARCHAR(60) NULL DEFAULT NULL COMMENT '',
  `NRO_CEP` BIGINT(20) NULL DEFAULT NULL COMMENT '',
  `DES_COMPLEMENTO` VARCHAR(120) NULL DEFAULT NULL COMMENT '',
  `DES_LOGRADOURO` VARCHAR(70) NULL DEFAULT NULL COMMENT '',
  `NRO_LOGRADOURO` BIGINT(20) NULL DEFAULT NULL COMMENT '',
  `COD_CIDADE` BIGINT(20) NULL DEFAULT NULL COMMENT '',
  `COD_PESSOA` BIGINT(20) NULL DEFAULT NULL COMMENT '',
  `COD_TIPO_ENDERECO` BIGINT(20) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`COD_ENDERECO`, `NRO_REV`)  COMMENT '',
  INDEX `FKBF5ED60DE912C828` (`NRO_REV` ASC)  COMMENT '')
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`pessoa_fisica`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`pessoa_fisica` (
  `cod_pessoa` INT(11) NOT NULL COMMENT '',
  `ind_sexo` VARCHAR(1) NOT NULL COMMENT '',
  `dat_nascimento` DATETIME NULL DEFAULT NULL COMMENT '',
  `ind_membro_equipe` INT(11) NOT NULL DEFAULT '0' COMMENT '',
  PRIMARY KEY (`cod_pessoa`)  COMMENT '',
  INDEX `fk_pessoa_fisica_pessoa1_idx` (`cod_pessoa` ASC)  COMMENT '',
  CONSTRAINT `fk_pessoa_fisica_pessoa1`
    FOREIGN KEY (`cod_pessoa`)
    REFERENCES `ai_sample`.`pessoa` (`cod_pessoa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`equipe_cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`equipe_cliente` (
  `cod_equipe_cliente` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `cod_cliente` INT(11) NOT NULL COMMENT '',
  `cod_pessoa` INT(11) NOT NULL COMMENT '',
  PRIMARY KEY (`cod_equipe_cliente`)  COMMENT '',
  INDEX `fk_equipe_cliente_cliente1_idx` (`cod_cliente` ASC)  COMMENT '',
  INDEX `fk_equipe_cliente_pessoa1_idx` (`cod_pessoa` ASC)  COMMENT '',
  INDEX `FK1950C5F0907EFFD0` (`cod_pessoa` ASC)  COMMENT '',
  CONSTRAINT `FK1950C5F0907EFFD0`
    FOREIGN KEY (`cod_pessoa`)
    REFERENCES `ai_sample`.`pessoa_fisica` (`cod_pessoa`),
  CONSTRAINT `fk_equipe_cliente_cliente1`
    FOREIGN KEY (`cod_cliente`)
    REFERENCES `ai_sample`.`cliente` (`cod_cliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_equipe_cliente_pessoa1`
    FOREIGN KEY (`cod_pessoa`)
    REFERENCES `ai_sample`.`pessoa` (`cod_pessoa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 14985
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`equipe_cliente_VER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`equipe_cliente_VER` (
  `NRO_REV` INT(11) NOT NULL COMMENT '',
  `COD_CLIENTE` BIGINT(20) NOT NULL COMMENT '',
  `COD_PESSOA` BIGINT(20) NOT NULL COMMENT '',
  `IND_REVTYPE` TINYINT(4) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`NRO_REV`, `COD_CLIENTE`, `COD_PESSOA`)  COMMENT '',
  INDEX `FKD6C8C8B4E912C828` (`NRO_REV` ASC)  COMMENT '')
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`parametro`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`parametro` (
  `cod_parametro` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `des_parametro` VARCHAR(255) NOT NULL COMMENT '',
  `val_parametro` VARCHAR(255) NULL DEFAULT NULL COMMENT '',
  `nro_versao` INT(11) NOT NULL COMMENT '',
  PRIMARY KEY (`cod_parametro`)  COMMENT '',
  UNIQUE INDEX `des_parametro_unique` (`des_parametro` ASC)  COMMENT '')
ENGINE = InnoDB
AUTO_INCREMENT = 11
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`parametro_VER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`parametro_VER` (
  `COD_PARAMETRO` BIGINT(20) NOT NULL COMMENT '',
  `NRO_REV` INT(11) NOT NULL COMMENT '',
  `IND_REVTYPE` TINYINT(4) NULL DEFAULT NULL COMMENT '',
  `DES_PARAMETRO` VARCHAR(255) NULL DEFAULT NULL COMMENT '',
  `VAL_PARAMETRO` VARCHAR(255) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`COD_PARAMETRO`, `NRO_REV`)  COMMENT '',
  INDEX `FKFF6BEB7DE912C828` (`NRO_REV` ASC)  COMMENT '')
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`perfil`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`perfil` (
  `cod_perfil` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `des_nome` VARCHAR(45) NOT NULL COMMENT '',
  `des_role` VARCHAR(120) NOT NULL COMMENT '',
  `nro_versao` INT(11) NOT NULL COMMENT '',
  PRIMARY KEY (`cod_perfil`)  COMMENT '')
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`perfil_VER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`perfil_VER` (
  `cod_perfil` BIGINT(20) NOT NULL COMMENT '',
  `NRO_REV` INT(11) NOT NULL COMMENT '',
  `IND_REVTYPE` TINYINT(4) NULL DEFAULT NULL COMMENT '',
  `des_nome` VARCHAR(255) NULL DEFAULT NULL COMMENT '',
  `des_role` VARCHAR(255) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`cod_perfil`, `NRO_REV`)  COMMENT '',
  INDEX `FK6E6E1E90E912C828` (`NRO_REV` ASC)  COMMENT '')
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`pessoa_VER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`pessoa_VER` (
  `COD_PESSOA` BIGINT(20) NOT NULL COMMENT '',
  `NRO_REV` INT(11) NOT NULL COMMENT '',
  `IND_REVTYPE` TINYINT(4) NULL DEFAULT NULL COMMENT '',
  `DES_EMAIL` VARCHAR(70) NULL DEFAULT NULL COMMENT '',
  `DES_EMAIL_FATURAMENTO` VARCHAR(70) NULL DEFAULT NULL COMMENT '',
  `NOM_PESSOA` VARCHAR(80) NULL DEFAULT NULL COMMENT '',
  `DES_OBSERVACAO` LONGTEXT NULL DEFAULT NULL COMMENT '',
  `IND_TIPO_PESSOA` VARCHAR(1) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`COD_PESSOA`, `NRO_REV`)  COMMENT '',
  INDEX `FK8FA25EEBE912C828` (`NRO_REV` ASC)  COMMENT '')
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`pessoa_fisica_VER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`pessoa_fisica_VER` (
  `COD_PESSOA` BIGINT(20) NOT NULL COMMENT '',
  `NRO_REV` INT(11) NOT NULL COMMENT '',
  `DAT_NASCIMENTO` DATE NULL DEFAULT NULL COMMENT '',
  `IND_MEMBRO_EQUIPE` TINYINT(1) NULL DEFAULT NULL COMMENT '',
  `IND_SEXO` VARCHAR(1) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`COD_PESSOA`, `NRO_REV`)  COMMENT '',
  INDEX `FKE36AFF532466824C` (`COD_PESSOA` ASC, `NRO_REV` ASC)  COMMENT '')
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`pessoa_juridica`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`pessoa_juridica` (
  `cod_pessoa` INT(11) NOT NULL COMMENT '',
  `des_razao_social` VARCHAR(70) NOT NULL COMMENT '',
  `nro_cnpj` DECIMAL(15,0) NULL DEFAULT NULL COMMENT '',
  `cod_inscricao_estadual` VARCHAR(20) NULL DEFAULT NULL COMMENT '',
  `cod_inscricao_municipal` VARCHAR(20) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`cod_pessoa`)  COMMENT '',
  INDEX `fk_pessoa_juridica_pessoa1_idx` (`cod_pessoa` ASC)  COMMENT '',
  CONSTRAINT `fk_pessoa_juridica_pessoa1`
    FOREIGN KEY (`cod_pessoa`)
    REFERENCES `ai_sample`.`pessoa` (`cod_pessoa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`pessoa_juridica_VER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`pessoa_juridica_VER` (
  `COD_PESSOA` BIGINT(20) NOT NULL COMMENT '',
  `NRO_REV` INT(11) NOT NULL COMMENT '',
  `NRO_CNPJ` VARCHAR(15) NULL DEFAULT NULL COMMENT '',
  `COD_INSCRICAO_ESTADUAL` VARCHAR(20) NULL DEFAULT NULL COMMENT '',
  `COD_INSCRICAO_MUNICIPAL` VARCHAR(20) NULL DEFAULT NULL COMMENT '',
  `DES_RAZAO_SOCIAL` VARCHAR(50) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`COD_PESSOA`, `NRO_REV`)  COMMENT '',
  INDEX `FKF06B51412466824C` (`COD_PESSOA` ASC, `NRO_REV` ASC)  COMMENT '')
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`relatorio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`relatorio` (
  `cod_relatorio` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `nom_relatorio` VARCHAR(255) NOT NULL COMMENT '',
  `des_select` TEXT NULL DEFAULT NULL COMMENT '',
  `des_select_order_by` TEXT NULL DEFAULT NULL COMMENT '',
  `dat_inicio_vigencia` DATETIME NULL DEFAULT NULL COMMENT '',
  `dat_fim_vigencia` DATETIME NULL DEFAULT NULL COMMENT '',
  `ind_ativo` BIT(1) NOT NULL COMMENT '',
  `nro_versao` INT(11) NOT NULL COMMENT '',
  PRIMARY KEY (`cod_relatorio`)  COMMENT '')
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`relatorio_VER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`relatorio_VER` (
  `cod_relatorio` BIGINT(20) NOT NULL COMMENT '',
  `NRO_REV` INT(11) NOT NULL COMMENT '',
  `IND_REVTYPE` TINYINT(4) NULL DEFAULT NULL COMMENT '',
  `ind_ativo` TINYINT(1) NULL DEFAULT NULL COMMENT '',
  `dat_fim_vigencia` DATETIME NULL DEFAULT NULL COMMENT '',
  `dat_inicio_vigencia` DATETIME NULL DEFAULT NULL COMMENT '',
  `nom_relatorio` VARCHAR(255) NULL DEFAULT NULL COMMENT '',
  `des_select_order_by` LONGTEXT NULL DEFAULT NULL COMMENT '',
  `des_select` LONGTEXT NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`cod_relatorio`, `NRO_REV`)  COMMENT '',
  INDEX `FK215F1319E912C828` (`NRO_REV` ASC)  COMMENT '')
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`relatorio_campo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`relatorio_campo` (
  `cod_relatorio_campo` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `cod_relatorio` INT(11) NOT NULL COMMENT '',
  `nom_campo` VARCHAR(255) NOT NULL COMMENT '',
  `ind_tipo_campo` CHAR(3) NOT NULL COMMENT '',
  `des_mascara` VARCHAR(255) NULL DEFAULT NULL COMMENT '',
  `nro_versao` INT(11) NOT NULL COMMENT '',
  PRIMARY KEY (`cod_relatorio_campo`, `cod_relatorio`)  COMMENT '',
  INDEX `fk_relatorio_campo_relatorio1` (`cod_relatorio` ASC)  COMMENT '',
  CONSTRAINT `fk_relatorio_campo_relatorio1`
    FOREIGN KEY (`cod_relatorio`)
    REFERENCES `ai_sample`.`relatorio` (`cod_relatorio`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`relatorio_campo_VER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`relatorio_campo_VER` (
  `cod_relatorio_campo` BIGINT(20) NOT NULL COMMENT '',
  `NRO_REV` INT(11) NOT NULL COMMENT '',
  `IND_REVTYPE` TINYINT(4) NULL DEFAULT NULL COMMENT '',
  `des_mascara` VARCHAR(120) NULL DEFAULT NULL COMMENT '',
  `nom_campo` VARCHAR(50) NULL DEFAULT NULL COMMENT '',
  `ind_tipo_campo` VARCHAR(3) NULL DEFAULT NULL COMMENT '',
  `cod_relatorio` BIGINT(20) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`cod_relatorio_campo`, `NRO_REV`)  COMMENT '',
  INDEX `FKDCCAD8E8E912C828` (`NRO_REV` ASC)  COMMENT '')
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`relatorio_parametro`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`relatorio_parametro` (
  `cod_relatorio_parametro` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `cod_relatorio` INT(11) NOT NULL COMMENT '',
  `nro_sequencia` INT(11) NOT NULL COMMENT '',
  `nom_parametro` VARCHAR(255) NOT NULL COMMENT '',
  `ind_tipo_campo` CHAR(3) NOT NULL COMMENT '',
  `des_valor_padrao` VARCHAR(255) NULL DEFAULT NULL COMMENT '',
  `ind_obrigatorio` BIT(1) NOT NULL COMMENT '',
  `des_mascara` VARCHAR(255) NULL DEFAULT NULL COMMENT '',
  `nro_versao` INT(11) NOT NULL COMMENT '',
  PRIMARY KEY (`cod_relatorio_parametro`, `cod_relatorio`)  COMMENT '',
  INDEX `fk_relatorio_parametro_relatorio1` (`cod_relatorio` ASC)  COMMENT '',
  CONSTRAINT `fk_relatorio_parametro_relatorio1`
    FOREIGN KEY (`cod_relatorio`)
    REFERENCES `ai_sample`.`relatorio` (`cod_relatorio`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`relatorio_parametro_VER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`relatorio_parametro_VER` (
  `cod_relatorio_parametro` BIGINT(20) NOT NULL COMMENT '',
  `NRO_REV` INT(11) NOT NULL COMMENT '',
  `IND_REVTYPE` TINYINT(4) NULL DEFAULT NULL COMMENT '',
  `des_mascara` VARCHAR(255) NULL DEFAULT NULL COMMENT '',
  `nom_parametro` VARCHAR(255) NULL DEFAULT NULL COMMENT '',
  `ind_obrigatorio` TINYINT(1) NULL DEFAULT NULL COMMENT '',
  `nro_sequencia` INT(11) NULL DEFAULT NULL COMMENT '',
  `ind_tipo_campo` VARCHAR(3) NULL DEFAULT NULL COMMENT '',
  `des_valor_padrao` VARCHAR(255) NULL DEFAULT NULL COMMENT '',
  `cod_relatorio` BIGINT(20) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`cod_relatorio_parametro`, `NRO_REV`)  COMMENT '',
  INDEX `FKD57DAF93E912C828` (`NRO_REV` ASC)  COMMENT '')
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`relatorio_perfil`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`relatorio_perfil` (
  `cod_relatorio` INT(11) NOT NULL COMMENT '',
  `cod_perfil` INT(11) NOT NULL COMMENT '',
  PRIMARY KEY (`cod_relatorio`, `cod_perfil`)  COMMENT '',
  INDEX `fk_relatorio_has_perfil_perfil1` (`cod_perfil` ASC)  COMMENT '',
  INDEX `fk_relatorio_has_perfil_relatorio1` (`cod_relatorio` ASC)  COMMENT '',
  CONSTRAINT `fk_relatorio_has_perfil_perfil1`
    FOREIGN KEY (`cod_perfil`)
    REFERENCES `ai_sample`.`perfil` (`cod_perfil`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_relatorio_has_perfil_relatorio1`
    FOREIGN KEY (`cod_relatorio`)
    REFERENCES `ai_sample`.`relatorio` (`cod_relatorio`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`tipo_telefone`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`tipo_telefone` (
  `cod_tipo_telefone` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `des_tipo_telefone` VARCHAR(45) NOT NULL COMMENT '',
  `nro_versao` INT(11) NOT NULL COMMENT '',
  PRIMARY KEY (`cod_tipo_telefone`)  COMMENT '')
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`telefone`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`telefone` (
  `cod_telefone` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `cod_tipo_telefone` INT(11) NOT NULL COMMENT '',
  `nro_telefone` VARCHAR(15) NOT NULL COMMENT '',
  `nom_pessoa_contato` VARCHAR(45) NULL DEFAULT NULL COMMENT '',
  `cod_pessoa` INT(11) NOT NULL COMMENT '',
  `nro_versao` INT(11) NOT NULL COMMENT '',
  PRIMARY KEY (`cod_telefone`)  COMMENT '',
  INDEX `fk_telefone_tipo_telefone1_idx` (`cod_tipo_telefone` ASC)  COMMENT '',
  INDEX `fk_telefone_pessoa1` (`cod_pessoa` ASC)  COMMENT '',
  CONSTRAINT `fk_telefone_pessoa1`
    FOREIGN KEY (`cod_pessoa`)
    REFERENCES `ai_sample`.`pessoa` (`cod_pessoa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_telefone_tipo_telefone1`
    FOREIGN KEY (`cod_tipo_telefone`)
    REFERENCES `ai_sample`.`tipo_telefone` (`cod_tipo_telefone`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`telefone_VER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`telefone_VER` (
  `COD_TELEFONE` BIGINT(20) NOT NULL COMMENT '',
  `NRO_REV` INT(11) NOT NULL COMMENT '',
  `IND_REVTYPE` TINYINT(4) NULL DEFAULT NULL COMMENT '',
  `NOM_PESSOA_CONTATO` VARCHAR(45) NULL DEFAULT NULL COMMENT '',
  `NRO_TELEFONE` VARCHAR(15) NULL DEFAULT NULL COMMENT '',
  `COD_PESSOA` BIGINT(20) NULL DEFAULT NULL COMMENT '',
  `COD_TIPO_TELEFONE` BIGINT(20) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`COD_TELEFONE`, `NRO_REV`)  COMMENT '',
  INDEX `FKDDB160AEE912C828` (`NRO_REV` ASC)  COMMENT '')
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`tipo_endereco_VER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`tipo_endereco_VER` (
  `COD_TIPO_ENDERECO` BIGINT(20) NOT NULL COMMENT '',
  `NRO_REV` INT(11) NOT NULL COMMENT '',
  `IND_REVTYPE` TINYINT(4) NULL DEFAULT NULL COMMENT '',
  `DES_TIPO_ENDERECO` VARCHAR(50) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`COD_TIPO_ENDERECO`, `NRO_REV`)  COMMENT '',
  INDEX `FK92F3C138E912C828` (`NRO_REV` ASC)  COMMENT '')
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`tipo_telefone_VER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`tipo_telefone_VER` (
  `COD_TIPO_TELEFONE` BIGINT(20) NOT NULL COMMENT '',
  `NRO_REV` INT(11) NOT NULL COMMENT '',
  `IND_REVTYPE` TINYINT(4) NULL DEFAULT NULL COMMENT '',
  `DES_TIPO_TELEFONE` VARCHAR(45) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`COD_TIPO_TELEFONE`, `NRO_REV`)  COMMENT '',
  INDEX `FKB1464BD9E912C828` (`NRO_REV` ASC)  COMMENT '')
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`unidade_federacao_VER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`unidade_federacao_VER` (
  `COD_UF` BIGINT(20) NOT NULL COMMENT '',
  `NRO_REV` INT(11) NOT NULL COMMENT '',
  `IND_REVTYPE` TINYINT(4) NULL DEFAULT NULL COMMENT '',
  `DES_UF` VARCHAR(45) NULL DEFAULT NULL COMMENT '',
  `DES_SIGLA_UF` VARCHAR(2) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`COD_UF`, `NRO_REV`)  COMMENT '',
  INDEX `FK7EC4A7B5E912C828` (`NRO_REV` ASC)  COMMENT '')
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`usuario` (
  `cod_usuario` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `des_apelido` VARCHAR(45) NOT NULL COMMENT '',
  `des_senha` VARCHAR(50) NOT NULL COMMENT '',
  `ind_ativo` BIT(1) NOT NULL COMMENT '',
  `cod_pessoa` INT(11) NOT NULL COMMENT '',
  `dat_acesso_anterior` DATETIME NULL DEFAULT NULL COMMENT '',
  `dat_ultimo_acesso` DATETIME NULL DEFAULT NULL COMMENT '',
  `nro_versao` INT(11) NOT NULL COMMENT '',
  PRIMARY KEY (`cod_usuario`)  COMMENT '',
  UNIQUE INDEX `cod_pessoa_unique` (`cod_pessoa` ASC)  COMMENT '',
  UNIQUE INDEX `des_apelido_unique` (`des_apelido` ASC)  COMMENT '',
  INDEX `fk_usuario_pessoa1` (`cod_pessoa` ASC)  COMMENT '',
  CONSTRAINT `fk_usuario_pessoa1`
    FOREIGN KEY (`cod_pessoa`)
    REFERENCES `ai_sample`.`pessoa_fisica` (`cod_pessoa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 232
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`usuario_VER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`usuario_VER` (
  `cod_usuario` BIGINT(20) NOT NULL COMMENT '',
  `NRO_REV` INT(11) NOT NULL COMMENT '',
  `IND_REVTYPE` TINYINT(4) NULL DEFAULT NULL COMMENT '',
  `des_apelido` VARCHAR(255) NULL DEFAULT NULL COMMENT '',
  `ind_ativo` TINYINT(1) NULL DEFAULT NULL COMMENT '',
  `DAT_ACESSO_ANTERIOR` DATETIME NULL DEFAULT NULL COMMENT '',
  `DAT_ULTIMO_ACESSO` DATETIME NULL DEFAULT NULL COMMENT '',
  `des_senha` VARCHAR(255) NULL DEFAULT NULL COMMENT '',
  `COD_PESSOA` BIGINT(20) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`cod_usuario`, `NRO_REV`)  COMMENT '',
  INDEX `FK311826F2E912C828` (`NRO_REV` ASC)  COMMENT '')
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`usuario_cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`usuario_cliente` (
  `cod_usuario_cliente` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `cod_cliente` INT(11) NOT NULL COMMENT '',
  `cod_usuario` INT(11) NOT NULL COMMENT '',
  PRIMARY KEY (`cod_usuario_cliente`)  COMMENT '',
  INDEX `fk_usuario_cliente_cliente1_idx` (`cod_cliente` ASC)  COMMENT '',
  INDEX `fk_usuario_cliente_usuario1_idx` (`cod_usuario` ASC)  COMMENT '',
  CONSTRAINT `fk_usuario_cliente_cliente1`
    FOREIGN KEY (`cod_cliente`)
    REFERENCES `ai_sample`.`cliente` (`cod_cliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_usuario_cliente_usuario1`
    FOREIGN KEY (`cod_usuario`)
    REFERENCES `ai_sample`.`usuario` (`cod_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 4281
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`usuario_cliente_VER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`usuario_cliente_VER` (
  `NRO_REV` INT(11) NOT NULL COMMENT '',
  `COD_CLIENTE` BIGINT(20) NOT NULL COMMENT '',
  `COD_USUARIO` BIGINT(20) NOT NULL COMMENT '',
  `IND_REVTYPE` TINYINT(4) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`NRO_REV`, `COD_CLIENTE`, `COD_USUARIO`)  COMMENT '',
  INDEX `FK85A8702DE912C828` (`NRO_REV` ASC)  COMMENT '')
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`usuario_cliente_clipping`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`usuario_cliente_clipping` (
  `cod_usuario_cliente_clipping` INT(11) NOT NULL AUTO_INCREMENT COMMENT '',
  `cod_cliente` INT(11) NOT NULL COMMENT '',
  `cod_usuario` INT(11) NOT NULL COMMENT '',
  PRIMARY KEY (`cod_usuario_cliente_clipping`)  COMMENT '',
  INDEX `fk_usuario_has_cliente_cliente1` (`cod_cliente` ASC)  COMMENT '',
  INDEX `fk_usuario_has_cliente_usuario1` (`cod_usuario` ASC)  COMMENT '',
  CONSTRAINT `fk_usuario_has_cliente_cliente1`
    FOREIGN KEY (`cod_cliente`)
    REFERENCES `ai_sample`.`cliente` (`cod_cliente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_usuario_has_cliente_usuario1`
    FOREIGN KEY (`cod_usuario`)
    REFERENCES `ai_sample`.`usuario` (`cod_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 1480
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`usuario_cliente_clipping_ver`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`usuario_cliente_clipping_ver` (
  `NRO_REV` INT(11) NOT NULL COMMENT '',
  `COD_CLIENTE` BIGINT(20) NOT NULL COMMENT '',
  `COD_USUARIO` BIGINT(20) NOT NULL COMMENT '',
  `IND_REVTYPE` TINYINT(4) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`NRO_REV`, `COD_CLIENTE`, `COD_USUARIO`)  COMMENT '',
  INDEX `FK_62ivbm2tkfx2gerhq96peco5f` (`NRO_REV` ASC)  COMMENT '')
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`usuario_perfil`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`usuario_perfil` (
  `cod_usuario` INT(11) NOT NULL COMMENT '',
  `cod_perfil` INT(11) NOT NULL COMMENT '',
  PRIMARY KEY (`cod_usuario`, `cod_perfil`)  COMMENT '',
  INDEX `fk_usuario_has_perfil_perfil1` (`cod_perfil` ASC)  COMMENT '',
  INDEX `fk_usuario_has_perfil_usuario1` (`cod_usuario` ASC)  COMMENT '',
  CONSTRAINT `fk_usuario_has_perfil_perfil1`
    FOREIGN KEY (`cod_perfil`)
    REFERENCES `ai_sample`.`perfil` (`cod_perfil`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_usuario_has_perfil_usuario1`
    FOREIGN KEY (`cod_usuario`)
    REFERENCES `ai_sample`.`usuario` (`cod_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `ai_sample`.`usuario_perfil_VER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ai_sample`.`usuario_perfil_VER` (
  `NRO_REV` INT(11) NOT NULL COMMENT '',
  `cod_perfil` BIGINT(20) NOT NULL COMMENT '',
  `cod_usuario` BIGINT(20) NOT NULL COMMENT '',
  `IND_REVTYPE` TINYINT(4) NULL DEFAULT NULL COMMENT '',
  PRIMARY KEY (`NRO_REV`, `cod_usuario`, `cod_perfil`)  COMMENT '',
  INDEX `FKA49D5841E912C828` (`NRO_REV` ASC)  COMMENT '')
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin1;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
