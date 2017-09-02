package br.com.browseframeworksample.bean;

import java.util.List;
import java.util.Stack;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

import br.com.browseframeworksample.app.Aplicacao;
import br.com.browseframeworksample.app.AplicacaoActionMenuFactory;
import br.com.browseframeworksample.app.enums.AplicacaoBotaoId;
import br.com.browseframeworksample.app.enums.AplicacaoType;
import br.com.browseframeworksample.domain.Parametro;
import br.com.browseframeworksample.domain.Perfil;
import br.com.browseframeworksample.domain.Usuario;
import br.com.browseframeworksample.domain.enums.NomeParametro;
import br.com.browseframework.base.data.dto.BaseDTO;
import br.com.browseframework.base.exception.GenericBusinessException;
import br.com.browseframework.jsfprimefaces.util.FacesUtil;

@ManagedBean(name = "aplicacaoBean")
@ViewScoped
public class AplicacaoControleBean {
	static Logger log = Logger.getLogger(AplicacaoDialogControleBean.class.getName());
	
	// Acesso ao Facade do classificacao
	@ManagedProperty(value = "#{utilBean}")
	private UtilBean utilBean;
	
	private String defaultBtnId = null;
	
	/**
	 * Classe que envolve a aplicação e seu tipo.
	 */
	public class AplicacaoEAplicacaoType {
		private Aplicacao aplicacao;
		private AplicacaoType aplicacaoType;
		
		public AplicacaoEAplicacaoType(Aplicacao aplicacao, AplicacaoType aplicacaoType){
			setAplicacao(aplicacao);
			setAplicacaoType(aplicacaoType);
		}

		public Aplicacao getAplicacao() {
			return aplicacao;
		}

		public void setAplicacao(Aplicacao aplicacao) {
			this.aplicacao = aplicacao;
		}

		public AplicacaoType getAplicacaoType() {
			return aplicacaoType;
		}

		public void setAplicacaoType(AplicacaoType aplicacaoType) {
			this.aplicacaoType = aplicacaoType;
		}
	}
	
	// Aplicacao em execução
	private Stack<AplicacaoEAplicacaoType> pilhaAplicacaoCorrente = new Stack<AplicacaoEAplicacaoType>();
	
	// Registro selecionado na lista
	@SuppressWarnings("rawtypes")
	private BaseDTO registroSelecionado;
	// Modelo do menu de ação
	private MenuModel menuActionModel;
	 
	/**
	 * Carrega a aplicação inicial para o usuário.
	 */
	@PostConstruct
	public void doPostConstruct(){
		doCarregarDashboard();
	}

	public void doCarregarDashboard() {
		// Clipping
		boolean clipping = hasUsuarioPerfilClipping();
		boolean perfilNaoClipping = hasUsuarioPerfilNaoClipping();
		doCarregar((Aplicacao) FacesUtil.resolveExpression("#{dashboardAppBean}"), AplicacaoType.DASHBOARD);
	}

	protected boolean hasUsuarioPerfilClipping() {
		boolean clipping = false;
		final LoginBean login = (LoginBean) FacesUtil.resolveExpression("#{loginBean}");
		final Usuario u = ((Usuario) login.getLoggedUserDetails());
		final Parametro par = getUtilBean().getParametroFacade().findByNome(NomeParametro.PERFIL_CLIPPING.getDescricao());
		Perfil perfil = null;
		try {
			if (par != null){
				if (par.getValor() != null){
					final Long id = Long.parseLong(String.valueOf(par.getValor()));
					perfil = getUtilBean().getPerfilFacade().findById(id);
				}
			}
		} catch (Exception e){
			log.error(e);
		}
		if (perfil != null){
			for (Perfil p : u.getPerfis()){
				if (p.getId().equals(perfil.getId())){
					clipping = true;
					break;
				}
			}
		}
		return clipping;
	}
	
