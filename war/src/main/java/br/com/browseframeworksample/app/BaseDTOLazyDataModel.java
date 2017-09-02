package br.com.browseframeworksample.app;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

import br.com.browseframework.base.data.DataPage;
import br.com.browseframework.base.data.Filter;
import br.com.browseframework.base.data.Order;
import br.com.browseframework.base.data.Page;
import br.com.browseframework.base.data.dto.BaseDTO;
import br.com.browseframework.base.data.enums.OrderDirection;
import br.com.browseframework.base.data.enums.Restriction;
import br.com.browseframework.base.data.type.CriterionType;
import br.com.browseframework.base.data.type.OrderType;
import br.com.browseframework.base.data.type.PageType;
import br.com.browseframework.base.exception.GenericBusinessException;
import br.com.browseframework.jsfprimefaces.util.FacesUtil;
import br.com.browseframework.util.date.DateFormatterUtil;
import br.com.browseframework.util.reflection.ReflectionUtil;
import br.com.browseframework.util.spring.SpringUtil;

public class BaseDTOLazyDataModel<T extends Serializable> extends LazyDataModel<BaseDTO<T>> {

	private static final long serialVersionUID = -109542474602603668L;

	static final Logger log = Logger.getLogger(BaseDTOLazyDataModel.class);
	
	private Object bean;
	private String nomeMetodo;
	private Object pesquisa;
	private DataPage<BaseDTO<T>> dataPages = null;
	private Page page = new Page();
	private CriterionType[] parametrosPadrao;
	@SuppressWarnings("rawtypes")
	private Class baseDTOClass;
	
	/**
	 * Construtor parametriza LazyDataModel e invoca o método de consulta.
	 * Utiliza o método "findAll" como padrão.
	 * @param facade Facade que fará as chamadas no dao
	 * @param pesquisa
	 */
	public BaseDTOLazyDataModel(@SuppressWarnings("rawtypes") Class baseDTOClass, Object bean, Page page, CriterionType[] parametros) {
		setBaseDTOClass(baseDTOClass);
		setBean(bean);
		// Método padrão
		setNomeMetodo("findAll");
		setFiltersPadrao(parametros);
		setPage(page);
		// Chamada do método de pesquisa
		this.invokeMethod(getPage(), parametros);
	}
	
	/**
	 * Construtor parametriza LazyDataModel e invoca o método de consulta.
	 * @param facade Facade que fará as chamadas no dao
	 * @param nomeMetodo nome do método que segue a estrutura do findAll do CrudFacade.
	 * Deve retornar um DataPage<BaseDTO> e ter a seguinte assinatura:
	 *  - Page, Filter...parametros;
	 * @param pesquisa
	 */
	public BaseDTOLazyDataModel(@SuppressWarnings("rawtypes") Class baseDTOClass, Object bean, String nomeMetodo, Page page, Filter[] parametros) {
		setBaseDTOClass(baseDTOClass);
		setBean(bean);
		// Método padrão
		setNomeMetodo(nomeMetodo);
		setFiltersPadrao(parametros);
		setPage(page);
		// Chamada do método de pesquisa
		// this.invokeMethod(getPage(), parametros);
	}

	/**
	 * Retorna o método na classe do bean informado buscando pelos parâmetros Page e Filter[].
	 * @param clazz
	 * @throws SecurityException
	 */
	@SuppressWarnings("unchecked")
	private Method getMethod(@SuppressWarnings("rawtypes") Class clazz) throws SecurityException {
		Method retorno = null;
		try {
			// Procura encontrar o método na classe informada
			retorno = clazz.getDeclaredMethod(getNomeMetodo(), new Class<?>[]{PageType.class, CriterionType[].class}); 
		} catch (NoSuchMethodException e) {
			retorno = null;
			// caso a excessão estore não é necessário tratar pois a busca continuara na super classe
		}

		// Caso não tenha encontrado método
		if (retorno == null)
			// Caso a super classe não seja Object e nem Class
			if (!(clazz.getSuperclass().equals(Object.class)) && !(clazz.getSuperclass().equals(Class.class))) {
				// realiza chamada recursiva passando a super classe
				return getMethod(clazz.getSuperclass());
			}
		return retorno;
	}
	
