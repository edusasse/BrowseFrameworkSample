package br.com.browseframeworksample.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.browseframeworksample.dao.UnidadeFederacaoDAO;
import br.com.browseframeworksample.domain.UnidadeFederacao;
import br.com.browseframeworksample.facade.UnidadeFederacaoFacade;
import br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl;

@Service(value = "unidadeFederacaoFacade")
@Transactional(rollbackFor = Throwable.class)
public class UnidadeFederacaoFacadeImpl extends CrudFacadeImpl<Long, UnidadeFederacao> implements UnidadeFederacaoFacade {
 
	@Autowired
	public UnidadeFederacaoFacadeImpl(UnidadeFederacaoDAO dao) {
		super(dao); 
	}
		
}