package br.com.browseframeworksample.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.browseframeworksample.dao.ClassificacaoDAO;
import br.com.browseframeworksample.domain.Classificacao;
import br.com.browseframeworksample.facade.ClassificacaoFacade;
import br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl;

@Service(value = "classificacaoFacade")
@Transactional(rollbackFor = Throwable.class)
public class ClassificacaoFacadeImpl extends CrudFacadeImpl<Long, Classificacao> implements ClassificacaoFacade {
 
	@Autowired
	public ClassificacaoFacadeImpl(ClassificacaoDAO dao) {
		super(dao); 
	}
		
}