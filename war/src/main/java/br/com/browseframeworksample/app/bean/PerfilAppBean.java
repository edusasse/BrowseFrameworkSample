package br.com.browseframeworksample.app.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.browseframeworksample.app.GenericAppBean;
import br.com.browseframeworksample.app.enums.DiretoriosApp;
import br.com.browseframeworksample.bean.UtilBean;
import br.com.browseframeworksample.domain.Perfil;
import br.com.browseframeworksample.facade.PerfilFacade;
import br.com.browseframework.base.crud.facade.CrudFacade;

@ManagedBean(name = "perfilAppBean")
@ViewScoped
public class PerfilAppBean extends GenericAppBean<Perfil> {
    // Acesso ao Facade do classificacao
    @ManagedProperty(name = "perfilFacade", value = "#{perfilFacade}")
	private PerfilFacade perfilFacade;
    // Acesso ao Facade do classificacao
   	@ManagedProperty(value = "#{utilBean}")
   	private UtilBean utilBean;
   	
    public PerfilAppBean() {
		super("perfil", DiretoriosApp.SECURED_APPS,  Perfil.class);
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
		return getPerfilFacade();
	}

    // GETTERS && SETTERS 
     
	public PerfilFacade getPerfilFacade() {
		return perfilFacade;
	}

	public void setPerfilFacade(
			PerfilFacade perfilFacade) {
		this.perfilFacade = perfilFacade;
	}

	public UtilBean getUtilBean() {
		return utilBean;
	}

	public void setUtilBean(UtilBean utilBean) {
		this.utilBean = utilBean;
	}
	
}