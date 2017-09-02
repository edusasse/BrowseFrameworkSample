package br.com.browseframeworksample.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.browseframeworksample.domain.Usuario;
import br.com.browseframeworksample.facade.UsuarioFacade;
import br.com.browseframework.base.exception.GenericBusinessException;
import br.com.browseframework.jsfprimefaces.util.FacesUtil;

@Service(value = "usuarioService")
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UsuarioFacade usuarioFacade;
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (getUsuarioFacade() == null){
			throw new GenericBusinessException(FacesUtil.getResourceBundleString("msgs", "login.serviceConnectFail"));
		}
		
		final Usuario usuario = getUsuarioFacade().findByApelido(username);
		
		return (UserDetails) usuario;		
	}

	// GETTERS && SETTERS 
	
	public UsuarioFacade getUsuarioFacade() {
		return usuarioFacade;
	}

	public void setUsuarioFacade(UsuarioFacade usuarioFacade) {
		this.usuarioFacade = usuarioFacade;
	}
	
}

