package br.com.browseframeworksample.app.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.browseframeworksample.app.GenericAppBean;
import br.com.browseframeworksample.app.enums.DiretoriosApp;
import br.com.browseframeworksample.domain.UnidadeFederacao;
import br.com.browseframeworksample.facade.UnidadeFederacaoFacade;
import br.com.browseframework.base.crud.facade.CrudFacade;

@ManagedBean(name = "unidadeFederacaoAppBean")
@ViewScoped
public class UnidadeFederacaoAppBean extends GenericAppBean<UnidadeFederacao> {
    // Acesso ao Facade do classificacao
    @ManagedProperty(name = "unidadeFederacaoFacade", value = "#{unidadeFederacaoFacade}")
	private UnidadeFederacaoFacade unidadeFederacaoFacade;
    
    public UnidadeFederacaoAppBean() {
		super("unidadeFederacao", DiretoriosApp.SECURED_APPS,  UnidadeFederacao.class);
	}
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.GenericAppBean#getFacade()
     */
    @SuppressWarnings("rawtypes")
	@Override
	public CrudFacade getFacade() {
		return getUnidadeFederacaoFacade();
	}
    
    // GETTERS && SETTERS 
     
	public UnidadeFederacaoFacade getUnidadeFederacaoFacade() {
		return unidadeFederacaoFacade;
	}

	public void setUnidadeFederacaoFacade(
			UnidadeFederacaoFacade unidadeFederacaoFacade) {
		this.unidadeFederacaoFacade = unidadeFederacaoFacade;
	}
	
}