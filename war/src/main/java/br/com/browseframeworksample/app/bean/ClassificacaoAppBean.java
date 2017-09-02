package br.com.browseframeworksample.app.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.browseframeworksample.app.GenericAppBean;
import br.com.browseframeworksample.app.enums.DiretoriosApp;
import br.com.browseframeworksample.domain.Classificacao;
import br.com.browseframeworksample.facade.ClassificacaoFacade;
import br.com.browseframework.base.crud.facade.CrudFacade;

@ManagedBean(name = "classificacaoAppBean")
@ViewScoped
public class ClassificacaoAppBean extends GenericAppBean<Classificacao> {
    // Acesso ao Facade do classificacao
    @ManagedProperty(name = "classificacaoFacade", value = "#{classificacaoFacade}")
	private ClassificacaoFacade classificacaoFacade;
    
    public ClassificacaoAppBean() {
		super("classificacao", DiretoriosApp.SECURED_APPS,  Classificacao.class);
	}
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.GenericAppBean#getFacade()
     */
    @SuppressWarnings("rawtypes")
	@Override
	public CrudFacade getFacade() {
		return getClassificacaoFacade();
	}
 
    // GETTERS && SETTERS 
     
	public ClassificacaoFacade getClassificacaoFacade() {
		return classificacaoFacade;
	}

	public void setClassificacaoFacade(
			ClassificacaoFacade classificacaoFacade) {
		this.classificacaoFacade = classificacaoFacade;
	}
	
}