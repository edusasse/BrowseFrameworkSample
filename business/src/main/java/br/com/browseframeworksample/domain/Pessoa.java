package br.com.browseframeworksample.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import br.com.browseframeworksample.domain.enums.TipoPessoa;
import br.com.browseframework.base.data.dto.BaseDTO;

@Audited
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "pessoa")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Pessoa extends BaseDTO<Long> {
	
	private static final long serialVersionUID = 2101316489789556825L;
	
	public Pessoa(TipoPessoa tipoPessoa) {
		setTipoPessoa(tipoPessoa); 
	}
	
	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "COD_PESSOA", length = 11)
	private Long id;
	
	@Column(name = "NOM_PESSOA", nullable = false, length = 80)
	private String nome;
	
	@Column(name = "IND_TIPO_PESSOA", nullable = false, length = 1)
	@Enumerated(EnumType.STRING)
	private TipoPessoa tipoPessoa;

	@Column(name = "DES_EMAIL", nullable = false, length = 70)
	private String email;

	@Column(name = "DES_EMAIL_FATURAMENTO", nullable = true, length = 70)
	private String emailFaturamento;

	@Column(name = "DES_OBSERVACAO", length = 500, nullable = true)
	private String observacao;
	
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

	public TipoPessoa getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailFaturamento() {
		return emailFaturamento;
	}

	public void setEmailFaturamento(String emailFaturamento) {
		this.emailFaturamento = emailFaturamento;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Long getVersao() {
		return versao;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
	}

}