	protected boolean hasUsuarioPerfilNaoClipping() {
		boolean naoClipping = false;
		final LoginBean login = (LoginBean) FacesUtil.resolveExpression("#{loginBean}");
		final Usuario u = ((Usuario) login.getLoggedUserDetails());
		final Parametro par = getUtilBean().getParametroFacade().findByNome(NomeParametro.PERFIL_CLIPPING.getDescricao());
		Perfil perfil = null;
		try {
			if (par != null){
				if (par.getValor() != null){
					final Long id = Long.parseLong(String.valueOf(par.getValor()));
					perfil = getUtilBean().getPerfilFacade().findById(id);
				}
			}
		} catch (Exception e){
			log.error(e);
		}
		if (perfil != null){
			for (Perfil p : u.getPerfis()){
				if (!(p.getId().equals(perfil.getId()))){
					naoClipping = true;
					break;
				}
			}
		}
		return naoClipping;
	}
	
	@PreDestroy
	public void doPreDestroy(){ }
	
	private void push(Aplicacao app, AplicacaoType appType){
		boolean emExecucao = false;
		if (!getPilhaAplicacaoCorrente().isEmpty()){
			final AplicacaoEAplicacaoType aat = getPilhaAplicacaoCorrente().peek(); 
			final Aplicacao a = aat.getAplicacao();
			final AplicacaoType at = aat.getAplicacaoType();
			
			if (a.equals(app) && at.equals(appType)){
				log.info("Aplicação ja esta aberta!");
				emExecucao = true;
			}
		 
		}
		if (!emExecucao){
			// Guarda aplicacao na pilha
			final AplicacaoEAplicacaoType aat = new AplicacaoEAplicacaoType(app, appType); 
			getPilhaAplicacaoCorrente().push(aat);
		} else {
			log.info("Aplicação [" + app.getNomeAplicacao() + "] já em execução!");
		}
	}
	
	private void pop(){
		if (getAplicacaoCorrente() != null){
			getPilhaAplicacaoCorrente().pop();
		}
	}
	
	public void doCarregar(Aplicacao app, String typeS){
		final AplicacaoType type = AplicacaoType.valueOf(typeS);
		doCarregar(app, type, null);
	}
	
	public void doCarregar(Aplicacao app, AplicacaoType type){
		doCarregar(app, type, null);
	}
	
	/**
	 * Carrega a aplicação abrindo a lista
	 * @param app
	 */
	public void doCarregar(Aplicacao app, AplicacaoType type, Object parametro){
		doCarregar(app, type, null, parametro);
	}
	
	/**
	 * Carrega uma aplicação no seu formato CRUD informando como objeto em edição o DTO.
	 * @param app
	 * @param dto
	 * @param parametro
	 */
	public void doCarregarDTO(Aplicacao app, @SuppressWarnings("rawtypes") BaseDTO dto, Object parametro){
		doCarregar(app, AplicacaoType.CRUD, dto, parametro);
	}
	
	/**
	 * Carrega a aplicação abrindo a lista
	 * @param app
	 */
	private void doCarregar(Aplicacao app, AplicacaoType type, @SuppressWarnings("rawtypes") BaseDTO dto, Object parametro){
		
		if (app != null){
			if (!app.isAccessGranted()){
				FacesUtil.errorMessage("Acesso negado!");
			} else {
				if (AplicacaoType.LISTA.equals(type) || AplicacaoType.CRUD.equals(type) || AplicacaoType.DASHBOARD.equals(type)){
					// Guarda aplicacao na pilha e Guarda aplicacao tipo corrente
					push(app, type);
				}
		
				// Carrega o DTO informado somente se for uma aplicacao CRUD
				if (dto != null && AplicacaoType.CRUD.equals(type)){
					getAplicacaoCorrente().setBaseDTOEmEdicao(dto);
				}
				
				// Carrega o parametro na aplicacao
				getAplicacaoCorrente().setParametro(parametro);
				
				// Carrega aplicacao
				// ------------------
				if (AplicacaoType.LISTA.equals(type)){
					getAplicacaoCorrente().doCarregar(AplicacaoType.LISTA);
				} else if (AplicacaoType.CRUD.equals(type)){
					getAplicacaoCorrente().doCarregar(AplicacaoType.CRUD);
				} else if (AplicacaoType.DASHBOARD.equals(type)){
					app.doCarregar(AplicacaoType.DASHBOARD);
				}
				
				// Carrega menu
				// ------------
				doCarregarMenu();
			}
		}
		
	}

