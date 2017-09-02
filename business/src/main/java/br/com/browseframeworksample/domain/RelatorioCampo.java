package br.com.browseframeworksample.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import br.com.browseframeworksample.domain.enums.TipoCampo;
import br.com.browseframework.base.data.dto.BaseDTO;

@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "relatorio_campo")
public class RelatorioCampo extends BaseDTO<Long> {

	private static final long serialVersionUID = 1925191939407290780L;

	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "cod_relatorio_campo", length = 8)
	private Long id;

	@JoinColumn(name = "cod_relatorio", nullable = true)
	@Fetch(FetchMode.JOIN)
	@ManyToOne(fetch = FetchType.LAZY)
	private Relatorio relatorio;

	@Column(name = "nom_campo", nullable = false, length = 50)
	private String nomeCampo;

	@Column(name = "ind_tipo_campo", nullable = false, length = 3)
	@Enumerated(EnumType.STRING)
	private TipoCampo tipoCampo;

	@Column(name = "des_mascara", nullable = true, length = 120)
	private String mascara;
 
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

	public Relatorio getRelatorio() {
		return relatorio;
	}

	public void setRelatorio(Relatorio relatorio) {
		this.relatorio = relatorio;
	}

	public String getNomeCampo() {
		return nomeCampo;
	}

	public void setNomeCampo(String nomeCampo) {
		this.nomeCampo = nomeCampo;
	}

	public TipoCampo getTipoCampo() {
		return tipoCampo;
	}

	public void setTipoCampo(TipoCampo tipoCampo) {
		this.tipoCampo = tipoCampo;
	}

	public String getMascara() {
		return mascara;
	}

	public void setMascara(String mascara) {
		this.mascara = mascara;
	}

	public java.lang.Long getVersao() {
		return versao;
	}

	public void setVersao(java.lang.Long versao) {
		this.versao = versao;
	}

}