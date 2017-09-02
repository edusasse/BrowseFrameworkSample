package br.com.browseframeworksample.facade;

import java.util.List;

import br.com.browseframeworksample.domain.PessoaFisica;
import br.com.browseframeworksample.domain.Usuario;
import br.com.browseframework.base.crud.facade.CrudFacade;

public interface UsuarioFacade extends CrudFacade<Long, Usuario> {

	/**
	 * Encontra um usuário a partir de ser apelido(login).
	 * @param apelido
	 * @return
	 */
	public Usuario findByApelido(String apelido);
	
	/**
	 * Encontra o usuário pela pessoa. Relação 1:1.
	 * @param p
	 * @return
	 */
	public Usuario findByPessoaFisica(PessoaFisica p);
	
	/**
	 * Obtém os usuários com o perfil Clipping associado.
	 * 
	 * @return
	 */
	public List<Usuario> findUsuariosClipping();
}