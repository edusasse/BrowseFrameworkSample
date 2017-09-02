package br.com.browseframeworksample.app;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import br.com.browseframeworksample.app.enums.AplicacaoBotaoId;
import br.com.browseframeworksample.bean.AplicacaoControleBean;
import br.com.browseframework.jsfprimefaces.util.FacesUtil;

public class AplicacaoActionMenuActionListener implements ActionListener {

	@Override
	public void processAction(ActionEvent event) throws AbortProcessingException {
		final AplicacaoControleBean aplicacaoControleBean = (AplicacaoControleBean) FacesUtil.resolveExpression("#{aplicacaoBean}");
		if (aplicacaoControleBean != null){
			if (AplicacaoBotaoId.SALVAR.getId().equals(event.getComponent().getId())){
				aplicacaoControleBean.doProcessarBotaoSalvar();
			} else if (AplicacaoBotaoId.NOVO.getId().equals(event.getComponent().getId())){
				aplicacaoControleBean.doProcessarBotaoNovo();
			} else if (AplicacaoBotaoId.EXCLUIR.getId().equals(event.getComponent().getId())){
				aplicacaoControleBean.doProcessarBotaoExcluir();
			}
		}
	}
	
}