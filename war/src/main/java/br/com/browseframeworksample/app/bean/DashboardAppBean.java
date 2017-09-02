package br.com.browseframeworksample.app.bean;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIForm;

import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import br.com.browseframeworksample.app.Aplicacao;
import br.com.browseframeworksample.app.enums.AplicacaoBotaoId;
import br.com.browseframeworksample.app.enums.AplicacaoType;
import br.com.browseframeworksample.bean.UtilBean;
import br.com.browseframeworksample.domain.UnidadeFederacao;
import br.com.browseframeworksample.facade.ClassificacaoFacade;
import br.com.browseframeworksample.facade.ClienteFacade;
import br.com.browseframeworksample.facade.ParametroFacade;
import br.com.browseframework.base.crud.facade.CrudFacade;
import br.com.browseframework.base.data.dto.BaseDTO;

@ManagedBean(name = "dashboardAppBean")
@SessionScoped
public class DashboardAppBean implements Aplicacao {

	// Acesso ao Facade do classificacao
	@ManagedProperty(value = "#{utilBean}")
	private UtilBean utilBean;
	
	// Acesso ao Facade do classificacao
	@ManagedProperty(name = "classificacaoFacade", value = "#{classificacaoFacade}")
	private ClassificacaoFacade classificacaoFacade;

	// Acesso ao Facade do classificacao
	@ManagedProperty(name = "clienteFacade", value = "#{clienteFacade}")
	private ClienteFacade clienteFacade;

	// Acesso ao Facade de parametros
	@ManagedProperty(name = "parametroFacade", value = "#{parametroFacade}")
	private ParametroFacade parametroFacade;
	
	private CartesianChartModel categoryModelQuantidade;
	private CartesianChartModel categoryModelValor;

	// Form
	private UIForm uiForm;

	public DashboardAppBean() {	}

