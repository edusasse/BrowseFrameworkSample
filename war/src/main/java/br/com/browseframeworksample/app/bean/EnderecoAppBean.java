package br.com.browseframeworksample.app.bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.browseframeworksample.app.GenericAppBean;
import br.com.browseframeworksample.app.enums.AplicacaoType;
import br.com.browseframeworksample.app.enums.DiretoriosApp;
import br.com.browseframeworksample.domain.Endereco;
import br.com.browseframeworksample.domain.Pessoa;
import br.com.browseframeworksample.facade.EnderecoFacade;
import br.com.browseframework.base.crud.facade.CrudFacade;
import br.com.browseframework.base.exception.GenericBusinessException;
import br.com.browseframework.jsfprimefaces.util.FacesUtil;

@ManagedBean(name = "enderecoAppBean")
@ViewScoped
public class EnderecoAppBean extends GenericAppBean<Endereco> {
    // Acesso ao Facade do classificacao
    @ManagedProperty(name = "enderecoFacade", value = "#{enderecoFacade}")
	private EnderecoFacade enderecoFacade;
    
    public EnderecoAppBean() {
		super("endereco", DiretoriosApp.SECURED_APPS,  Endereco.class);
	}
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.GenericAppBean#getFacade()
     */
    @SuppressWarnings("rawtypes")
	@Override
	public CrudFacade getFacade() {
		return getEnderecoFacade();
	}
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.GenericAppBean#doCarregar(br.com.browseframeworksample.app.enums.AplicacaoType)
     */
    public void doCarregar(AplicacaoType aplicacaoType){
    	super.doCarregar(aplicacaoType);
    	if (AplicacaoType.CRUD.equals(aplicacaoType)){
    		// Parametro passado é a pessoa do endereço
    		final Pessoa pessoa = (Pessoa) getParametro();
    		if (pessoa == null){
    			throw new GenericBusinessException(FacesUtil.getResourceBundleString("msgs", "app.endereco.passagemParametro"));
    		}
    		// Procura se ja existe um endereco cadastrado para essa pessoa
    		final List<Endereco> listEnderecos = getEnderecoFacade().findByPessoa(pessoa);
    		if (listEnderecos != null && listEnderecos.size() >= 1){
    			setListDto(listEnderecos);
    		} else {
    			setListDto(new ArrayList<Endereco>());
    			// Cria um novo
    			doNovo();
    			// Carrega a pessoa no objeto
    			getDto().setPessoa(pessoa);
    		}
    	}
    }
    
    @Override
    public void doNovo() {
    	super.doNovo();
		// Parametro passado é a pessoa do endereço
		final Pessoa pessoa = (Pessoa) getParametro();
		if (pessoa != null){
			getDto().setPessoa(pessoa);
		}
    }
    
    // GETTERS && SETTERS 
     
	public EnderecoFacade getEnderecoFacade() {
		return enderecoFacade;
	}

	public void setEnderecoFacade(
			EnderecoFacade enderecoFacade) {
		this.enderecoFacade = enderecoFacade;
	}
	
}