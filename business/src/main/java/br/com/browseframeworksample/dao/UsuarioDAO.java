package br.com.browseframeworksample.dao;

import java.util.List;

import br.com.browseframeworksample.domain.Usuario;
import br.com.browseframework.base.crud.dao.CrudDAO;

public interface UsuarioDAO extends CrudDAO<Long, Usuario> {

	/**
	 * Obtém os usuários com o perfil Clipping associado.
	 * 
	 * @return
	 */
	public List<Usuario> findUsuariosClipping();
	
}