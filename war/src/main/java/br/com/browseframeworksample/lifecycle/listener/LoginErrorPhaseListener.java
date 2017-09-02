package br.com.browseframeworksample.lifecycle.listener;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.apache.log4j.Logger;
import org.springframework.security.web.WebAttributes;

import br.com.browseframework.jsfprimefaces.util.FacesUtil;

@SuppressWarnings({"serial"})
public class LoginErrorPhaseListener implements PhaseListener {

	static final Logger log = Logger.getLogger(LoginErrorPhaseListener.class);
	
	/*
	 * (non-Javadoc)
	 * @see javax.faces.event.PhaseListener#afterPhase(javax.faces.event.PhaseEvent)
	 */
	@Override
	public void afterPhase(PhaseEvent arg0) {
	}

	/*
	 * (non-Javadoc)
	 * @see javax.faces.event.PhaseListener#beforePhase(javax.faces.event.PhaseEvent)
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public void beforePhase(PhaseEvent arg0) {
		final Exception e = (Exception) FacesUtil.getSessionMap().get(WebAttributes.AUTHENTICATION_EXCEPTION);
		if (e != null){
			FacesUtil.getSessionMap().put(WebAttributes.AUTHENTICATION_EXCEPTION, null);
			FacesUtil.errorMessage(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.faces.event.PhaseListener#getPhaseId()
	 */
	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}
}