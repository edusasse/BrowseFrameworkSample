package br.com.browseframeworksample.facade;

import java.util.List;

import br.com.browseframeworksample.domain.Pessoa;
import br.com.browseframeworksample.domain.Telefone;
import br.com.browseframework.base.crud.facade.CrudFacade;

public interface TelefoneFacade extends CrudFacade<Long, Telefone> {

	/**
	 * Retorna a lista de telefones para 1 pessoa.
	 * @param p
	 * @return
	 */
	public List<Telefone> findByPessoa(Pessoa p);
}