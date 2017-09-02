package br.com.browseframeworksample.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import br.com.browseframework.base.data.dto.BaseDTO;
import br.com.browseframeworksample.domain.enums.SituacaoCliente;
import br.com.browseframeworksample.domain.to.ClienteTO;

@Audited
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "cliente")
public class Cliente extends BaseDTO<Long> {

	private static final long serialVersionUID = -4263066890453564804L;

	public Cliente() {
		setFechamentoMensal(false);
	}
	
	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "COD_CLIENTE", length = 11)
	private Long id;

	@Column(name = "NOM_CONTATO", nullable = true, length = 85)
	private String contato;

	@Column(name = "IND_SITUACAO", nullable = false, length = 1)
	@Enumerated(EnumType.STRING)
	private SituacaoCliente situacao;

	@JoinColumn(name = "COD_PESSOA", nullable = false)
	@ManyToOne
	private Pessoa pessoa;

	@JoinColumn(name = "COD_PESSOA_RESPONSAVEL_CONTA", nullable = false)
	@ManyToOne
	private Pessoa pessoaResponsavelConta;

	@Column(name = "DES_OBSERVACAO", nullable = true, length = 500)
	private String observacao;

	@ManyToMany(fetch = FetchType.LAZY)
	@Fetch(FetchMode.SELECT)
	@JoinTable(name = "equipe_cliente", joinColumns = { @JoinColumn(name = "COD_CLIENTE") }, inverseJoinColumns = { @JoinColumn(name = "COD_PESSOA") })
	private List<PessoaFisica> equipe;

	@ManyToMany(fetch = FetchType.LAZY)
	@Fetch(FetchMode.SELECT)
	@JoinTable(name = "usuario_cliente", joinColumns = { @JoinColumn(name = "COD_CLIENTE") }, inverseJoinColumns = { @JoinColumn(name = "COD_USUARIO") })
	private List<Usuario> usuarios;

	@ManyToMany(fetch = FetchType.LAZY)
	@Fetch(FetchMode.SELECT)
	@JoinTable(name = "usuario_cliente_clipping", joinColumns = { @JoinColumn(name = "COD_CLIENTE") }, inverseJoinColumns = { @JoinColumn(name = "COD_USUARIO") })
	private List<Usuario> usuariosClipping;
	
	@Column(name = "DAT_ABERTURA", nullable = true, length = 23)
	@Temporal(TemporalType.DATE)
	private Date dataAbertura;

	@Column(name = "QTD_META_ACOES", nullable = true)
	private Long metaAcoes;

	@Column(name = "IND_PERIODO_FECHAMENTO_MENSAL", nullable = false)
	private boolean fechamentoMensal;

	@Version
	@Column(name = "NRO_VERSAO", nullable = false, length = 10)
	private Long versao;

	// Transfer Object
	@Transient
	private ClienteTO to = new ClienteTO();
	
	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	// GETTERS && SETTERS

	public String getContato() {
		return contato;
	}

	public void setContato(String contato) {
		this.contato = contato;
	}

	public SituacaoCliente getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoCliente situacao) {
		this.situacao = situacao;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Pessoa getPessoaResponsavelConta() {
		return pessoaResponsavelConta;
	}

	public void setPessoaResponsavelConta(Pessoa pessoaResponsavelConta) {
		this.pessoaResponsavelConta = pessoaResponsavelConta;
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

	public List<PessoaFisica> getEquipe() {
		return equipe;
	}

	public void setEquipe(List<PessoaFisica> equipe) {
		this.equipe = equipe;
	}
 

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Long getMetaAcoes() {
		return metaAcoes;
	}

	public void setMetaAcoes(Long metaAcoes) {
		this.metaAcoes = metaAcoes;
	}

	public Date getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(Date dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public boolean isFechamentoMensal() {
		return fechamentoMensal;
	}

	public void setFechamentoMensal(boolean fechamentoMensal) {
		this.fechamentoMensal = fechamentoMensal;
	}

	public ClienteTO getTo() {
		return to;
	}

	public void setTo(ClienteTO to) {
		this.to = to;
	}

	public List<Usuario> getUsuariosClipping() {
		return usuariosClipping;
	}

	public void setUsuariosClipping(List<Usuario> usuariosClipping) {
		this.usuariosClipping = usuariosClipping;
	}

}