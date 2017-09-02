package br.com.browseframeworksample.bean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

import br.com.browseframeworksample.app.Aplicacao;
import br.com.browseframeworksample.app.AplicacaoActionMenuFactory;
import br.com.browseframeworksample.app.enums.AplicacaoBotaoId;
import br.com.browseframeworksample.app.enums.AplicacaoType;
import br.com.browseframework.base.exception.GenericBusinessException;
import br.com.browseframework.jsfprimefaces.util.FacesUtil;

@ManagedBean(name = "aplicacaoDialogBean")
@ViewScoped
public class AplicacaoDialogControleBean {
	static Logger log = Logger.getLogger(AplicacaoDialogControleBean.class.getName());
	
	// Aplicacao em execução
	private Stack<Aplicacao> pilhaAplicacaoDialogCorrente = new Stack<Aplicacao>();
	// Modelo do menu de ação dialog
	private Map<String, MenuModel> mapMenuActionModelDialog = new HashMap<String, MenuModel>();
	
	private String defaultBtnId = null;
	
	/**
	 * Carrega a aplicação inicial para o usuário.
	 */
	@PostConstruct
	public void doPostConstruct(){ }

	@PreDestroy
	public void doPreDestroy(){ }
	
	/**
	 * Fecha a aplicação corrente.
	 */
	public void doFecharAplicacaoDialogCorrente(){
		if (getPilhaAplicacaoDialogCorrente() != null && !getPilhaAplicacaoDialogCorrente().isEmpty()){
			getAplicacaoDialogCorrente().doFechar();
			getPilhaAplicacaoDialogCorrente().pop();
		} 
	}
	
	/**
	 * Fecha a aplicação corrente se esta for da mesma chave.
	 */
	public void doFecharAplicacaoDialogCorrente(String appKey){
		if (getPilhaAplicacaoDialogCorrente() != null && !getPilhaAplicacaoDialogCorrente().isEmpty()){
			if (getAplicacaoDialogCorrente().getAppKey().equals(appKey)){
				getAplicacaoDialogCorrente().doFechar();
				getPilhaAplicacaoDialogCorrente().pop();
			}
		} 
	}
	
	/**
	 * Carrega o caminho da aplicação devido ao javascript executar antes do actionListener.
	 * @param appBeanName
	 */
	public void doCarregarDialogCaminho(String appBeanName, String property){
		final Aplicacao app = (Aplicacao) FacesUtil.resolveExpression("#{" + appBeanName + "}");
		doCarregarDialog(app, property);
		app.doNovo();
	}
 
	/**
	 * Carrega a aplicação abrindo a lista
	 * @param app
	 */
	private void doCarregarDialog(Aplicacao app, String property){
		boolean emExecucao = false;
		if (getAplicacaoDialogCorrente() != null || (getAplicacaoDialogCorrente() != null && !getAplicacaoDialogCorrente().equals(app))){
			final Iterator<Aplicacao> itApp = getPilhaAplicacaoDialogCorrente().iterator();
			
			while (itApp.hasNext()){
				final Aplicacao a = itApp.next();
				if (a.equals(app)){
					log.info("Aplicação ja esta aberta em segundo plano!");
					emExecucao = true;
					break;
				}
			}
		}
		if (!emExecucao){
			// Guarda aplicacao na pilha
			getPilhaAplicacaoDialogCorrente().push(app);
			
			// Caso tenha uma propriedade para a qual deva jogar o valor
			if (property != null && property.trim().length() > 0){
				app.setDialogSetProperty(property);
			}
			// Como ja vai abrir o cadastro deixa um novo dto disponivel
			getAplicacaoDialogCorrente().doNovo();
				
			// Carrega menu
			doBuildMenu();
		} else {
			log.info("Aplicação [" + app.getNomeAplicacao() + "] já em execução!");
		}
	}

	protected void doBuildMenu() {
		getMapMenuActionModelDialog().put(getAplicacaoDialogCorrente().getAppKey(), (buildMenu(getAplicacaoDialogCorrente().getMenuItems(AplicacaoType.DIALOG), true)));
	}
	
	/**
	 * Retorna o caminho para a pagina da aplicação pelo seu tipo corrente.
	 * @return
	 */
	public String getCaminhoDialog(){
		String retorno = null;
		retorno = getAplicacaoDialogCorrente().getCaminho(AplicacaoType.DIALOG);
		
		return retorno;
	}

	/**
	 * Processa chamada do botão novo
	 */
	public void doProcessarBotaoNovo(){
		getAplicacaoDialogCorrente().doNovo();
	}
	
	/**
	 * Processa chamada do botão salvar
	 */
	public void doProcessarBotaoSalvar(){
		try {
			getAplicacaoDialogCorrente().doSalvar();
			//FacesUtil.runJavaScriptCode("doFecharDialog"+getAplicacaoDialogCorrente().getAppKey() + "('" + getAplicacaoDialogCorrente().getAppKey() + "');");
			getAplicacaoDialogCorrente().doNovo(); // para limpar
			FacesUtil.infoMessage(FacesUtil.getResourceBundleString("msgs", "action.save.successMessage"));
		} catch (GenericBusinessException gbe){
			throw gbe;
		} catch(Exception e){
			throw new GenericBusinessException(e.getMessage());
		}
	}
 
	/**
	 * Retorna a aplicacao do topo da pilha
	 * @return
	 */
	public Aplicacao getAplicacaoDialogCorrente() {
		Aplicacao retorno = null;
		if (!getPilhaAplicacaoDialogCorrente().isEmpty()){
			retorno = getPilhaAplicacaoDialogCorrente().peek();
		}
		return retorno;
	}
	
