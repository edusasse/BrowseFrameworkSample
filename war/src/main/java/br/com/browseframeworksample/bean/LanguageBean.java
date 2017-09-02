package br.com.browseframeworksample.bean;

import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import br.com.browseframeworksample.facade.UsuarioFacade;

@ManagedBean(name = "languageBean")
@SessionScoped
public class LanguageBean {
	
	// Acesso ao Facade do classificacao
    @ManagedProperty(name = "usuarioFacade", value = "#{usuarioFacade}")
	private UsuarioFacade usuarioFacade;
	 
    private Locale currentLocale = new Locale("pt", "BR");
    
    @PostConstruct
    public void loadCurrentLocale(){
		final UIViewRoot viewRoot = getViewRoot();
		viewRoot.setLocale(currentLocale);
	}
	
	public void loadEnglishLocale() {
		final UIViewRoot viewRoot = getViewRoot();
		currentLocale = Locale.US;
		viewRoot.setLocale(currentLocale);
	}

	public void loadPortugueseLocale() {
		final UIViewRoot viewRoot = getViewRoot();
		currentLocale = new Locale("pt", "BR");
		viewRoot.setLocale(currentLocale);
	}

	public UIViewRoot getViewRoot() {
		final UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
		return viewRoot;
	}
	
    // GETTERS && SETTERS

	public UsuarioFacade getUsuarioFacade() {
		return usuarioFacade;
	}

	public void setUsuarioFacade(UsuarioFacade usuarioFacade) {
		this.usuarioFacade = usuarioFacade;
	}

	public Locale getCurrentLocale() {
		return currentLocale;
	}

	public void setCurrentLocale(Locale currentLocale) {
		this.currentLocale = currentLocale;
	}
	
}