	protected void doCarregarMenu() {
		if (getAplicacaoCorrente().isExibirActionMenu()){
			setMenuActionModel(buildMenu(getAplicacaoCorrente().getMenuItems(getAplicacaoTypeCorrente())));
		} else {
			setMenuActionModel(null);
		}
	}
	
	/**
	 * Fecha a aplicação corrente.
	 */
	public void doFecharAplicacaoCorrente(){
		if (getPilhaAplicacaoCorrente() != null && !getPilhaAplicacaoCorrente().isEmpty()){
			pop();
			doCarregarMenu();			
		} 
	}
	
	/**
	 * Retorna o caminho para a pagina da aplicação pelo seu tipo corrente.
	 * @return
	 */
	public String getCaminho(){
		String retorno = null;
		retorno = getAplicacaoCorrente().getCaminho(getAplicacaoTypeCorrente());
		
		return retorno;
	}
	
	/**
	 * Recupera o item selecionado na lista e direciona para tela de edição
	 */
	public void doProcessarListaDuploClique(){
		getAplicacaoCorrente().setBaseDTOEmEdicao(getRegistroSelecionado());
		// Troca o tipo de aplicacao
		// -------------------------------------------------------
		trocaAplicacaoTypeDaAplicacaoCorrente(AplicacaoType.CRUD);
		doCarregarMenu();
	}

	protected void trocaAplicacaoTypeDaAplicacaoCorrente(AplicacaoType at) {
		if (!getPilhaAplicacaoCorrente().isEmpty()){
			final AplicacaoEAplicacaoType aat = new AplicacaoEAplicacaoType(getPilhaAplicacaoCorrente().peek().aplicacao, at); 
			getPilhaAplicacaoCorrente().push(aat);
		} else {
			FacesUtil.errorMessage(FacesUtil.getResourceBundleString("msgs", "app.stackEmpty"));
		}
	}
	
	/**
	 * Ação ao selecionar uma registro na lista.
	 */
	public void doProcessarListaSelecaoDeLinha(){
		if (getRegistroSelecionado() != null){
			doCarregarMenu();
		}
	}
	
	/**
	 * Limpa objeto selecionado
	 */
	public void doProcessarListaDesSelecaoDeLinha(){
		setRegistroSelecionado(null);
		doCarregarMenu();
	}
	
	/**
	 * Processa chamada do botão novo
	 */
	public void doProcessarBotaoFechar(){
		getAplicacaoCorrente().doProcessarBotaoFechar();
		// Passa o controle para a aplicação por um instante
		getAplicacaoCorrente().doFechar();
		// Remove aplicação da pilha
		pop();
		doCarregarMenu();
	}
	
	/**
	 * Processa chamada do botão novo
	 */
	public void doProcessarBotaoNovo(){
		// Troca o tipo de aplicacao
		// -------------------------------------------------------
		if (getAplicacaoCorrente().isTrocaAplicacaoCorrente()){
			trocaAplicacaoTypeDaAplicacaoCorrente(AplicacaoType.CRUD);
		}
		getAplicacaoCorrente().doProcessarBotaoNovo();
		getAplicacaoCorrente().doNovo();
		doCarregarMenu();
	}
	
	/**
	 * Processa chamada do botão salvar
	 */
	public void doProcessarBotaoSalvar(){
		try {
			getAplicacaoCorrente().doProcessarBotaoSalvar();
			getAplicacaoCorrente().doSalvar();
			if (AplicacaoType.CRUD.equals(getAplicacaoTypeCorrente())){
				final Aplicacao appCor = getAplicacaoCorrente();
				if (appCor.isFecharAoSalvar()){
					doProcessarBotaoFechar();
				}
			}
 			FacesUtil.infoMessage(FacesUtil.getResourceBundleString("msgs", "action.save.successMessage"));
		} catch(GenericBusinessException gbe){
			throw gbe;
		} catch(Exception e){
			throw new GenericBusinessException(e.getMessage());
		} finally {
			FacesUtil.getRequestContext().update("@(.actionMenu)");
		}
	}

