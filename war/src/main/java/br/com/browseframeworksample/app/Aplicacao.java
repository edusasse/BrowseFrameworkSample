package br.com.browseframeworksample.app;

import java.util.List;

import javax.faces.component.UIForm;

import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.LazyDataModel;

import br.com.browseframeworksample.app.enums.AplicacaoBotaoId;
import br.com.browseframeworksample.app.enums.AplicacaoType;
import br.com.browseframework.base.crud.facade.CrudFacade;
import br.com.browseframework.base.data.dto.BaseDTO;

public interface Aplicacao {
	public static final byte PROXIMO = 0;
	public static final byte ANTERIOR = 1;
	public static final int LINHAS_POR_PAGINA_NA_LISTA = 30;
	public static final boolean FECHAR_AO_SALVAR_PADRAO = true;
	
	public boolean isAccessGranted();
	
	/**
	 * Deve retornar o nome do bean.
	 * @return
	 */
	public String getBeanName();
	
	/**
	 * Código de identificação unico da aplicação.
	 * @return
	 */
	public String getAppKey();
	
	/** 
	 * Nome de apresentação da aplicação
	 * @return
	 */
	public String getNomeAplicacao();
	
	/**
	 * Ação disparada ao processar o botao novo. O método doNovo() esta mais ligado a manipulacao do DTO.
	 */
	public void doProcessarBotaoNovo(); 
	
	/**
	 * Abre um cadastro novo
	 */
	public void doNovo();
	
	/**
	 * Ação disparada ao processar o botao salvar.
	 */
	public void doProcessarBotaoSalvar(); 
	
	/**
	 * Salva o registro em edição
	 */
	public void doSalvar();

	/**
	 * Ação disparada ao processar o botao excluir.
	 */
	public void doProcessarBotaoExcluir();
	
	/**
	 * Exclui o registro em edição
	 */
	public void doExcluir();	
	
	/**
	 * Ação disparada ao processar o botao fechar.
	 */
	public void doProcessarBotaoFechar();
	
	/**
	 * Chama ao Fechar a aplicação.
	 */
	public void doFechar();	
	
	/**
	 * Carrega os valores para a aplicacao ao chamar o programa
	 */
	public void doCarregar(AplicacaoType aplicacaoType);

	/**
	 * Modelo da lista
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public LazyDataModel getBaseDTOLazyDataModel();
	
	/**
	 * Caminho relativo a tela de edição
	 * @return
	 */
	public String getCaminho(AplicacaoType aplicacaoType);
	
	/**
	 * Carrega o DTO em edição
	 * @param dto
	 */
	@SuppressWarnings("rawtypes")
	public void setBaseDTOEmEdicao(BaseDTO dto);
	
	/**
	 * Recupera o DTO em edição
	 * @param dto
	 */
	@SuppressWarnings("rawtypes")
	public BaseDTO getBaseDTO();
	
	/**
	 * Carrega itens de menu extras.
	 * @return
	 */
	public List<MenuItem> getMenuItems(AplicacaoType aplicacaoType);
	
	/**
	 * Caso ao passar o AplicacaoBotaoId e ter um retorno true significa que o
	 * botao não deve ser adicionado para essa aplicação LISTA.
	 * 
	 * @param abId
	 * @return
	 */
	public boolean isMenuItemVisivel(AplicacaoBotaoId abId, AplicacaoType aplicacaoType);
	
	/**
	 * Através da regra de negócio é determinado se o botão esta atvio.
	 * 
	 * @param abId
	 * @return
	 */
	public boolean isMenuItemHabilitado(AplicacaoBotaoId abId, AplicacaoType aplicacaoType);
	
	/**
	 * Retorna o UiForm utilizado para armazenar o formulario em edição.
	 * @return
	 */
	public UIForm getForm();
	
	/**
	 * Seta o UiForm.
	 * @param uiForm
	 * @return
	 */
	public void setForm(UIForm uiForm);
	
	/**
	 * Retorna o id do form.
	 * @return
	 */
	public String getFormId();

	/**
	 * Caminho para setar a propriedade quando a dialog for salva.
	 * @param property
	 */
	public void setDialogSetProperty(String property);
	
	/**
	 * 
	 * @param var
	 * @return
	 */
	public List<?> doAutoComplete(String var);
	
	/**
	 * Retorna o nome de um campo que descreve o objeto visualmente.
	 * @return
	 */
	public String getNomeCampoDescricao();
	
	/**
	 * Facade do DTO.
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public CrudFacade getFacade();
	
	/**
	 * Algum objeto que pode ser passado via parametro para a aplicacao ao carregar.
	 * @param par
	 */
	public void setParametro(Object par);
	
	/**
	 * Retornando true o programa é fechado após salvar com sucesso.
	 * @return
	 */
	public boolean isFecharAoSalvar();
	
	/**
	 * Realiza a navegação em uma lista de Dtos no Crud.
	 * @param direcao
	 */
	public void doNavegarListaDto(byte direcao);
	
	public String getOnClickActionByAplicacaoBotaoId(AplicacaoBotaoId abId);
	
	public String getOnClickActionByAplicacaoBotaoId(AplicacaoType at, AplicacaoBotaoId abId);
	
	public String getExpressaoByAplicacaoBotaoId(AplicacaoBotaoId abId);
	
	public String getExpressaoByAplicacaoBotaoId(AplicacaoType at, AplicacaoBotaoId abId);
	
	public boolean isExibirActionMenu();
	
	public boolean isTrocaAplicacaoCorrente();
	
}