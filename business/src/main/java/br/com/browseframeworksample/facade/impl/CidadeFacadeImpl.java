package br.com.browseframeworksample.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.browseframeworksample.dao.CidadeDAO;
import br.com.browseframeworksample.domain.Cidade;
import br.com.browseframeworksample.facade.CidadeFacade;
import br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl;

@Service(value = "cidadeFacade")
@Transactional(rollbackFor = Throwable.class)
public class CidadeFacadeImpl extends CrudFacadeImpl<Long, Cidade> implements CidadeFacade {
 
	@Autowired
	public CidadeFacadeImpl(CidadeDAO dao) {
		super(dao); 
	}
		
}