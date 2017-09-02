package br.com.browseframeworksample.app.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.browseframeworksample.app.GenericAppBean;
import br.com.browseframeworksample.app.enums.DiretoriosApp;
import br.com.browseframeworksample.domain.Cidade;
import br.com.browseframeworksample.facade.CidadeFacade;
import br.com.browseframework.base.crud.facade.CrudFacade;

@ManagedBean(name = "cidadeAppBean")
@ViewScoped
public class CidadeAppBean extends GenericAppBean<Cidade> {
    // Acesso ao Facade do classificacao
    @ManagedProperty(name = "cidadeFacade", value = "#{cidadeFacade}")
	private CidadeFacade cidadeFacade;
    
    public CidadeAppBean() {
		super("cidade", DiretoriosApp.SECURED_APPS,  Cidade.class);
	}
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.GenericAppBean#getFacade()
     */
    @SuppressWarnings("rawtypes")
	@Override
	public CrudFacade getFacade() {
		return getCidadeFacade();
	}
    
    // GETTERS && SETTERS
     
	public CidadeFacade getCidadeFacade() {
		return cidadeFacade;
	}

	public void setCidadeFacade(
			CidadeFacade cidadeFacade) {
		this.cidadeFacade = cidadeFacade;
	}
	
}