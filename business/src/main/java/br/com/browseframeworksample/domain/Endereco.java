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
@Table(name = "endereco")
public class Endereco extends BaseDTO<Long> {

	private static final long serialVersionUID = 6902574962022833358L;

	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "COD_ENDERECO", length = 11)
	private Long id;

	@JoinColumn(name = "COD_TIPO_ENDERECO", nullable = false)
	@ManyToOne
	private TipoEndereco tipoEndereco;

	@JoinColumn(name = "COD_CIDADE", nullable = false)
	@ManyToOne
	private Cidade cidade;

	@JoinColumn(name = "COD_PESSOA", nullable = false)
	@ManyToOne
	private Pessoa pessoa;

	@Column(name = "NRO_CEP", nullable = false, length = 8)
	private Long cep;

	@Column(name = "DES_LOGRADOURO", nullable = false, length = 70)
	private String logradouro;

	@Column(name = "NRO_LOGRADOURO", nullable = false, scale = 5, precision = 0)
	private Long numero;

	@Column(name = "DES_BAIRRO", nullable = false, length = 60)
	private String bairro;

	@Column(name = "DES_COMPLEMENTO", nullable = true, length = 120)
	private String complemento;
	
	@Version
	@Column(name = "NRO_VERSAO", nullable = false, length = 11)
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

	public TipoEndereco getTipoEndereco() {
		return tipoEndereco;
	}

	public void setTipoEndereco(TipoEndereco tipoEndereco) {
		this.tipoEndereco = tipoEndereco;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Long getCep() {
		return cep;
	}

	public void setCep(Long cep) {
		this.cep = cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public Long getVersao() {
		return versao;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

}
