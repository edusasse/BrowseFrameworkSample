package br.com.browseframeworksample.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.browseframeworksample.dao.PessoaDAO;
import br.com.browseframeworksample.domain.Pessoa;
import br.com.browseframeworksample.facade.PessoaFacade;
import br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl;

@Service(value = "pessoaFacade")
@Transactional(rollbackFor = Throwable.class)
public class PessoaFacadeImpl extends CrudFacadeImpl<Long, Pessoa> implements PessoaFacade {
 
	@Autowired
	public PessoaFacadeImpl(PessoaDAO dao) {
		super(dao); 
	}
		
}