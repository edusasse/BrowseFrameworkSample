package br.com.browseframeworksample.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.browseframeworksample.dao.PerfilDAO;
import br.com.browseframeworksample.domain.Perfil;
import br.com.browseframeworksample.facade.PerfilFacade;
import br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl;

@Service(value = "perfilFacade")
@Transactional(rollbackFor = Throwable.class)
public class PerfilFacadeImpl extends CrudFacadeImpl<Long, Perfil> implements PerfilFacade {
 
	@Autowired
	public PerfilFacadeImpl(PerfilDAO dao) {
		super(dao); 
	}

}