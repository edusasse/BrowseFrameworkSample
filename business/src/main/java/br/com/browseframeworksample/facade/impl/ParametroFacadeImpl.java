package br.com.browseframeworksample.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.browseframeworksample.dao.ParametroDAO;
import br.com.browseframeworksample.domain.Parametro;
import br.com.browseframeworksample.facade.ParametroFacade;
import br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl;
import br.com.browseframework.base.data.Filter;
import br.com.browseframework.base.data.type.CriterionType;

@Service(value = "parametroFacade")
@Transactional(rollbackFor = Throwable.class)
public class ParametroFacadeImpl extends CrudFacadeImpl<Long, Parametro> implements ParametroFacade {
 
	@Autowired
	public ParametroFacadeImpl(ParametroDAO dao) {
		super(dao); 
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.facade.ParametroFacade#findByNome(java.lang.String)
	 */
	public Parametro findByNome(String nomeParametro){
		return findAll(null, new CriterionType[]{new Filter("descricao", nomeParametro)}).getFirst();
	}
		
}