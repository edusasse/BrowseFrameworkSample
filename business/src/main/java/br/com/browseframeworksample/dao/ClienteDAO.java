package br.com.browseframeworksample.dao;

import java.util.List;
import java.util.Map;

import br.com.browseframework.base.crud.dao.CrudDAO;
import br.com.browseframework.base.data.type.CriterionType;
import br.com.browseframework.base.data.type.DataPageType;
import br.com.browseframework.base.data.type.PageType;
import br.com.browseframeworksample.domain.Cliente;
import br.com.browseframeworksample.domain.Usuario;

public interface ClienteDAO extends CrudDAO<Long, Cliente> {
	public final int OPCAO_TODAS = 1;
	public final int OPCAO_PROGRAMADAS = 2;
	public final int OPCAO_EM_ATRASO = 3;
	public final int OPCAO_ACOES_EM_ANDAMENTO = 4;
	public final int OPCAO_SEM_PENDENCIAS = 5;
	public final int OPCAO_ACIMA_DA_META_QUANTIDADE = 6;
	public final int OPCAO_ABAIXO_DA_META_QUANTIDADE = 7;
	public final int OPCAO_ACIMA_DA_META_VALOR = 8;
	public final int OPCAO_ABAIXO_DA_META_VALOR = 9;
	
	public static final String FILTER_DATAFINAL = "datafinal";
	public static final String FILTER_DATAINICIAL = "datainicial";
	public static final String FILTER_IDPESSOA = "idpessoa";
	public static final String FILTER_NOMEPESSOA = "pessoa.nome";
	public static final String FILTER_OPCAO = "opcao";
	
	/**
	 * Lista de clientes na tela de ação.
	 * @param page
	 * @param criteria
	 * @return
	 */
	public DataPageType<Cliente> findClientesByPessoa(PageType page, CriterionType[] criteria);

}
	