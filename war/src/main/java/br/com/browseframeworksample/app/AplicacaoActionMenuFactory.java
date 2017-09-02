package br.com.browseframeworksample.app;

import org.primefaces.component.menuitem.MenuItem;

import br.com.browseframeworksample.app.enums.AplicacaoBotaoId;
import br.com.browseframeworksample.app.enums.AplicacaoType;
import br.com.browseframework.jsfprimefaces.util.FacesUtil;

public class AplicacaoActionMenuFactory {
	
	private final String sufixo = "Dlg";
	
	private static AplicacaoActionMenuFactory instance = null;
	
	private static Long contador = 0L;
	
	public static Long getComponentUniqueId(){
		if (Long.MAX_VALUE == contador){
			contador = 0L;
		}
		return contador++;
	}
	
	public static AplicacaoActionMenuFactory getInstance(){
		if (instance == null){
			instance = new AplicacaoActionMenuFactory();
		}
		
		return instance;
	}
	
	public MenuItem build(Aplicacao aplicacaoCorrente, AplicacaoType tipoCorrente, AplicacaoBotaoId abId, String expressao, String onClick, String update, Boolean isHabilitado, boolean isSufixo){
		MenuItem result = null;
		
		if (AplicacaoBotaoId.EXCLUIR.equals(abId)){
			result = buildBotaoExcluir(aplicacaoCorrente, tipoCorrente, expressao, onClick, update, isHabilitado, isSufixo);
		} else if (AplicacaoBotaoId.NOVO.equals(abId)){
			result = buildBotaoNovo(aplicacaoCorrente, tipoCorrente, expressao, onClick, update, isHabilitado, isSufixo);
		} else if (AplicacaoBotaoId.SALVAR.equals(abId)){
			result = buildBotaoSalvar(aplicacaoCorrente, tipoCorrente, expressao, onClick, update, isHabilitado, isSufixo);
		} else if (AplicacaoBotaoId.FECHAR.equals(abId)){
			result = buildBotaoFechar(aplicacaoCorrente, tipoCorrente, expressao, onClick, update, isHabilitado, isSufixo);
		}
		
		return result;
	}
	
	/**
	 * Carrega atributos comuns de todos os MenuItems.
	 * @param mi
	 */
	private void carregarAtributosComunsMenuItem(Aplicacao aplicacaoCorrente, AplicacaoType tipoCorrente, MenuItem mi, AplicacaoBotaoId abId, String expressao, String onClick, String update, Boolean isHabilitado, boolean isSufixo){
		// Update
		if (update != null && isSufixo){
			mi.setUpdate("@(.actionMenu) @form");
		} else if (update == null){
			mi.setUpdate("@form @(.actionMenu)");
		} else {
			mi.setUpdate(update);
		}
		
		mi.setStyle("color: white;");
		final boolean menuItemHabilitado = aplicacaoCorrente.isMenuItemHabilitado(abId, tipoCorrente);
		mi.setDisabled(!menuItemHabilitado);
		// Se vier informado da aplicacao controle e for FALSO
		if (isHabilitado != null && isHabilitado.equals(Boolean.FALSE)){
			mi.setDisabled(true);
		} else
			// Se vier informa da aplicacao controle e for VERDADEIRO e a aplicacao também concordar seta verdadeiro
			if (isHabilitado != null && isHabilitado.equals(Boolean.TRUE) && menuItemHabilitado){
			mi.setDisabled(false);
		} else {
			mi.setDisabled(!menuItemHabilitado);
		}
		mi.setId((abId.getId()+(isSufixo ? sufixo + aplicacaoCorrente.getAppKey() : "")) + String.valueOf(getComponentUniqueId()));
		// Expressão
		if (expressao != null){
			mi.setActionExpression(FacesUtil.buildMethodExpression(expressao));
			mi.setImmediate(false);
		}
		// OnClick
		if (onClick != null){
			mi.setOnclick(onClick);
			// Se tiver uma expressão não vai executar ela quando tiver uma URL
			if (expressao != null){
				mi.setUrl(null);
			} else {
				mi.setUrl("javascript:void(0)");
			}
		}
		
	}
	
