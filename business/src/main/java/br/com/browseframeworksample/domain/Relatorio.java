package br.com.browseframeworksample.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import br.com.browseframework.base.data.dto.BaseDTO;
  
@Audited
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "relatorio")
public class Relatorio extends BaseDTO<Long> {

	private static final long serialVersionUID = 1925191939407290780L;

	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "cod_relatorio", length = 8)
	private Long id;

	@Column(name = "nom_relatorio", nullable = false, length=255)
	private String nome;

	@Column(name = "des_select", nullable = false)
	@Type(type="text")
	private String query;

	@Column(name = "des_select_order_by", nullable = true)
	@Type(type="text")
	private String orderBy;
	
	@Column(name = "dat_inicio_vigencia", nullable = true, length = 23)
	private java.util.Date dataVigenciaInicial;

	@Column(name = "dat_fim_vigencia", nullable = true, length = 23)
	private java.util.Date dataVigenciaFinal;
	
	@Column(name = "ind_ativo", nullable = false, length = 1)
	private boolean ativo;
	
	@OneToMany(mappedBy = "relatorio", cascade = javax.persistence.CascadeType.ALL, fetch = FetchType.LAZY)
	@Fetch(FetchMode.SELECT)
	private List<RelatorioParametro> parametros;

	@OneToMany(mappedBy = "relatorio", cascade = javax.persistence.CascadeType.ALL, fetch = FetchType.LAZY)
	@Fetch(FetchMode.SELECT)
	@OrderBy(clause="cod_relatorio_campo")
	private List<RelatorioCampo> campos;

	@Version
	@Column(name = "nro_versao", nullable = false, length = 10)
	private java.lang.Long versao;

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

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public java.util.Date getDataVigenciaInicial() {
		return dataVigenciaInicial;
	}

	public void setDataVigenciaInicial(java.util.Date dataVigenciaInicial) {
		this.dataVigenciaInicial = dataVigenciaInicial;
	}

	public java.util.Date getDataVigenciaFinal() {
		return dataVigenciaFinal;
	}

	public void setDataVigenciaFinal(java.util.Date dataVigenciaFinal) {
		this.dataVigenciaFinal = dataVigenciaFinal;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public List<RelatorioParametro> getParametros() {
		return parametros;
	}

	public void setParametros(List<RelatorioParametro> parametros) {
		this.parametros = parametros;
	}

	public List<RelatorioCampo> getCampos() {
		return campos;
	}

	public void setCampos(List<RelatorioCampo> campos) {
		this.campos = campos;
	}

	public java.lang.Long getVersao() {
		return versao;
	}

	public void setVersao(java.lang.Long versao) {
		this.versao = versao;
	}
}