package br.com.browseframeworksample.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.browseframeworksample.dao.TipoTelefoneDAO;
import br.com.browseframeworksample.domain.TipoTelefone;
import br.com.browseframeworksample.facade.TipoTelefoneFacade;
import br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl;

@Service(value = "tipoTelefoneFacade")
@Transactional(rollbackFor = Throwable.class)
public class TipoTelefoneFacadeImpl extends CrudFacadeImpl<Long, TipoTelefone> implements TipoTelefoneFacade {
 
	@Autowired
	public TipoTelefoneFacadeImpl(TipoTelefoneDAO dao) {
		super(dao); 
	}
		
}