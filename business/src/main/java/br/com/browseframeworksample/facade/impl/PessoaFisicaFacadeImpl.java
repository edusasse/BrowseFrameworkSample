package br.com.browseframeworksample.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.browseframeworksample.dao.PessoaFisicaDAO;
import br.com.browseframeworksample.domain.PessoaFisica;
import br.com.browseframeworksample.facade.PessoaFisicaFacade;
import br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl;

@Service(value = "pessoaFisicaFacade")
@Transactional(rollbackFor = Throwable.class)
public class PessoaFisicaFacadeImpl extends CrudFacadeImpl<Long, PessoaFisica> implements PessoaFisicaFacade {
 
	@Autowired
	public PessoaFisicaFacadeImpl(PessoaFisicaDAO dao) {
		super(dao); 
	}
		
}