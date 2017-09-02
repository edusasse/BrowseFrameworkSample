package br.com.browseframeworksample.app.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.browseframeworksample.app.GenericAppBean;
import br.com.browseframeworksample.app.enums.DiretoriosApp;
import br.com.browseframeworksample.domain.TipoEndereco;
import br.com.browseframeworksample.facade.TipoEnderecoFacade;
import br.com.browseframework.base.crud.facade.CrudFacade;

@ManagedBean(name = "tipoEnderecoAppBean")
@ViewScoped
public class TipoEnderecoAppBean extends GenericAppBean<TipoEndereco> {
    // Acesso ao Facade do classificacao
    @ManagedProperty(name = "tipoEnderecoFacade", value = "#{tipoEnderecoFacade}")
	private TipoEnderecoFacade tipoEnderecoFacade;
    
    public TipoEnderecoAppBean() {
		super("tipoEndereco", DiretoriosApp.SECURED_APPS,  TipoEndereco.class);
	}
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.GenericAppBean#getFacade()
     */
    @SuppressWarnings("rawtypes")
	@Override
	public CrudFacade getFacade() {
		return getTipoEnderecoFacade();
	}
    
    // GETTERS && SETTERS 
     
	public TipoEnderecoFacade getTipoEnderecoFacade() {
		return tipoEnderecoFacade;
	}

	public void setTipoEnderecoFacade(
			TipoEnderecoFacade tipoEnderecoFacade) {
		this.tipoEnderecoFacade = tipoEnderecoFacade;
	}
	
}