	/**
	 * Novo.
	 * FIXME Ao criar um novo limpa todos os formularios da tela.
	 * @param aplicacaoCorrente
	 * @param tipoCorrente
	 * @param expressao
	 * @return
	 */
	private MenuItem buildBotaoNovo(Aplicacao aplicacaoCorrente, AplicacaoType tipoCorrente, String expressao, String onClick, String update, Boolean isHabilitado, boolean isSufixo){
		MenuItem result = null;
		if (aplicacaoCorrente.isMenuItemVisivel(AplicacaoBotaoId.NOVO, tipoCorrente)){
			result = new MenuItem();
			result.setValue(FacesUtil.getResourceBundleString("msgs", "actionMenu.default.new"));
			result.setIcon("ui-icon-document");
			carregarAtributosComunsMenuItem(aplicacaoCorrente, tipoCorrente, result, AplicacaoBotaoId.NOVO, expressao, onClick, update, isHabilitado, isSufixo);
			result.setImmediate(true);
			result.setProcess("@this");
		}
		return result;
	
	}
	
	/**
	 * Salvar.
	 * @param aplicacaoCorrente
	 * @param tipoCorrente
	 * @param expressao
	 * @return
	 */
	private MenuItem buildBotaoSalvar(Aplicacao aplicacaoCorrente, AplicacaoType tipoCorrente, String expressao, String onClick, String update, Boolean isHabilitado, boolean isSufixo){
		MenuItem result = null;
		if (aplicacaoCorrente.isMenuItemVisivel(AplicacaoBotaoId.SALVAR, tipoCorrente)){
			result = new MenuItem();
			result.setValue(FacesUtil.getResourceBundleString("msgs", "actionMenu.default.save"));
			result.setIcon("ui-icon-disk");
			carregarAtributosComunsMenuItem(aplicacaoCorrente, tipoCorrente, result, AplicacaoBotaoId.SALVAR, expressao, onClick, update, isHabilitado, isSufixo);
		}
		return result;
	}
	
	/**
	 * Excluir.
	 * @param aplicacaoCorrente
	 * @param tipoCorrente
	 * @param expressao
	 * @return
	 */
	private MenuItem buildBotaoExcluir(Aplicacao aplicacaoCorrente, AplicacaoType tipoCorrente, String expressao, String onClick, String update, Boolean isHabilitado, boolean isSufixo){
		MenuItem result = null;
		if (aplicacaoCorrente.isMenuItemVisivel(AplicacaoBotaoId.EXCLUIR, tipoCorrente)){
			result = new MenuItem();
			result.setValue(FacesUtil.getResourceBundleString("msgs", "actionMenu.default.remove"));
			result.setIcon("ui-icon-trash");
			carregarAtributosComunsMenuItem(aplicacaoCorrente, tipoCorrente, result, AplicacaoBotaoId.EXCLUIR, null, onClick, update, isHabilitado, isSufixo);
		}
		return result;
	
	}
	
	/**
	 * Salvar.
	 * @param aplicacaoCorrente
	 * @param tipoCorrente
	 * @param expressao
	 * @return
	 */
	private MenuItem buildBotaoFechar(Aplicacao aplicacaoCorrente, AplicacaoType tipoCorrente, String expressao, String onClick, String update, Boolean isHabilitado, boolean isSufixo){
		MenuItem result = null;
		if (aplicacaoCorrente.isMenuItemVisivel(AplicacaoBotaoId.FECHAR, tipoCorrente)){
			result = new MenuItem();
			result.setValue(FacesUtil.getResourceBundleString("msgs", "actionMenu.default.fechar"));
			result.setIcon("ui-icon-arrowreturnthick-1-w");
			result.setProcess("@this");
			carregarAtributosComunsMenuItem(aplicacaoCorrente, tipoCorrente, result, AplicacaoBotaoId.FECHAR, expressao, onClick, update, isHabilitado, isSufixo);
			result.setStyle("color: #FFCC00;");
			result.setUpdate(":all");
		}
		return result;
	
	}
}
