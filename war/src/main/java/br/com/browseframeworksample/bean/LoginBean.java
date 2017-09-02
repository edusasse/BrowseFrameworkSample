package br.com.browseframeworksample.bean;

import java.util.Calendar;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.browseframeworksample.domain.Usuario;
import br.com.browseframeworksample.facade.UsuarioFacade;
import br.com.browseframework.jsfprimefaces.util.FacesUtil;

@ManagedBean(name = "loginBean")
@RequestScoped
public class LoginBean {
	
	// Acesso ao Facade do classificacao
    @ManagedProperty(name = "usuarioFacade", value = "#{usuarioFacade}")
	private UsuarioFacade usuarioFacade;
    
	/**
	 * Efetua o login no sistema através do Spring Security.
	 * @return
	 */
	public String doLogin() {		
		try {
			final RequestDispatcher dispatcher = FacesUtil.getServletRequest().getRequestDispatcher("/j_spring_security_check");
		    dispatcher.forward(FacesUtil.getServletRequest(), FacesUtil.getServletResponse());
		    FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception ex) {
			FacesUtil.errorMessage(ex.getMessage());
		}
		
		UserDetails u = getLoggedUserDetails();
		if (u != null && Usuario.class.isInstance(u)){
			final Usuario usuario = (Usuario) u;
			// Guarda a data do acesso anterior
			usuario.setDataAcessoAnterior(usuario.getDataUltimoAcesso());
			// Guarda a data corrente como sendo o ultimo acesso
			usuario.setDataUltimoAcesso(Calendar.getInstance().getTime());
			// Salva
			usuarioFacade.save(usuario);
		}
		
	    return null;
	}
	
	/**
	 * Fecha o sistema com segurança.
	 */
	public void doLogOut() {		
		try {
		    final RequestDispatcher dispatcher = FacesUtil.getServletRequest().getRequestDispatcher("/j_spring_security_logout");
		    dispatcher.forward(FacesUtil.getServletRequest(), FacesUtil.getServletResponse());
		    FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception ex) {
			FacesUtil.errorMessage(ex.getMessage());
		}
		
	}
	
	public UserDetails getLoggedUserDetails(){
		UserDetails result = null;
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
			result = (UserDetails) auth.getPrincipal();
		}
		
		return result;
	}

	public UsuarioFacade getUsuarioFacade() {
		return usuarioFacade;
	}

	public void setUsuarioFacade(UsuarioFacade usuarioFacade) {
		this.usuarioFacade = usuarioFacade;
	}
	
}