package br.com.browseframeworksample.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.browseframeworksample.dao.PessoaJuridicaDAO;
import br.com.browseframeworksample.domain.PessoaJuridica;
import br.com.browseframeworksample.facade.PessoaJuridicaFacade;
import br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl;

@Service(value = "pessoaJuridicaFacade")
@Transactional(rollbackFor = Throwable.class)
public class PessoaJuridicaFacadeImpl extends CrudFacadeImpl<Long, PessoaJuridica> implements PessoaJuridicaFacade {
 
	@Autowired
	public PessoaJuridicaFacadeImpl(PessoaJuridicaDAO dao) {
		super(dao); 
	}
		
}