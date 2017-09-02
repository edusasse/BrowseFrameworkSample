package br.com.browseframeworksample.app.audit;

import org.hibernate.envers.RevisionListener;

import br.com.browseframeworksample.bean.AplicacaoControleBean;
import br.com.browseframeworksample.bean.AplicacaoDialogControleBean;
import br.com.browseframeworksample.bean.LoginBean;
import br.com.browseframeworksample.domain.Usuario;
import br.com.browseframework.jsfprimefaces.util.FacesUtil;

public class RevEnversListener implements RevisionListener {
	
	/*
	 * (non-Javadoc)
	 * @see org.hibernate.envers.RevisionListener#newRevision(java.lang.Object)
	 */
	@Override
	public void newRevision(Object revisionEntity) {
		final RevEnversEntity rev = (RevEnversEntity) revisionEntity;
		// Usuario
		final Usuario usuarioLogado = (Usuario) getLoginBean().getLoggedUserDetails();
		rev.setApelido(usuarioLogado.getApelido());
		// Aplicacao
		final AplicacaoControleBean app = getAplicacaoControleBean();
		final AplicacaoControleBean appDialog = getAplicacaoControleBean();
		rev.setAplicacao(app.getAplicacaoCorrente() + "-" + app.getAplicacaoTypeCorrente() + ( appDialog.getAplicacaoCorrente() != null ? " -> " + appDialog.getAplicacaoCorrente() + "-" + appDialog.getAplicacaoTypeCorrente() : "") );
	}

	public LoginBean getLoginBean() {
		return (LoginBean) FacesUtil.resolveExpression("#{loginBean}");
	}
	
	public AplicacaoControleBean getAplicacaoControleBean() {
		return (AplicacaoControleBean) FacesUtil.resolveExpression("#{aplicacaoBean}");
	}
	
	public AplicacaoDialogControleBean getAplicacaoDialogControleBean() {
		return (AplicacaoDialogControleBean) FacesUtil.resolveExpression("#{aplicacaoDialogBean}");
	}
	
}