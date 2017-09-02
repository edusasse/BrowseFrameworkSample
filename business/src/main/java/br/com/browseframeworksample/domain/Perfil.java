package br.com.browseframeworksample.domain;

import java.util.ArrayList;
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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;

import br.com.browseframework.base.data.dto.BaseDTO;

/**
 * Classe do Perfil ficou levemente acoplada ao Spring Security.
 * 
 * @author Eduardo
 *
 */

@Audited
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "perfil")
public class Perfil extends BaseDTO<Long> implements GrantedAuthority {

	private static final long serialVersionUID = -1053027511737139723L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cod_perfil")
	private Long id;
	
	@Column(name = "des_nome")
	private String nome;
	
	@Column(name = "des_role")
	private String role;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@Cascade(value=CascadeType.SAVE_UPDATE)
	@Fetch(FetchMode.SELECT)
	@JoinTable(name = "usuario_perfil", joinColumns = @JoinColumn(name = "cod_perfil"), inverseJoinColumns = @JoinColumn(name = "cod_usuario"))
	private List<Usuario> usuarios = new ArrayList<Usuario>();
	
	@Version
	@Column(name = "nro_versao", nullable = false, length = 10)
	private Long versao;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.GrantedAuthority#getAuthority()
	 */
	@Transient
	@Override
	public String getAuthority() {
		return this.role;
	}
	
	// GETTERS && SETTERS

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Long getVersao() {
		return versao;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
	}
  
}