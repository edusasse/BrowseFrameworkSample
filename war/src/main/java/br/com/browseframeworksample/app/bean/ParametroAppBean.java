package br.com.browseframeworksample.app.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.browseframeworksample.app.GenericAppBean;
import br.com.browseframeworksample.app.enums.DiretoriosApp;
import br.com.browseframeworksample.bean.UtilBean;
import br.com.browseframeworksample.domain.Parametro;
import br.com.browseframeworksample.facade.ParametroFacade;
import br.com.browseframework.base.crud.facade.CrudFacade;

@ManagedBean(name = "parametroAppBean")
@ViewScoped
public class ParametroAppBean extends GenericAppBean<Parametro> {
    // Acesso ao Facade do classificacao
    @ManagedProperty(name = "parametroFacade", value = "#{parametroFacade}")
	private ParametroFacade parametroFacade;
    // Acesso ao Facade do classificacao
  	@ManagedProperty(value = "#{utilBean}")
  	private UtilBean utilBean;
    
  	public ParametroAppBean() {
		super("parametro", DiretoriosApp.SECURED_APPS,  Parametro.class);
	}
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.GenericAppBean#isAccessGranted()
     */
    @Override
    public boolean isAccessGranted() {
    	return getUtilBean().areAnyGranted("ROLE_ADMIN");
    }
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.GenericAppBean#getFacade()
     */
    @SuppressWarnings("rawtypes")
	@Override
	public CrudFacade getFacade() {
		return getParametroFacade();
	}

    // GETTERS && SETTERS 
     
	public ParametroFacade getParametroFacade() {
		return parametroFacade;
	}

	public void setParametroFacade(
			ParametroFacade parametroFacade) {
		this.parametroFacade = parametroFacade;
	}

	public UtilBean getUtilBean() {
		return utilBean;
	}

	public void setUtilBean(UtilBean utilBean) {
		this.utilBean = utilBean;
	}
	
}