package br.com.browseframeworksample.facade;

import java.util.List;
import java.util.Map;

import br.com.browseframework.base.crud.facade.CrudFacade;
import br.com.browseframework.base.data.type.CriterionType;
import br.com.browseframework.base.data.type.DataPageType;
import br.com.browseframework.base.data.type.PageType;
import br.com.browseframeworksample.domain.Cliente;
import br.com.browseframeworksample.domain.Pessoa;
import br.com.browseframeworksample.domain.Usuario;

public interface ClienteFacade extends CrudFacade<Long, Cliente> {

	/**
	 * Encontra o cliente pela pessoa. Relação 1:1.
	 * @param p
	 * @return
	 */
	public Cliente findByPessoa(Pessoa p);
	
	public DataPageType<Cliente> findClientesByPessoa(PageType page, CriterionType[] criteria);
	
}