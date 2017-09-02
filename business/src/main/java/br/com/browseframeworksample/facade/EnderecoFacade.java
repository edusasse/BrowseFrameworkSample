package br.com.browseframeworksample.facade;

import java.util.List;

import br.com.browseframeworksample.domain.Endereco;
import br.com.browseframeworksample.domain.Pessoa;
import br.com.browseframework.base.crud.facade.CrudFacade;

public interface EnderecoFacade extends CrudFacade<Long, Endereco> {
	
	/**
	 * Rela��o de endere�os para 1 pessoa.
	 * @param p
	 * @return
	 */
	public List<Endereco> findByPessoa(Pessoa p);

}