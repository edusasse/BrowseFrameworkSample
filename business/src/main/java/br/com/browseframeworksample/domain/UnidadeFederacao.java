package br.com.browseframeworksample.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import br.com.browseframework.base.data.dto.BaseDTO;

@Audited
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "unidade_federacao")
public class UnidadeFederacao extends BaseDTO<Long> {

	private static final long serialVersionUID = -6604954730211915568L;

	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "COD_UF", length = 11)
	private Long id;

	@Column(name = "DES_UF", nullable = false, length = 45, unique=true)
	private String nome;

	@Column(name = "DES_SIGLA_UF", nullable = false, length = 2)
	private String sigla;

	@Version
	@Column(name = "NRO_VERSAO", nullable = false, length = 10)
	private Long versao;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	// GETTERS && SETTERS 
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public Long getVersao() {
		return versao;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
	}

}