	/**
	 * Constroi o ActionMenu para a LISTA e CRUD com sufixo em branco.
	 * @param listaMenuItemAdd
	 * @return
	 */
	public MenuModel buildMenu(List<MenuItem> listaMenuItemAdd){
		return buildMenu(listaMenuItemAdd, false);
	}
	
	/**
	 * Constroi o ActionMenu.
	 * @param listaMenuItemAdd
	 * @return
	 */
	public MenuModel buildMenu(List<MenuItem> listaMenuItemAdd, boolean dialogMenu){
		setDefaultBtnId(null);
		// Limpa o menu model atual
		// ------------------------
		final MenuModel model = getActionMenuEmptyMenuModelDialog();
		
		// --------------------------------------
		// NOVO - AMBOS [CRUD | LISTA]
		// --------------------------------------
		final String actionOnClick = getAplicacaoDialogCorrente().getOnClickActionByAplicacaoBotaoId(AplicacaoType.CRUD, AplicacaoBotaoId.NOVO);
		String onClick = null; 
		if (actionOnClick != null){
			onClick = actionOnClick;
		}
		
		final String actionExpressao = getAplicacaoDialogCorrente().getExpressaoByAplicacaoBotaoId(AplicacaoType.CRUD, AplicacaoBotaoId.NOVO);
		String expressao = null; 
		if (actionOnClick != null){
			expressao = actionExpressao;
		} else {
			expressao = "#{aplicacaoBean.doProcessarBotaoNovo()}";
		}
		
		final MenuItem miNovo = AplicacaoActionMenuFactory.getInstance().build(getAplicacaoDialogCorrente(), AplicacaoType.DIALOG, AplicacaoBotaoId.NOVO, expressao, onClick, "@form", null, dialogMenu);
		if (miNovo != null){
			model.addMenuItem(miNovo);
		}
		
		// --------------------------------------
		// SALVAR - SOMENTE [CRUD]
		// --------------------------------------
	
		final MenuItem miSalvar = AplicacaoActionMenuFactory.getInstance().build(getAplicacaoDialogCorrente(), AplicacaoType.DIALOG, AplicacaoBotaoId.SALVAR, "#{aplicacaoDialogBean.doProcessarBotaoSalvar()}", null, null, null, dialogMenu);
		if (miSalvar != null){
			model.addMenuItem(miSalvar);
		}
		
		// Adiciona itens da aplicacao especificamente criados
		// ---------------------------------------------------
		doAdicionarMenuItemDaAplicacao(listaMenuItemAdd, model);
		
		return model;
	}

	/**
	 * Limpa o menu model ou cria um novo.
	 * @return
	 */
	private MenuModel getActionMenuEmptyMenuModelDialog() {
		MenuModel result = null;
		MenuModel model = getMapMenuActionModelDialog().get(getAplicacaoDialogCorrente().getAppKey());
		if (model != null){
			model.getContents().clear();
			result = model;
		} else {
			result = new DefaultMenuModel();
		}
		return result;
	}

	/**
	 * Adiciona a lista de menus oriundo da Aplicacao no modelo gerado.
	 * @param listaMenuItemAdd
	 * @param model
	 */
	private void doAdicionarMenuItemDaAplicacao(List<MenuItem> listaMenuItemAdd, final MenuModel model) {
		// Da aplicacao
		if (listaMenuItemAdd != null && !listaMenuItemAdd.isEmpty()){
			for (MenuItem mi : listaMenuItemAdd){
				model.addMenuItem(mi);
			}
		}
	}
	
	/**
	 * Retorna o menu armazenado no mapa para a chave da aplicacao informada.
	 * @param appKey
	 * @return
	 */
	public MenuModel getDialogActionMenuModel(String appKey){
		MenuModel retorno = null;
		// Sempre recontroi o menu
		if (getAplicacaoDialogCorrente() != null){
			doBuildMenu();
		
			if (!getMapMenuActionModelDialog().isEmpty()){
				retorno = getMapMenuActionModelDialog().get(appKey);
			}
		}
		
		return retorno;
	}
	
	/**
	 * Retorna verdadeiro se houver uma aplicação em aberto.
	 * @return
	 */
	public boolean hasAppDialogAberta(){
		boolean retorno = (getPilhaAplicacaoDialogCorrente() != null && getPilhaAplicacaoDialogCorrente().size() >= 1);
		FacesUtil.addRequestContextCallbackParam("hasAppDialogAberta", retorno);
		return retorno;
	}
	
	// GETTERS && SETTERS
	
	public Stack<Aplicacao> getPilhaAplicacaoDialogCorrente() {
		return pilhaAplicacaoDialogCorrente;
	}

	public void setPilhaAplicacaoDialogCorrente(
			Stack<Aplicacao> pilhaAplicacaoDialogCorrente) {
		this.pilhaAplicacaoDialogCorrente = pilhaAplicacaoDialogCorrente;
	}

	public Map<String, MenuModel> getMapMenuActionModelDialog() {
		return mapMenuActionModelDialog;
	}

	public void setMapMenuActionModelDialog(
			Map<String, MenuModel> mapMenuActionModelDialog) {
		this.mapMenuActionModelDialog = mapMenuActionModelDialog;
	}

	public String getDefaultBtnId() {
		return defaultBtnId;
	}

	public void setDefaultBtnId(String defaultBtnId) {
		this.defaultBtnId = defaultBtnId;
	}
 
}