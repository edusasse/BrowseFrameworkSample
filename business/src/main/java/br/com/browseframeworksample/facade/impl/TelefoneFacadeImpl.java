package br.com.browseframeworksample.facade.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.browseframeworksample.dao.TelefoneDAO;
import br.com.browseframeworksample.domain.Pessoa;
import br.com.browseframeworksample.domain.Telefone;
import br.com.browseframeworksample.facade.TelefoneFacade;
import br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl;
import br.com.browseframework.base.data.Filter;
import br.com.browseframework.base.data.type.CriterionType;
import br.com.browseframework.base.data.type.FilterType;

@Service(value = "telefoneFacade")
@Transactional(rollbackFor = Throwable.class)
public class TelefoneFacadeImpl extends CrudFacadeImpl<Long, Telefone> implements TelefoneFacade {
 
	@Autowired
	public TelefoneFacadeImpl(TelefoneDAO dao) {
		super(dao); 
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.facade.TelefoneFacade#findByPessoa(br.com.browseframeworksample.domain.Pessoa)
	 */
	public List<Telefone> findByPessoa(Pessoa p){
		List<Telefone> retorno = null;
		if (p != null && !p.isNew()){
			final FilterType f1 = new Filter();
			f1.setPropertyName("pessoa.id");
			f1.setPropertyValue(p.getId());
			
			retorno = findAll(null, new CriterionType[]{f1}).getData();
		}
		
		return retorno;
	}
}