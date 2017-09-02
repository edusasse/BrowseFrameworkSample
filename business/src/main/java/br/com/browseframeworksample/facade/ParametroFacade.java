package br.com.browseframeworksample.facade;

import br.com.browseframeworksample.domain.Parametro;
import br.com.browseframework.base.crud.facade.CrudFacade;

public interface ParametroFacade extends CrudFacade<Long, Parametro> {

	/**
	 * Recupera o parametro pela descriçao.
	 * @param nomeParametro
	 * @return
	 */
	public Parametro findByNome(String nomeParametro);
	
}