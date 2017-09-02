package br.com.browseframeworksample.app;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIForm;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;

import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.LazyDataModel;

import br.com.browseframeworksample.app.enums.AplicacaoBotaoId;
import br.com.browseframeworksample.app.enums.AplicacaoType;
import br.com.browseframeworksample.app.enums.DiretoriosApp;
import br.com.browseframeworksample.bean.UtilBean;
import br.com.browseframework.base.crud.facade.CrudFacade;
import br.com.browseframework.base.data.Filter;
import br.com.browseframework.base.data.Order;
import br.com.browseframework.base.data.Page;
import br.com.browseframework.base.data.dto.BaseDTO;
import br.com.browseframework.base.data.enums.OrderDirection;
import br.com.browseframework.base.data.enums.Restriction;
import br.com.browseframework.base.data.type.CriterionType;
import br.com.browseframework.base.data.type.DataPageType;
import br.com.browseframework.base.data.type.FilterType;
import br.com.browseframework.base.data.type.OrderType;
import br.com.browseframework.base.exception.GenericBusinessException;
import br.com.browseframework.jsfprimefaces.util.FacesUtil;

public abstract class GenericAppBean<T extends BaseDTO<?>> implements Aplicacao {
	// Util
    @ManagedProperty(value = "#{utilBean}")
	private UtilBean utilBean;
    
	protected String APP_KEY;
	protected Class<?> APP_CLASS;
	protected DiretoriosApp diretorioApp;
	// BaseDTO
    private List<T> listDto;
    private int posicaoListaDto = 0; // inicia do 0 (zero)
    private T dto;
    // Lazy Data Model da lista
    private LazyDataModel<T> listaBaseDTOLazyDataModel = null;
    // Page da lista
    private Page pageLista;
    // Form
    private UIForm uiForm;
    // Campo descricao
    private String campoDescricao = null;
    // Objeto parametro
    private Object parametro;
    
    private String dialogSetProperty;
    
    public GenericAppBean(String APP_KEY, DiretoriosApp diretorioApp, Class<?> APP_CLASS) {
    	this.APP_KEY = APP_KEY;
    	this.diretorioApp = diretorioApp;
    	this.APP_CLASS = APP_CLASS;
    	setPageLista(new Page(0, LINHAS_POR_PAGINA_NA_LISTA));
	}
    
    /**
     * Deve possuir pelo menos um dos três perfis; ADMIN, MANAGER ou USER.
     */
    @Override
    public boolean isAccessGranted() {
    	return getUtilBean().areAnyGranted("ROLE_ADMIN,ROLE_MANAGER,ROLE_USER");
    }
    
    // -----------------------------------------------------
    // Métodos abstratos
    // -----------------------------------------------------
    @SuppressWarnings("rawtypes")
	public abstract CrudFacade getFacade();
    // -----------------------------------------------------

	protected String getAPP_KEY() {
		return this.APP_KEY;
	}

	protected Class<?> getAPP_CLASS() {
		return this.APP_CLASS;
	}
    
