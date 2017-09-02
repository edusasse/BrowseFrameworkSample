package br.com.browseframeworksample.app.bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.browseframeworksample.app.GenericAppBean;
import br.com.browseframeworksample.app.enums.AplicacaoType;
import br.com.browseframeworksample.app.enums.DiretoriosApp;
import br.com.browseframeworksample.domain.Pessoa;
import br.com.browseframeworksample.domain.Telefone;
import br.com.browseframeworksample.facade.TelefoneFacade;
import br.com.browseframework.base.crud.facade.CrudFacade;
import br.com.browseframework.base.exception.GenericBusinessException;
import br.com.browseframework.jsfprimefaces.util.FacesUtil;

@ManagedBean(name = "telefoneAppBean")
@ViewScoped
public class TelefoneAppBean extends GenericAppBean<Telefone> {
    // Acesso ao Facade do classificacao
    @ManagedProperty(name = "telefoneFacade", value = "#{telefoneFacade}")
	private TelefoneFacade telefoneFacade;
    
    public TelefoneAppBean() {
		super("telefone", DiretoriosApp.SECURED_APPS,  Telefone.class);
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
    		final List<Telefone> listTelefones = getTelefoneFacade().findByPessoa(pessoa);
    		if (listTelefones != null && listTelefones.size() >= 1){
    			setListDto(listTelefones);
    		} else {
    			setListDto(new ArrayList<Telefone>());
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
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.GenericAppBean#getFacade()
     */
    @SuppressWarnings("rawtypes")
	@Override
	public CrudFacade getFacade() {
		return getTelefoneFacade();
	}
    
    // GETTERS && SETTERS 
     
	public TelefoneFacade getTelefoneFacade() {
		return telefoneFacade;
	}

	public void setTelefoneFacade(TelefoneFacade telefoneFacade) {
		this.telefoneFacade = telefoneFacade;
	}
	
}