	@Override
	public boolean isAccessGranted() {
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.browseframeworksample.app.Aplicacao#doCarregar(br.com.
	 * browseframeworksample.app.enums.AplicacaoType)
	 */
	@Override
	public void doCarregar(AplicacaoType aplicacaoType) {
		buildDashboard();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.browseframeworksample.app.Aplicacao#isFecharAoSalvar()
	 */
	@Override
	public boolean isFecharAoSalvar() {
		return FECHAR_AO_SALVAR_PADRAO;
	}

	@Override
	public String getNomeCampoDescricao() {
		return "dashboardAppBean";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.browseframeworksample.app.Aplicacao#getNomeAplicacao()
	 */
	@Override
	public String getNomeAplicacao() {
		return "Dashboard";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.browseframeworksample.app.Aplicacao#getCaminho(br.com.
	 * browseframeworksample.app.enums.AplicacaoType)
	 */
	@Override
	public String getCaminho(AplicacaoType aplicacaoType) {
		String retorno = null;
		if (AplicacaoType.DASHBOARD.equals(aplicacaoType)) {
			retorno = "/secured/apps/dashboard/dashboardApp.xhtml";
		}

		return retorno;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.browseframeworksample.app.Aplicacao#getFormId()
	 */
	@Override
	public String getFormId() {
		return "frmDashboard";
	}

	@Override
	public boolean isTrocaAplicacaoCorrente() {
		return true;
	}

	/**
	 * Cria gráfico de meta por quantidade.
	 */
	public void buildDashboard() {
		// TODO
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.browseframeworksample.app.Aplicacao#isExibirActionMenu()
	 */
	@Override
	public boolean isExibirActionMenu() {
		return false;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public CrudFacade getFacade() {
		return null;
	}

	@Override
	public void doFechar() {
	}

	@Override
	public String getOnClickActionByAplicacaoBotaoId(AplicacaoBotaoId abId) {
		return null;
	}

	@Override
	public void doNavegarListaDto(byte direcao) {
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BaseDTO getBaseDTO() {
		return null;
	}

	@Override
	public String getExpressaoByAplicacaoBotaoId(AplicacaoType at,
			AplicacaoBotaoId abId) {
		return null;
	}

	@Override
	public String getOnClickActionByAplicacaoBotaoId(AplicacaoType at,
			AplicacaoBotaoId abId) {
		return null;
	}

	@Override
	public String getBeanName() {
		return null;
	}

	@Override
	public String getAppKey() {
		return null;
	}

	@Override
	public List<UnidadeFederacao> doAutoComplete(String filtro) {
		List<UnidadeFederacao> retorno = null;
		return retorno;
	}

	@Override
	public void setParametro(Object par) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.browseframeworksample.app.Aplicacao#getBaseDTOLazyDataModel()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public LazyDataModel getBaseDTOLazyDataModel() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.browseframeworksample.app.Aplicacao#setBaseDTOEmEdicao(br.com.
	 * browseframework.base.data.dto.BaseDTO)
	 */
	@Override
	public void setBaseDTOEmEdicao(@SuppressWarnings("rawtypes") BaseDTO dto) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.browseframeworksample.app.Aplicacao#save()
	 */
	@Override
	public void doSalvar() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.browseframeworksample.app.Aplicacao#doNovo()
	 */
	@Override
	public void doNovo() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.browseframeworksample.app.Aplicacao#getMenuItems(br.com.
	 * browseframeworksample.app.enums.AplicacaoType)
	 */
	@Override
	public List<MenuItem> getMenuItems(AplicacaoType aplicacaoType) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.browseframeworksample.app.Aplicacao#doExcluir()
	 */
	@Override
	public void doExcluir() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.browseframeworksample.app.Aplicacao#isMenuItemVisivel(br.com.
	 * browseframeworksample.app.enums.AplicacaoBotaoId,
	 * br.com.browseframeworksample.app.enums.AplicacaoType)
	 */
	@Override
	public boolean isMenuItemVisivel(AplicacaoBotaoId abId,
			AplicacaoType aplicacaoType) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.browseframeworksample.app.Aplicacao#isMenuItemHabilitado(br.com
	 * .browseframeworksample.app.enums.AplicacaoBotaoId,
	 * br.com.browseframeworksample.app.enums.AplicacaoType)
	 */
	@Override
	public boolean isMenuItemHabilitado(AplicacaoBotaoId abId,
			AplicacaoType aplicacaoType) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.browseframeworksample.app.Aplicacao#getForm()
	 */
	@Override
	public UIForm getForm() {
		return getUiForm();
	}

	@Override
	public String getExpressaoByAplicacaoBotaoId(AplicacaoBotaoId abId) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.browseframeworksample.app.Aplicacao#setForm(javax.faces.component
	 * .UIForm)
	 */
	@Override
	public void setForm(UIForm uiForm) {
		setUiForm(uiForm);
	}

	// GETTERS && SETTERS

	public ClassificacaoFacade getClassificacaoFacade() {
		return classificacaoFacade;
	}

	public void setClassificacaoFacade(ClassificacaoFacade classificacaoFacade) {
		this.classificacaoFacade = classificacaoFacade;
	}

	public UIForm getUiForm() {
		return uiForm;
	}

	public void setUiForm(UIForm uiForm) {
		this.uiForm = uiForm;
	}

	@Override
	public void setDialogSetProperty(String property) {
	}

	@Override
	public void doProcessarBotaoNovo() {
	}

	@Override
	public void doProcessarBotaoSalvar() {
	}

	@Override
	public void doProcessarBotaoExcluir() {
	}

	@Override
	public void doProcessarBotaoFechar() {
	}

	public ClienteFacade getClienteFacade() {
		return clienteFacade;
	}

	public void setClienteFacade(ClienteFacade clienteFacade) {
		this.clienteFacade = clienteFacade;
	}

	public CartesianChartModel getCategoryModelQuantidade() {
		return categoryModelQuantidade;
	}

	public void setCategoryModelQuantidade(
			CartesianChartModel categoryModelQuantidade) {
		this.categoryModelQuantidade = categoryModelQuantidade;
	}

	public CartesianChartModel getCategoryModelValor() {
		return categoryModelValor;
	}

	public void setCategoryModelValor(CartesianChartModel categoryModelValor) {
		this.categoryModelValor = categoryModelValor;
	}

	public ParametroFacade getParametroFacade() {
		return parametroFacade;
	}

	public void setParametroFacade(ParametroFacade parametroFacade) {
		this.parametroFacade = parametroFacade;
	}

	public UtilBean getUtilBean() {
		return utilBean;
	}

	public void setUtilBean(UtilBean utilBean) {
		this.utilBean = utilBean;
	}
 

}