package br.com.browseframeworksample.facade.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl;
import br.com.browseframework.base.data.Filter;
import br.com.browseframework.base.data.type.CriterionType;
import br.com.browseframework.base.data.type.DataPageType;
import br.com.browseframework.base.data.type.FilterType;
import br.com.browseframework.base.data.type.PageType;
import br.com.browseframework.base.exception.GenericBusinessException;
import br.com.browseframeworksample.dao.ClienteDAO;
import br.com.browseframeworksample.domain.Cliente;
import br.com.browseframeworksample.domain.Pessoa;
import br.com.browseframeworksample.domain.Usuario;
import br.com.browseframeworksample.facade.ClienteFacade;

@Service(value = "clienteFacade")
@Transactional(rollbackFor = Throwable.class)
public class ClienteFacadeImpl extends CrudFacadeImpl<Long, Cliente> implements ClienteFacade {
 
	@Autowired
	public ClienteFacadeImpl(ClienteDAO dao) {
		super(dao); 
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.facade.ClienteFacade#findByPessoa(br.com.browseframeworksample.domain.Pessoa)
	 */
	public Cliente findByPessoa(Pessoa p){
		Cliente retorno = null;
		if (p != null && !p.isNew()){
			final FilterType f1 = new Filter();
			f1.setPropertyName("pessoa.id");
			f1.setPropertyValue(p.getId());
			
			retorno = findAll(null, new CriterionType[]{f1}).getFirst();
		}
		
		return retorno;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl#save(br.com.browseframework.base.data.dto.BaseDTO)
	 */
	@Override
	public Cliente save(Cliente dto) {
		Cliente retorno = null;
		if (dto != null){
			// Caso tenha a pessoa 
			if (dto.getPessoa() != null){
				// .. obtem o cliente para esta pessoa
				final Cliente cliente = findByPessoa(dto.getPessoa());
				// .. caso tenha encontrado
				if (cliente != null){
					// .. se o Cliente que esta sendo cadastrado for novo avisa que ja existe um cliente
					if (dto.isNew()){
						throw new GenericBusinessException("Já existe o Cliente Id [" + cliente.getId() + "] cadastrado para esta Pessoa!");
					} else if (!dto.equals(cliente)){ // caso o cliente encontrado for diferente do cliente em edição também avisa que ja existe um cliente para esta pessoa
						throw new GenericBusinessException("Não é possível atribuir a pessoa informada para este Cliente pois ela já esta relacionada ao Cliente Id [" + cliente.getId() + "]");
					}
				}
			} else {
				throw new GenericBusinessException("Pessoa não informada no Cliente. Informe antes de prosseguir!");
			}
		}
		retorno = super.save(dto);
		
		return retorno;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.facade.ClienteFacade#findClientesByPessoa(br.com.browseframework.base.data.type.PageType, br.com.browseframework.base.data.type.CriterionType[])
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public DataPageType<Cliente> findClientesByPessoa(PageType page, CriterionType[] criteria) {
		return ((ClienteDAO) crudDAO).findClientesByPessoa(page, criteria);
	}
	
}