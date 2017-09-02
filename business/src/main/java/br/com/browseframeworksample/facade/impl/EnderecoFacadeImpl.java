package br.com.browseframeworksample.facade.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.browseframeworksample.dao.EnderecoDAO;
import br.com.browseframeworksample.domain.Endereco;
import br.com.browseframeworksample.domain.Pessoa;
import br.com.browseframeworksample.facade.EnderecoFacade;
import br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl;
import br.com.browseframework.base.data.Filter;
import br.com.browseframework.base.data.type.CriterionType;
import br.com.browseframework.base.data.type.FilterType;

@Service(value = "enderecoFacade")
@Transactional(rollbackFor = Throwable.class)
public class EnderecoFacadeImpl extends CrudFacadeImpl<Long, Endereco> implements EnderecoFacade {
 
	@Autowired
	public EnderecoFacadeImpl(EnderecoDAO dao) {
		super(dao); 
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.facade.EnderecoFacade#findByPessoa(br.com.browseframeworksample.domain.Pessoa)
	 */
	public List<Endereco> findByPessoa(Pessoa p){
		List<Endereco> retorno = null;
		if (p != null && !p.isNew()){
			final FilterType f1 = new Filter();
			f1.setPropertyName("pessoa.id");
			f1.setPropertyValue(p.getId());
			
			retorno = findAll(null, new CriterionType[]{f1}).getData();
		}
		
		return retorno;
	}
}