package br.com.browseframeworksample.facade.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.browseframeworksample.dao.UsuarioDAO;
import br.com.browseframeworksample.domain.PessoaFisica;
import br.com.browseframeworksample.domain.Usuario;
import br.com.browseframeworksample.facade.UsuarioFacade;
import br.com.browseframework.base.crud.facade.impl.CrudFacadeImpl;
import br.com.browseframework.base.data.Criterion;
import br.com.browseframework.base.data.Filter;
import br.com.browseframework.base.data.enums.Restriction;
import br.com.browseframework.base.data.type.CriterionType;
import br.com.browseframework.base.data.type.DataPageType;
import br.com.browseframework.base.data.type.FilterType;
import br.com.browseframework.base.exception.GenericBusinessException;

@Service(value = "usuarioFacade")
@Transactional(rollbackFor = Throwable.class)
public class UsuarioFacadeImpl extends CrudFacadeImpl<Long, Usuario> implements UsuarioFacade {
 
	@Autowired
	public UsuarioFacadeImpl(UsuarioDAO dao) {
		super(dao); 
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.facade.UsuarioFacade#findByApelido(java.lang.String)
	 */
	@Override
	public Usuario findByApelido(String apelido){
		final Filter f = new Filter();
		f.setPropertyName("apelido");
		f.setPropertyValue(apelido);
		f.setRestriction(Restriction.EQUALS);
		
		final DataPageType<Usuario> dpUsuario = findAll(null, new Criterion[]{f});
		
		if (dpUsuario.getData().size() > 1){
			throw new GenericBusinessException("Foram encontrados [" + dpUsuario.getData().size() + "] usuários para o apelido [" + apelido + "]. Favor verificar a integridade da base.");
		}
		if (dpUsuario.getData().isEmpty()){
			throw new GenericBusinessException("Não foi possível localizar um usuário para o apelido [" + apelido + "]");
		}
		
		return dpUsuario.getData().get(0);
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.facade.ClienteFacade#findByPessoa(br.com.browseframeworksample.domain.Pessoa)
	 */
	public Usuario findByPessoaFisica(PessoaFisica p){
		Usuario retorno = null;
		if (p != null && !p.isNew()){
			final FilterType f1 = new Filter();
			f1.setPropertyName("pessoaFisica.id");
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
	public Usuario save(Usuario dto) {
		Usuario retorno = null;
		if (dto != null){
			// Caso tenha a pessoa 
			if (dto.getPessoaFisica() != null){
				// .. obtem o usuario para esta pessoa
				final Usuario usuario = findByPessoaFisica(dto.getPessoaFisica());
				// .. caso tenha encontrado
				if (usuario != null){
					// .. se o usuario que esta sendo cadastrado for novo avisa que ja existe um usuario
					if (dto.isNew()){
						throw new GenericBusinessException("Já existe o Usuário [" + usuario.getApelido() + "] cadastrado para esta Pessoa!");
					} else if (!dto.equals(usuario)){ // caso o usuario encontrado for diferente do cliente em edição também avisa que ja existe um cliente para esta pessoa
						throw new GenericBusinessException("Não é possível atribuir a pessoa informada para este Usuário pois ela já esta relacionada ao Usuário [" + usuario.getApelido() + "]");
					}
				}
			} else {
				throw new GenericBusinessException("Pessoa não informada no Usuário. Informe antes de prosseguir!");
			}
		}
		retorno = super.save(dto);
		
		return retorno;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.facade.UsuarioFacade#findUsuariosClipping()
	 */
	public List<Usuario> findUsuariosClipping(){
		return ((UsuarioDAO) crudDAO).findUsuariosClipping();
	}
}