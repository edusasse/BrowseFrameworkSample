package br.com.browseframeworksample.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "telefone")
public class Telefone extends BaseDTO<Long> {

	private static final long serialVersionUID = -3971572795955690800L;

	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "COD_TELEFONE", length = 11)
	private Long id;

	@Column(name = "NRO_TELEFONE", nullable = false, length = 15)
	private String numero;
	
	@Column(name = "NOM_PESSOA_CONTATO", nullable = true, length = 45)
	private String contato;
	
	@JoinColumn(name = "COD_TIPO_TELEFONE", nullable = false)
	@ManyToOne
	private TipoTelefone tipoTelefone;
	
	@JoinColumn(name = "COD_PESSOA", nullable = false)
	@ManyToOne
	private Pessoa pessoa;

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
	
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getContato() {
		return contato;
	}

	public void setContato(String contato) {
		this.contato = contato;
	}

	public TipoTelefone getTipoTelefone() {
		return tipoTelefone;
	}

	public void setTipoTelefone(TipoTelefone tipoTelefone) {
		this.tipoTelefone = tipoTelefone;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Long getVersao() {
		return versao;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
	}
	
}