	/**
	 * Processa chamada do botão excluir para a aplicação.
	 */
	public void doProcessarBotaoExcluir(){
		if (AplicacaoType.LISTA.equals(getAplicacaoTypeCorrente())){
			getAplicacaoCorrente().setBaseDTOEmEdicao(getRegistroSelecionado());
		}
		getAplicacaoCorrente().doProcessarBotaoExcluir();
		getAplicacaoCorrente().doExcluir();
		getAplicacaoCorrente().doNovo();
		if (AplicacaoType.CRUD.equals(getAplicacaoTypeCorrente())){
			doCarregar(getAplicacaoCorrente(), AplicacaoType.LISTA);
		}
	}
	
	/**
	 * Retorna a aplicacao do topo da pilha
	 * @return
	 */
	public Aplicacao getAplicacaoCorrente() {
		Aplicacao retorno = null;
		if (getPilhaAplicacaoCorrente().isEmpty()){
			doCarregarDashboard();
		}
	
		final AplicacaoEAplicacaoType aat = getPilhaAplicacaoCorrente().peek(); 
		final Aplicacao a = aat.getAplicacao();
		retorno = a;
	
		return retorno;
	}
	
	/**
	 * Retorna o tipo da aplicação no topo da pilha.
	 * @return
	 */
	public AplicacaoType getAplicacaoTypeCorrente(){
		AplicacaoType retorno = null;
		if (!getPilhaAplicacaoCorrente().isEmpty()){
			final AplicacaoEAplicacaoType aat = getPilhaAplicacaoCorrente().peek(); 
			final AplicacaoType at = aat.getAplicacaoType();
			retorno = at;
		}
		return retorno;
	}
	
	/**
	 * Constroi o ActionMenu.
	 * @param listaMenuItemAdd
	 * @return
	 */
	public MenuModel buildMenu(List<MenuItem> listaMenuItemAdd){
		setDefaultBtnId(null);
		
		// Limpa o menu model atual
		// ------------------------
		final MenuModel model = getActionMenuEmptyMenuModel();
		
		// --------------------------------------
		// FECHAR - AMBOS [CRUD | LISTA]
		// --------------------------------------
		boolean maisDeUmaAplicacao = getPilhaAplicacaoCorrente().size() > 1; // deve ter pelo menos mais de 1 aplicacao aberta 
		final MenuItem miFechar = AplicacaoActionMenuFactory.getInstance().build(getAplicacaoCorrente(), getAplicacaoTypeCorrente(), AplicacaoBotaoId.FECHAR, "#{aplicacaoBean.doProcessarBotaoFechar()}", null, null, new Boolean(maisDeUmaAplicacao), false);
		if (miFechar != null){
			model.addMenuItem(miFechar);
		}

		// --------------------------------------
		// NOVO - AMBOS [CRUD | LISTA]
		// --------------------------------------
		final String actionOnClick = getAplicacaoCorrente().getOnClickActionByAplicacaoBotaoId(AplicacaoBotaoId.NOVO);
		String onClick = null; 
		if (actionOnClick != null){
			onClick = actionOnClick;
		} else {
			onClick = AplicacaoType.CRUD.equals(getAplicacaoTypeCorrente()) ? "confirmationNew.show()" : null;
		}
		
		final String actionExpressao = getAplicacaoCorrente().getExpressaoByAplicacaoBotaoId(AplicacaoBotaoId.NOVO);
		String expressao = null; 
		if (actionExpressao != null){
			expressao = actionExpressao;
		} else {
			expressao = AplicacaoType.CRUD.equals(getAplicacaoTypeCorrente()) ? null : "#{aplicacaoBean.doProcessarBotaoNovo()}";
		}
		
		final MenuItem miNovo = AplicacaoActionMenuFactory.getInstance().build(getAplicacaoCorrente(), getAplicacaoTypeCorrente(), AplicacaoBotaoId.NOVO, expressao, onClick, null, null, false);
		if (miNovo != null){
			model.addMenuItem(miNovo);
		}
		
		// --------------------------------------
		// SALVAR - SOMENTE [CRUD]
		// --------------------------------------
		if (AplicacaoType.CRUD.equals(getAplicacaoTypeCorrente())){
			final MenuItem miSalvar = AplicacaoActionMenuFactory.getInstance().build(getAplicacaoCorrente(), getAplicacaoTypeCorrente(), AplicacaoBotaoId.SALVAR, "#{aplicacaoBean.doProcessarBotaoSalvar()}", null, null, null, false);
			if (miSalvar != null){
				defaultBtnId = miSalvar.getClientId();
				model.addMenuItem(miSalvar);
			}
		}
		
		// --------------------------------------
		// REMOVER - AMBOS [CRUD | LISTA]
		// --------------------------------------
		Boolean temDTOSelecionado = null;
		if (AplicacaoType.LISTA.equals(getAplicacaoTypeCorrente())){
			temDTOSelecionado = getRegistroSelecionado() != null;
		} else if (AplicacaoType.CRUD.equals(getAplicacaoTypeCorrente())){
			if (getAplicacaoCorrente().getBaseDTO() == null || (getAplicacaoCorrente().getBaseDTO() != null && getAplicacaoCorrente().getBaseDTO().isNew())){
				temDTOSelecionado = Boolean.FALSE;
			} else {
				temDTOSelecionado = Boolean.TRUE;
			}
		}
		final MenuItem miExcluir = AplicacaoActionMenuFactory.getInstance().build(getAplicacaoCorrente(), getAplicacaoTypeCorrente(), AplicacaoBotaoId.EXCLUIR, null, "confirmationRemove.show()", null, temDTOSelecionado, false); // "#{aplicacaoBean.doProcessarBotaoExcluir()}" chamado através de confirmation dialog
		if (miExcluir != null){
			model.addMenuItem(miExcluir);
		}

		// Adiciona itens da aplicacao especificamente criados
		// ---------------------------------------------------
		doAdicionarMenuItemDaAplicacao(listaMenuItemAdd, model);
		
		return model;
	}