	/**
	 * Chamada da consulta no banco de dados
	 * @param p
	 * @return
	 */
	@SuppressWarnings({ "unchecked"})
	public DataPage<BaseDTO<T>> invokeMethod(PageType p, CriterionType[] parametros) {
		Object target = getBean();
		// Proxy
		if(AopUtils.isAopProxy(getBean()) && getBean() instanceof Advised) {
		    try {
				target = SpringUtil.unProxy(getBean());
			} catch (Exception e) {
				target = getBean();
			}
		}

		final Method metodo = getMethod(target.getClass());
		
		if (metodo == null){
			throw new GenericBusinessException("Não foi possível encontrar o método [" + getNomeMetodo() + "] na classe [" + target.getClass().getName() + "]");
		}
		try {
			dataPages = ((DataPage<BaseDTO<T>>) metodo.invoke(target, (PageType) p, (CriterionType[]) parametros));
			if (dataPages != null && !dataPages.getData().isEmpty()) {
				final List<BaseDTO<T>> lista = dataPages.getData();
				dataPages.setData(lista);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} 
		 
		return dataPages;
	}

	/**
	 * 
	 * @param first Linha inicial
	 * @param pageSize Tamanho da página
	 * @param sortField
	 * @param sortOrder
	 * @param filters
	 * @return
	 */
	@SuppressWarnings({ "rawtypes"})
	@Override
	public List<BaseDTO<T>> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
		setPage(new Page());
		// Parametriza Page
		getPage().setPageSize(pageSize);
		getPage().setStartRow(first);
		
		// PARAMETROS - FILTRO
		// -------------------
		// Lista de parametros representando os filtros
		final List<CriterionType> listaFilters = new ArrayList<CriterionType>();
		
		// Itera sobre filtros para montar a lista de parametros
		for(Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
			// Obtém propriedade do filtro
			final String filterProperty = it.next();
			// Recupera valor informado no filtro
			final String filterValue = filters.get(filterProperty);
			log.info("Propriedade [" + filterProperty + "] valor [" + filterValue + "]");
			// Filtro com valor é registrado
			if (filterValue != null && !filterValue.trim().equals("")){
				// Carrega um parametro para a consulta
				final Filter p = getFilter(filterProperty, listaFilters);
				// Carrega tipo no parâmetro
				final Class clazz = carregarTipoFilter(p);
				// Carrega o valor pelo tipo
				carregarValorPorTipoFilter(p, clazz, filterValue);

				log.info("Criado parametro para a propriedade [" + filterProperty + "] valor [" + filterValue + "]");
			}
		}
		
		// PARAMETROS - ORDENACAO
		// ----------------------
		if (sortField != null && !sortField.trim().equals("")){
			final OrderType order = new Order();
			listaFilters.add(order);
			order.setPropertyName(sortField);
			if (sortOrder.equals(SortOrder.ASCENDING)){
				order.setOrderDirection(OrderDirection.ASC);
			} else {
				order.setOrderDirection(OrderDirection.DESC);
			}
		}
		
		// Adiciona os parâmetros padroes a lista por ultimo
		for (CriterionType pd : getFiltersPadrao()){
			listaFilters.add(pd);
		}
		
		// Executa consulta
		final DataPage<BaseDTO<T>> pages = (DataPage<BaseDTO<T>>) invokeMethod(page, listaFilters.toArray(new CriterionType[listaFilters.size()]));	 
		// Numero de linhas
		int dataSize = pages.getCount().intValue();
		this.setRowCount(dataSize);
		
		return pages.getData();
	}
	
	/**
	 * Retorna o parametro caso este ja exista
	 * @param nome
	 * @param listaFilters
	 * @return
	 */
	private Filter getFilter(String nome, final List<CriterionType> listaFilters){
		Filter retorno = null;
		for (CriterionType p : listaFilters){
			if (Filter.class.isInstance(p) && p.getPropertyName().equals(nome)){
				retorno = (Filter) p;
				break;
			}
		}
		if (retorno == null){
			retorno = new Filter();
			listaFilters.add(retorno);
			retorno.setPropertyName(nome);
		}
		
		return retorno;
	}
	
	/**
	 * Carrega o tipo de parâmetro obtendo o atributo da classe DTO informada.
	 * @param p
	 */
	@SuppressWarnings("rawtypes")
	public Class carregarTipoFilter(Filter p){
		Class retorno = null;
		final Field field = ReflectionUtil.getDeclaredFieldForClass(getBaseDTOClass(), p.getPropertyName());
		retorno = field.getType();
		p.setPropertyClassType(retorno); // seta o tipo
		return retorno;
	}
	
