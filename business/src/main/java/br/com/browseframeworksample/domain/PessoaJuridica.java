package br.com.browseframeworksample.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import br.com.browseframeworksample.domain.enums.TipoPessoa;

@Audited
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "pessoa_juridica")
public class PessoaJuridica extends Pessoa {

	private static final long serialVersionUID = -1247634464161811013L;

	public PessoaJuridica() {
		super(TipoPessoa.J);
	}

	@Column(name = "DES_RAZAO_SOCIAL", nullable = false , length = 50)
	private String razaoSocial;

	@Column(name = "NRO_CNPJ", nullable = true, length = 15)
	private String cnpj;
	
	@Column(name = "COD_INSCRICAO_ESTADUAL", nullable = true, length = 20)
	private String inscricaoEstadual;

	@Column(name = "COD_INSCRICAO_MUNICIPAL", nullable = true, length = 20)
	private String inscricaoMunicipal;

	// GETTERS && SETTERS 
	
	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}

	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}
	
}
