package br.com.browseframeworksample.domain;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.browseframework.base.data.dto.BaseDTO;

/**
 * Classe do Usuário ficou levemente acoplada ao Spring Security.
 * 
 * @author Eduardo
 *
 */

@Audited
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "usuario")
public class Usuario extends BaseDTO<Long> implements UserDetails {

	private static final long serialVersionUID = 6415664248282463176L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cod_usuario")
	private Long id;
	
	@Column(name = "des_apelido", nullable=false)
	private String apelido;
	
	@Column(name = "des_senha")
	private String senha;

	@Column(name = "ind_ativo")
	private boolean ativo;

	@ManyToMany(fetch = FetchType.LAZY)
	@Fetch(FetchMode.SELECT)
	@JoinTable(name = "usuario_perfil", joinColumns = @JoinColumn(name = "cod_usuario"), inverseJoinColumns = @JoinColumn(name = "cod_perfil"))
	private List<Perfil> perfis = new ArrayList<Perfil>();

	@JoinColumn(name = "COD_PESSOA", nullable = false)
	@ManyToOne
	private PessoaFisica pessoaFisica;

	@Column(name = "DAT_ULTIMO_ACESSO", nullable = true, length = 23)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltimoAcesso;

	@Column(name = "DAT_ACESSO_ANTERIOR", nullable = true, length = 23)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAcessoAnterior;
	
	@Version
	@Column(name = "nro_versao", nullable = false, length = 10)
	private Long versao;
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities()
	 */
	@Transient
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> lista = new ArrayList<GrantedAuthority>();
		for(Perfil perfil : getPerfis()) {
			lista.add(new SimpleGrantedAuthority(perfil.getAuthority()));
		}
		return lista;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getPassword()
	 */
	@Transient
	@Override
	public String getPassword() {
		return this.senha;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getUsername()
	 */
	@Transient
	@Override
	public String getUsername() {
		return this.apelido;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
	 */
	@Transient
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
	 */
	@Transient
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
	 */
	@Transient
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#isEnabled()
	 */
	@Transient
	@Override
	public boolean isEnabled() {
		return this.ativo;
	}

	// GETTERS && SETTERS 

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApelido() {
		return apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public List<Perfil> getPerfis() {
		return perfis;
	}

	public void setPerfis(List<Perfil> perfis) {
		this.perfis = perfis;
	}

	public Long getVersao() {
		return versao;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
	}

	public PessoaFisica getPessoaFisica() {
		return pessoaFisica;
	}

	public void setPessoaFisica(PessoaFisica pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}

	public Date getDataUltimoAcesso() {
		return dataUltimoAcesso;
	}

	public void setDataUltimoAcesso(Date dataUltimoAcesso) {
		this.dataUltimoAcesso = dataUltimoAcesso;
	}

	public Date getDataAcessoAnterior() {
		return dataAcessoAnterior;
	}

	public void setDataAcessoAnterior(Date dataAcessoAnterior) {
		this.dataAcessoAnterior = dataAcessoAnterior;
	}
 
}