	public void doNavegarProximo(){
		getAplicacaoCorrente().doNavegarListaDto(Aplicacao.PROXIMO);
 	}
	
	public void doNavegarAnterior(){
		getAplicacaoCorrente().doNavegarListaDto(Aplicacao.ANTERIOR);
	}
	
	/**
	 * Limpa o menu model ou cria um novo.
	 * @return
	 */
	private MenuModel getActionMenuEmptyMenuModel() {
		MenuModel result = null;
		if (getMenuActionModel() != null){
			getMenuActionModel().getContents().clear();
			result = getMenuActionModel();
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
	
	public int getLinhasPorPaginaNaLista(){
		return Aplicacao.LINHAS_POR_PAGINA_NA_LISTA;
	}
	
	// GETTERS && SETTERS
	
	@SuppressWarnings("rawtypes")
	public BaseDTO getRegistroSelecionado() {
		return this.registroSelecionado;
	}

	public void setRegistroSelecionado(@SuppressWarnings("rawtypes") BaseDTO registroSelecionado) {
		this.registroSelecionado = registroSelecionado;
	}
	
	public void setRegistroSelecionadoEDuploClique(@SuppressWarnings("rawtypes") BaseDTO registroSelecionado) {
		this.registroSelecionado = registroSelecionado;
		doProcessarListaDuploClique();
	}

	public MenuModel getMenuActionModel() {
		return this.menuActionModel;
	}

	public void setMenuActionModel(MenuModel menuActionModel) {
		this.menuActionModel = menuActionModel;
	}

	public Stack<AplicacaoEAplicacaoType> getPilhaAplicacaoCorrente() {
		return pilhaAplicacaoCorrente;
	}

	public void setPilhaAplicacaoCorrente(
			Stack<AplicacaoEAplicacaoType> pilhaAplicacaoCorrente) {
		this.pilhaAplicacaoCorrente = pilhaAplicacaoCorrente;
	}

	public String getDefaultBtnId() {
		return defaultBtnId;
	}

	public void setDefaultBtnId(String defaultBtnId) {
		this.defaultBtnId = defaultBtnId;
	}

	public UtilBean getUtilBean() {
		return utilBean;
	}

	public void setUtilBean(UtilBean utilBean) {
		this.utilBean = utilBean;
	}
	 
}