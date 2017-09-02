package br.com.browseframeworksample.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import br.com.browseframeworksample.domain.enums.Sexo;
import br.com.browseframeworksample.domain.enums.TipoPessoa;

@Audited
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "pessoa_fisica")
public class PessoaFisica extends Pessoa {

	private static final long serialVersionUID = -3766293167697387317L;

	public PessoaFisica() {
		super(TipoPessoa.F);
	}

	@Column(name = "IND_SEXO", nullable = true, length = 1)
	@Enumerated(EnumType.STRING)
	private Sexo sexo;
	
	@Column(name = "DAT_NASCIMENTO", nullable = true, length = 23)
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;
	
	@Column(name = "IND_MEMBRO_EQUIPE", nullable = false, length = 1)
	private Boolean membroEquipe;
	
	// GETTERS && SETTERS 
	
	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Boolean getMembroEquipe() {
		return membroEquipe;
	}

	public void setMembroEquipe(Boolean membroEquipe) {
		this.membroEquipe = membroEquipe;
	}

}
