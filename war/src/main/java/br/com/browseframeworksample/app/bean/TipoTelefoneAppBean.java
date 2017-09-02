package br.com.browseframeworksample.app.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.browseframeworksample.app.GenericAppBean;
import br.com.browseframeworksample.app.enums.DiretoriosApp;
import br.com.browseframeworksample.domain.TipoTelefone;
import br.com.browseframeworksample.facade.TipoTelefoneFacade;
import br.com.browseframework.base.crud.facade.CrudFacade;

@ManagedBean(name = "tipoTelefoneAppBean")
@ViewScoped
public class TipoTelefoneAppBean extends GenericAppBean<TipoTelefone> {
    // Acesso ao Facade do classificacao
    @ManagedProperty(name = "tipoTelefoneFacade", value = "#{tipoTelefoneFacade}")
	private TipoTelefoneFacade tipoTelefoneFacade;
    
    public TipoTelefoneAppBean() {
		super("tipoTelefone", DiretoriosApp.SECURED_APPS,  TipoTelefone.class);
	}
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.GenericAppBean#getFacade()
     */
    @SuppressWarnings("rawtypes")
	@Override
	public CrudFacade getFacade() {
		return getTipoTelefoneFacade();
	}
    
    // GETTERS && SETTERS 
     
	public TipoTelefoneFacade getTipoTelefoneFacade() {
		return tipoTelefoneFacade;
	}

	public void setTipoTelefoneFacade(
			TipoTelefoneFacade tipoTelefoneFacade) {
		this.tipoTelefoneFacade = tipoTelefoneFacade;
	}
	
}