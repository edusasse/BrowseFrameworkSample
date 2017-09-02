package br.com.browseframeworksample.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.browseframeworksample.dao.TipoEnderecoDAO;
import br.com.browseframeworksample.domain.TipoEndereco;
import br.com.browseframeworksample.facade.TipoEnderecoFacade;
import br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl;

@Service(value = "tipoEnderecoFacade")
@Transactional(rollbackFor = Throwable.class)
public class TipoEnderecoFacadeImpl extends CrudFacadeImpl<Long, TipoEndereco> implements TipoEnderecoFacade {
 
	@Autowired
	public TipoEnderecoFacadeImpl(TipoEnderecoDAO dao) {
		super(dao); 
	}
		
}