	/**
	 * Obtém o nome através da anotação ManagedBean presente, caso não encontre
	 * esta anotação retorna o nome da classe.
	 */
	@Override
	public String getBeanName() {
		String retorno = null;
		
		// Recupera a anotação ManagedBean da classe
		final ManagedBean mb = getClass().getAnnotation(ManagedBean.class);
		if (mb != null){
			// Atribui o nome definido nessa anotação ao retorno
			retorno = mb.name();
		}
		
		// Caso não tenha conseguido obter a anotação retorna o nome da classe
		if (retorno == null){
			retorno = getClass().getName();
		}
		
		return retorno;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.app.Aplicacao#getAppKey()
	 */
	@Override
	public String getAppKey() {
		return getAPP_KEY();
	}
	
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.Aplicacao#getNomeAplicacao()
     */
    @Override
    public String getNomeAplicacao() {
    	return FacesUtil.getResourceBundleString("msgs", "app." + getAPP_KEY());
    }
	
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.Aplicacao#isFecharAoSalvar()
     */
    @Override
    public boolean isFecharAoSalvar() {
    	boolean retorno = FECHAR_AO_SALVAR_PADRAO;
    	// Caso tenha mais DTOs na lista permanece na tela
    	if (getListDto() != null && !getListDto().isEmpty()){
    		retorno = false;
    	}
    	return retorno;
    }
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.Aplicacao#setParametro(java.lang.Object)
     */
	@Override
	public void setParametro(Object parametro) {
		this.parametro = parametro;
	}
	
	/*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.Aplicacao#getCaminho(br.com.browseframeworksample.app.enums.AplicacaoType)
     */
    @Override
	public String getCaminho(AplicacaoType aplicacaoType) {
		String retorno = null;
		if (AplicacaoType.LISTA.equals(aplicacaoType)){
			retorno = diretorioApp.getCaminho()+getAPP_KEY()+"/"+getAPP_KEY()+"ListaApp.xhtml";
		} else if (AplicacaoType.CRUD.equals(aplicacaoType)){
			retorno = diretorioApp.getCaminho()+getAPP_KEY()+"/"+getAPP_KEY()+"CRUDApp.xhtml";
		} else if (AplicacaoType.DIALOG.equals(aplicacaoType)){
			retorno = diretorioApp.getCaminho()+getAPP_KEY()+"/"+getAPP_KEY()+"DialogApp.xhtml";
		}
		
		return retorno;
	}
    
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.app.Aplicacao#getBaseDTOLazyDataModel()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public LazyDataModel getBaseDTOLazyDataModel() {
		return getListaBaseDTOLazyDataModel();
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.app.Aplicacao#setBaseDTOEmEdicao(br.com.browseframework.base.data.dto.BaseDTO)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setBaseDTOEmEdicao(@SuppressWarnings("rawtypes") BaseDTO dto) {
		if (!(getAPP_CLASS().isInstance(dto))){
			throw new GenericBusinessException("Não foi informado um objeto do tipo [" + getAPP_CLASS().getName() +"]");
		} else {
			try {
				if (dto != null && !dto.isNew()){
					getFacade().refresh(dto);
				}
				setDto((T) getAPP_CLASS().cast(dto));
			} catch (ClassCastException e) {
				throw new GenericBusinessException("Não foi informado um objeto do tipo [" + getAPP_CLASS().getName() +"]");
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.app.Aplicacao#getBaseDTO()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public BaseDTO getBaseDTO() {
		return getDto();
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.app.Aplicacao#doProcessarBotaoSalvar()
	 */
	@Override
	public void doProcessarBotaoSalvar() {	
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.app.Aplicacao#save()
	 */
    @SuppressWarnings("unchecked")
	@Override
    public void doSalvar() {
    	if (getDto() != null){
    		setDto( (T) getFacade().save(getDto()));
    		// Adiciona a lista de dtos
    		if (getListDto() != null){
    			getListDto().add(getDto());
    		}
    		if (getDialogSetProperty() != null && getDialogSetProperty().trim().length() > 0){
    			FacesUtil.setExpressionValue(getDialogSetProperty(), getDto());
    		}
    	} else {
    		throw new GenericBusinessException("Não foi encontrado um objeto do tipo  [" + getAPP_CLASS().getName() +"] em edição.");
    	}
    }
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.Aplicacao#doProcessarBotaoNovo()
     */
    @Override
    public void doProcessarBotaoNovo() {
    }
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.Aplicacao#doNovo()
     */
    @SuppressWarnings("unchecked")
	@Override
    public void doNovo() {
    	try {
    		setDto((T) getAPP_CLASS().newInstance());
		} catch (InstantiationException e) {
			throw new GenericBusinessException("Não foi possível criar o objeto do tipo  [" + getAPP_CLASS().getName() +"]");
		} catch (IllegalAccessException e) {
			throw new GenericBusinessException("Não foi possível criar o objeto do tipo  [" + getAPP_CLASS().getName() +"]");
		}
    }

    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.Aplicacao#doProcessarBotaoExcluir()
     */
    @Override
    public void doProcessarBotaoExcluir() {
    }
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.Aplicacao#doExcluir()
     */
    @SuppressWarnings("unchecked")
	@Override
    public void doExcluir() {
    	if (getDto() != null){
    		getFacade().remove(getDto());
    	} else {
    		throw new GenericBusinessException("Não foi encontrado um objeto de Classificação em edição");
    	}
    }

    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.Aplicacao#doProcessarBotaoFechar()
     */
    @Override
    public void doProcessarBotaoFechar() {	
    }
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.Aplicacao#doFechar()
     */
    @Override
    public void doFechar() {
    	// Limpa os campos da aplicacao
    	setListDto(null);
    	setDto(null);
    	setParametro(null);    	
    }
    
    /*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.app.Aplicacao#doCarregar(br.com.browseframeworksample.app.enums.AplicacaoType)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void doCarregar(AplicacaoType aplicacaoType){
		if (AplicacaoType.LISTA.equals(aplicacaoType)){
			CriterionType[] ct = null;
			final String campoDesc = getNomeCampoDescricao();
			if (campoDesc != null){
				final OrderType ot = new Order();
				ot.setOrderDirection(OrderDirection.ASC);
				ot.setPropertyName(campoDesc);
				
				ct = new CriterionType[]{ot};
			} else {
				ct = new CriterionType[]{};
			}
			
			setListaBaseDTOLazyDataModel((LazyDataModel<T>) new BaseDTOLazyDataModel<T>(
					getAPP_CLASS(), 
					getFacade(), 
					getPageLista(), 
					ct));
		}
	} 
	
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.Aplicacao#isMenuItemVisivel(br.com.browseframeworksample.app.enums.AplicacaoBotaoId, br.com.browseframeworksample.app.enums.AplicacaoType)
     */
    @Override
    public boolean isMenuItemVisivel(AplicacaoBotaoId abId, AplicacaoType aplicacaoType) {
    	return true;
    }
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.Aplicacao#isMenuItemHabilitado(br.com.browseframeworksample.app.enums.AplicacaoBotaoId, br.com.browseframeworksample.app.enums.AplicacaoType)
     */
    @Override
    public boolean isMenuItemHabilitado(AplicacaoBotaoId abId, AplicacaoType aplicacaoType) {
    	boolean retorno = false;
    	// Somente quando o CRUD estiver em ação e for um item novo.
    	if (abId.equals(AplicacaoBotaoId.EXCLUIR) && AplicacaoType.CRUD.equals(aplicacaoType)){
    		if (getDto() != null && !getDto().isNew()){
    			retorno = true;
    		}
    	} else {
    		retorno = true;
    	}
    	
    	return retorno;
    }
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.Aplicacao#getForm()
     */
    @Override
    public UIForm getForm() {
    	return getUiForm();
    }
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.Aplicacao#getFormId()
     */
    @Override
    public String getFormId() {
    	return "frm" + getAPP_KEY();
    }
    
    /*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.Aplicacao#setForm(javax.faces.component.UIForm)
     */
    @Override
    public void setForm(UIForm uiForm) {
    	setUiForm(uiForm);
    }
    
    @Override
    public void setDialogSetProperty(String property) {
    	this.dialogSetProperty = property;    	
    }
    
    /**
     * Infere o nome do campo de descrição considerando que ele esta após o Id.
     */
    @Override
    public String getNomeCampoDescricao() {
    	String retorno = this.campoDescricao;
    	// Obtem somente se o campo descrição ainda não foi obtido, pois ele nunca muda durante a execução.
    	if (retorno == null){
    		retorno = getNomeCampoDescricao(getAPP_CLASS());
    	}
    	return retorno;    	
    }
    
	/*
     * (non-Javadoc)
     * @see br.com.browseframeworksample.app.Aplicacao#getMenuItems(br.com.browseframeworksample.app.enums.AplicacaoType)
     */
    @Override
    public List<MenuItem> getMenuItems(AplicacaoType aplicacaoType) {
    	List<MenuItem> retorno = null;
    	if (getListDto() != null && !getListDto().isEmpty() && getListDto().size() > 1){
    		retorno = new ArrayList<MenuItem>();
    		// Anterior
    		final MenuItem miAnterior = new MenuItem();
    		retorno.add(miAnterior);
    		miAnterior.setStyle("color: orange;");    		
    		miAnterior.setId("miAppAnterior");
    		miAnterior.setValue(FacesUtil.getResourceBundleString("msgs", "app.back"));
    		miAnterior.setIcon("ui-icon-circle-arrow-w");
    		miAnterior.setImmediate(false);
    		miAnterior.setActionExpression(FacesUtil.buildMethodExpression("#{aplicacaoBean.doNavegarAnterior()}"));
    		miAnterior.setUpdate("@(.actionMenu) :all");
    		// Proximo
    		final MenuItem miProximo = new MenuItem();
    		retorno.add(miProximo);
    		miProximo.setStyle("color: orange;");
    		miProximo.setId("miAppProximo");
    		miProximo.setValue(FacesUtil.getResourceBundleString("msgs", "app.forward"));
    		miProximo.setIcon("ui-icon-circle-arrow-e");
    		miProximo.setImmediate(false);
    		miProximo.setActionExpression(FacesUtil.buildMethodExpression("#{aplicacaoBean.doNavegarProximo()}"));
    		miProximo.setUpdate("@(.actionMenu) :all");
    	}
    	return retorno;
    }
    
    /**
     * Iplementação que realiza a busca pelo campo de descrição considerando a herança.
     * @param clazz
     * @return
     */
    protected String getNomeCampoDescricao(@SuppressWarnings("rawtypes") Class clazz) {
    	String retorno = null;
    	
    	boolean proximo = false;
		for (java.lang.reflect.Field f : clazz.getDeclaredFields()){
			// Inferindo que a descricao esta apos o Id
			if (f.getAnnotation(Id.class) != null || f.getAnnotation(EmbeddedId.class) != null){
				proximo = true; // marca o proximo como true, assim o proximo campo sera a descrição
			} else {
				if (proximo){
					retorno = f.getName();
					break;
				}
			}
		}
    	
    	// Se nao encontrou o Id e assim não encontrou um campo descritivo
		if (retorno == null){
			// Verifica se a superclasse ainda é um candidato valido
			if (!(clazz.getSuperclass().equals(Object.class)) && !(clazz.getSuperclass().equals(Class.class))) {
				// analisa a clase herdada
				retorno = getNomeCampoDescricao(clazz.getSuperclass());
			}
		}
		
		return retorno;
    }
    
    /**
     * AutoComplete para campo descrição
     * @param filtro
     * @return
     */
	@Override
    public List<T> doAutoComplete(String filtro){
		List<T> retorno = null;
		
		// Obtem o campo descricao
		final String campoDesc = getNomeCampoDescricao();
		if (campoDesc != null){
			// Caso tenha algum valor informado
			if (filtro != null){
				// Força o like como sufixo
				filtro = filtro + "%";
				// Converte o * para um filtro like
				filtro = filtro.replace("*", "%");
			}
			
			final FilterType f1 = new Filter();
			f1.setPropertyName(getNomeCampoDescricao());
			f1.setPropertyValue(filtro);
			f1.setRestriction(Restriction.LIKE);
			
			// Realiza a pesquisa pelo nome
			@SuppressWarnings("unchecked")
			final DataPageType<T> dp = getFacade().findAll(null, new CriterionType[]{f1});
			
			retorno = dp.getData();
		}
		
		return retorno;
	}
    
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.app.Aplicacao#doNavegarListaDto(byte)
	 */
	@Override
	public void doNavegarListaDto(byte direcao) {
		if (Aplicacao.PROXIMO == direcao){
			doProximo();
		} else if (Aplicacao.ANTERIOR == direcao){
			doAnterior();
		}
	}
	
	protected void doProximo(){
		if (getListDto() != null){
			if (getPosicaoListaDto()+1 < getListDto().size()){
				setPosicaoListaDto(getPosicaoListaDto() + 1);
				setBaseDTOEmEdicao(getListDto().get(getPosicaoListaDto()));
				if (getPosicaoListaDto()+1 == getListDto().size()){
					FacesUtil.infoMessage(FacesUtil.getResourceBundleString("msgs", "app.forward.completeMessage"));
				}
			} else {
				FacesUtil.infoMessage(FacesUtil.getResourceBundleString("msgs", "app.forward.completeMessage"));
			}
		}
	}
	
	protected void doAnterior(){
		if (getListDto() != null){
			if (getPosicaoListaDto() > 0){
				setPosicaoListaDto(getPosicaoListaDto() - 1);
				setBaseDTOEmEdicao(getListDto().get(getPosicaoListaDto()));
				if (getPosicaoListaDto() == 0){
					FacesUtil.infoMessage(FacesUtil.getResourceBundleString("msgs", "app.forward.completeMessage"));
				}
			} else {
				FacesUtil.infoMessage(FacesUtil.getResourceBundleString("msgs", "app.back.completeMessage"));
			}
		}
	}
	
	public void setListDto(List<T> listDto) {
		this.listDto = listDto;
		// Zera a posição
		setPosicaoListaDto(0);
		// Carrega o primeiro item na lista no Dto
		if (listDto != null && listDto.size() >= 1){
			setBaseDTOEmEdicao(listDto.get(getPosicaoListaDto()));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.app.Aplicacao#getOnClickActionByAplicacaoBotaoId(br.com.browseframeworksample.app.enums.AplicacaoBotaoId)
	 */
	@Override
	public String getOnClickActionByAplicacaoBotaoId(AplicacaoBotaoId abId) {
		return getOnClickActionByAplicacaoBotaoId(null, abId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.app.Aplicacao#getOnClickActionByAplicacaoBotaoId(br.com.browseframeworksample.app.enums.AplicacaoType, br.com.browseframeworksample.app.enums.AplicacaoBotaoId)
	 */
	@Override
	public String getOnClickActionByAplicacaoBotaoId(AplicacaoType at, AplicacaoBotaoId abId) {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.app.Aplicacao#getExpressaoByAplicacaoBotaoId(br.com.browseframeworksample.app.enums.AplicacaoBotaoId)
	 */
	@Override
	public String getExpressaoByAplicacaoBotaoId(AplicacaoBotaoId abId) {
		return getExpressaoByAplicacaoBotaoId(null, abId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.app.Aplicacao#getExpressaoByAplicacaoBotaoId(br.com.browseframeworksample.app.enums.AplicacaoType, br.com.browseframeworksample.app.enums.AplicacaoBotaoId)
	 */
	@Override
	public String getExpressaoByAplicacaoBotaoId(AplicacaoType at,	AplicacaoBotaoId abId) {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.app.Aplicacao#isExibirActionMenu()
	 */
	@Override
	public boolean isExibirActionMenu() {
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.app.Aplicacao#isTrocaAplicacaoCorrente()
	 */
	@Override
	public boolean isTrocaAplicacaoCorrente() {
		return true;
	}
	
    // GETTERS && SETTERS
	
	public T getDto() {
		return dto;
	}

	public void setDto(T dto) {
		this.dto = dto;
	}

	public LazyDataModel<T> getListaBaseDTOLazyDataModel() {
		return listaBaseDTOLazyDataModel;
	}

	public void setListaBaseDTOLazyDataModel(LazyDataModel<T> listaBaseDTOLazyDataModel) {
		this.listaBaseDTOLazyDataModel = listaBaseDTOLazyDataModel;
	}

	public Page getPageLista() {
		return pageLista;
	}

	public void setPageLista(Page pageLista) {
		this.pageLista = pageLista;
	}

	public UIForm getUiForm() {
		return uiForm;
	}

	public void setUiForm(UIForm uiForm) {
		this.uiForm = uiForm;
	}

	public String getDialogSetProperty() {
		return dialogSetProperty;
	}

	protected Object getParametro() {
		return parametro;
	}

	public List<T> getListDto() {
		return listDto;
	}

	protected int getPosicaoListaDto() {
		return posicaoListaDto;
	}

	protected void setPosicaoListaDto(int posicaoListaDto) {
		this.posicaoListaDto = posicaoListaDto;
	}

	public UtilBean getUtilBean() {
		return utilBean;
	}

	public void setUtilBean(UtilBean utilBean) {
		this.utilBean = utilBean;
	}

}