	/**
	 * Carrega o valor convertendo para o tipo descoberto.
	 * @param p
	 * @param clazz
	 * @param valor
	 */
	public void carregarValorPorTipoFilter(Filter p, @SuppressWarnings("rawtypes") Class clazz, String valor){
		// Caso carregue uma expressão JSF
		if ((valor.contains("#{") || valor.contains("${")) && valor.contains("}")){
			final Object v = FacesUtil.resolveExpression(valor);
			if (Collection.class.isInstance(v)){
				if (Restriction.EQUALS.equals(p.getRestriction())){
					p.setRestriction(Restriction.IN);
				} else {
					p.setRestriction(Restriction.NOT_IN);
				}
			}
			p.setPropertyValue(v);
		} else if (clazz.isEnum()){
			carregarValorPorTipoFilterEnum(p, clazz, valor);
		} else if (java.util.Date.class.equals(clazz)){
			java.util.Date vl = null;
			valor = valor.trim();
			if (valor.length() == 10){ // dd/MM/yyyy
				try {
					vl = DateFormatterUtil.convertStringToDate(valor, "dd/MM/yyyy");
				} catch (ParseException e) {
					throw new GenericBusinessException("Não foi possível converter a data utilizando a máscara [dd/MM/yyyy]");
				}
			} else if (valor.length() == 16){ // dd/MM/yyyy hh:mm
					try {
						vl = DateFormatterUtil.convertStringToDate(valor, "dd/MM/yyyy hh:mm");
					} catch (ParseException e) {
						throw new GenericBusinessException("Não foi possível converter a data utilizando a máscara [dd/MM/yyyy]");
					}
			} else {
				throw new GenericBusinessException("Não foi possível obter um formato para a data [" + valor + "]");
			}
			p.setPropertyValue(vl);
		} else if (java.lang.String.class.equals(clazz)){
			if (valor != null){
				valor = valor.replaceAll("\\*", "%");
				if (!valor.trim().endsWith("%")){
					valor += "%";
				}
			}
			p.setRestriction(Restriction.LIKE);
			p.setPropertyValue(valor); // Valor que não necessitou de conversão
		} else {
			p.setPropertyValue(valor); // Valor que não necessitou de conversão
		}
		
		 // TODO implementar conversão para outros tipos!
	}
	
	/**
	 * Converte o valor em string para o Enum.
	 * @param p
	 * @param e
	 * @param valor
	 */
	@SuppressWarnings("unchecked")
	protected void carregarValorPorTipoFilterEnum(Filter p, @SuppressWarnings("rawtypes") Class e, String valor){
		try {
			@SuppressWarnings("rawtypes")
			final Enum vl = Enum.valueOf(e, valor);
			p.setPropertyValue(vl);
		} catch (Exception ex){
			throw new GenericBusinessException("Erro ao converter campo filtro [" + p.getPropertyName() + "] utilizando o Enum [" + e.getClass().getName() + "]. Erro [" + ex.getMessage() + "]");
		}
	}
	
	@Override
	public Object getRowKey(BaseDTO<T> object) {
		return object.getId();
	}
	
	@Override
	public BaseDTO<T> getRowData(String rowKey) {
		return super.getRowData(rowKey);
	}
	
	/**
	 * @return the bean
	 */
	public Object getBean() {
		return bean;
	}

	/**
	 * @param bean
	 *            the bean to set
	 */
	public void setBean(Object bean) {
		this.bean = bean;
	}

	/**
	 * @return the nomeMetodo
	 */
	public String getNomeMetodo() {
		return nomeMetodo;
	}

	/**
	 * @param nomeMetodo
	 *            the nomeMetodo to set
	 */
	public void setNomeMetodo(String nomeMetodo) {
		this.nomeMetodo = nomeMetodo;
	}

	/**
	 * @return the pesquisa
	 */
	public Object getPesquisa() {
		return pesquisa;
	}

	/**
	 * @param pesquisa
	 *            the pesquisa to set
	 */
	public void setPesquisa(Object pesquisa) {
		this.pesquisa = pesquisa;
	}
 
	/**
	 * @return the dataPages
	 */
	public DataPage<BaseDTO<T>> getDataPages() {
		return dataPages;
	}

	/**
	 * @param dataPages
	 *            the dataPages to set
	 */
	public void setDataPages(DataPage<BaseDTO<T>> dataPages) {
		this.dataPages = dataPages;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public CriterionType[] getFiltersPadrao() {
		return parametrosPadrao;
	}

	public void setFiltersPadrao(CriterionType[] parametrosPadrao) {
		this.parametrosPadrao = parametrosPadrao;
	}

	@SuppressWarnings("rawtypes")
	public Class getBaseDTOClass() {
		return baseDTOClass;
	}

	public void setBaseDTOClass(@SuppressWarnings("rawtypes") Class baseDTOClass) {
		this.baseDTOClass = baseDTOClass;
	}
  
}