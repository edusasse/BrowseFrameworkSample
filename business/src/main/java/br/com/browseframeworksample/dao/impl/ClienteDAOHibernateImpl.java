package br.com.browseframeworksample.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import br.com.browseframework.base.crud.paging.Pagination;
import br.com.browseframework.base.data.DataPage;
import br.com.browseframework.base.data.type.CriterionType;
import br.com.browseframework.base.data.type.DataPageType;
import br.com.browseframework.base.data.type.FilterType;
import br.com.browseframework.base.data.type.PageType;
import br.com.browseframework.base.data.type.util.CriterionTypeUtil;
import br.com.browseframework.base.exception.GenericBusinessException;
import br.com.browseframework.hibernate.dao.impl.CrudDAOHibernateImpl;
import br.com.browseframeworksample.dao.ClienteDAO;
import br.com.browseframeworksample.domain.Cliente;
import br.com.browseframeworksample.domain.Usuario;
import br.com.browseframeworksample.domain.enums.SituacaoCliente;
import br.com.browseframeworksample.domain.to.ClienteTO;

@Repository(value = "clienteDAO")
public class ClienteDAOHibernateImpl extends CrudDAOHibernateImpl<Long, Cliente> implements ClienteDAO {	
	
	@Autowired
	private Pagination pagingTemplate;
	
	@Autowired
	public ClienteDAOHibernateImpl(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.hibernate.dao.impl.CrudDAOHibernateImpl#criteriaFindAll(org.hibernate.Criteria)
	 */
	@Override
	protected void criteriaFindAll(Criteria criteria) {
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.hibernate.dao.impl.CrudDAOHibernateImpl#criteriaFindAllCount(org.hibernate.Criteria)
	 */
	@Override
	protected void criteriaFindAllCount(Criteria criteria) {
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.hibernate.dao.impl.CrudDAOHibernateImpl#criteriaFindById(org.hibernate.Criteria)
	 */
	@Override
	protected void criteriaFindById(Criteria criteria) {
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.browseframework.hibernate.dao.impl.CrudDAOHibernateImpl#getConcreteId(java.io.Serializable)
	 */
	@Override
	protected Long getConcreteId(Long id) {
		return id;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.browseframeworksample.dao.ClienteDAO#findClientesByPessoa(br.com.browseframework.base.data.type.PageType, br.com.browseframework.base.data.type.CriterionType[])
	 */
	public DataPageType<Cliente> findClientesByPessoa(PageType page, CriterionType[] criteria) {
		DataPageType<Cliente> retorno = null;
		
		// Mapa de paramentros pelo nome
		final Map<String, CriterionType> map = CriterionTypeUtil.getCriterionTypeMap(criteria);
		if (map == null){
			throw new GenericBusinessException("Não foi possível gerar mapa de Parâmetros!");
		}
 
		Session session = null;
		try {
			session = getCurrentSession();
			
			final StringBuilder sql = new StringBuilder();
			sql.append("	select ");
			sql.append("		{c.*} ");
			sql.append("	from ");
			sql.append("		cliente as c ");
			sql.append("		    inner join pessoa as p ");
			sql.append("		    on c.cod_pessoa = p.cod_pessoa ");
			sql.append("    where c.ind_situacao = '" + SituacaoCliente.A.name() + "' ");
			
			if (map.containsKey(FILTER_IDPESSOA) && ((FilterType) map.get(FILTER_IDPESSOA)).getPropertyValue() != null){
				sql.append("        and c.cod_cliente in (select ");
				sql.append("   							cod_cliente "); 
				sql.append("  						 from ");
				sql.append("   							equipe_cliente "); 
				sql.append("   						where ");
				sql.append("   							cod_pessoa = :idpessoa ) ");
			}
			
			if (map.containsKey(FILTER_NOMEPESSOA) && ((FilterType) map.get(FILTER_NOMEPESSOA)).getPropertyValue() != null){
				sql.append("        and p.nom_pessoa like :nomepessoa ");
			}
			
			boolean hasParametroIntervaloDeDatas = map.containsKey(FILTER_DATAINICIAL) && ((FilterType) map.get(FILTER_DATAINICIAL)).getPropertyValue() != null && map.containsKey(FILTER_DATAFINAL) && ((FilterType) map.get(FILTER_DATAFINAL)).getPropertyValue() != null;
			if (hasParametroIntervaloDeDatas){
				sql.append("    /* CLIENTE DEVE POSSUIR PERIODO NO INTERVALO DE DATAS INFORMADO */");
				sql.append("    and (exists (select 1 ");
				sql.append("                from cliente_periodo_fechamento ");
				sql.append("                where 1=1 ");
				sql.append("                and cod_cliente = c.cod_cliente ");
				if (hasParametroIntervaloDeDatas){
					sql.append("                and dat_inicio >= :datainicial and dat_fim <= :datafinal)");
				}
				sql.append("    /* OU O CLIENTE NAO DEVE POSSUIR UM PERIODO */");
				sql.append("    or not exists (select 1 ");
				sql.append("                from cliente_periodo_fechamento ");
				sql.append("                where 1=1 ");
				sql.append("                and cod_cliente = c.cod_cliente) ");
				sql.append(" ) ");
			}
			
			if (map.containsKey(FILTER_OPCAO) && ((FilterType) map.get(FILTER_OPCAO)).getPropertyValue() != null){
				String opcaos = (String) ((FilterType) map.get(FILTER_OPCAO)).getPropertyValue();
				int opcao = Integer.parseInt(opcaos);
				// PROGRAMADAS
				// ------------
				if (opcao == ClienteDAO.OPCAO_PROGRAMADAS){
					sql.append("/* EXISTAM AÇÕES NA SITUACAO PLANEJADA */ ");
					sql.append(" and exists ");
					sql.append("(	select ");
					sql.append("        1 ");
					sql.append("    from ");
					sql.append("        acao as a ");
					sql.append("            inner join cliente_periodo_fechamento as cpf ");
					sql.append("            on a.cod_cliente_periodo_fechamento = cpf.cod_cliente_periodo_fechamento ");
					sql.append("            and cpf.dat_inicio = (	select ");
					sql.append("                                                max(dat_inicio)  ");
					sql.append("                                            from ");
					sql.append("                                                cliente_periodo_fechamento  ");
					sql.append("                                            where ");
					sql.append("                                                cod_cliente = cpf.cod_cliente ");
					if (hasParametroIntervaloDeDatas){
						sql.append("                                                and dat_inicio >= :datainicial and dat_fim <= :datafinal ");
					}
					sql.append("                    ) ");
					sql.append("    where ");
					sql.append("        1=1 ");
					sql.append("        and a.cod_cliente = c.cod_cliente  ");
					
					sql.append("        /* SOMENTE AÇÕES PLANEJADAS*/ ");
					sql.append("        and a.ind_situacao= 'P'  ");
					if (hasParametroIntervaloDeDatas){
						sql.append("        /* FILTRA O PERIODO INFORMADO EM TELA */ ");
						sql.append("        and cpf.dat_inicio >= :datainicial ");
						sql.append("        and cpf.dat_fim <= :datafinal ");
					}
					sql.append(") ");
				} else
					// EM ATRASO
					// --------------------
					if (opcao == ClienteDAO.OPCAO_EM_ATRASO){
						sql.append("/* EXISTAM AÇÕES NA SITUACAO PLANEJADA E ABERTA QUE TENHAM UM PREVISAO E JA EXTOURARAM A MESMA */ ");
						sql.append(" and exists ");
						sql.append("(	select ");
						sql.append("        1 ");
						sql.append("    from ");
						sql.append("        acao as a ");
						sql.append("            inner join cliente_periodo_fechamento as cpf ");
						sql.append("            on a.cod_cliente_periodo_fechamento = cpf.cod_cliente_periodo_fechamento ");
						sql.append("            and cpf.dat_inicio = (	select ");
						sql.append("                                                max(dat_inicio)  ");
						sql.append("                                            from ");
						sql.append("                                                cliente_periodo_fechamento  ");
						sql.append("                                            where ");
						sql.append("                                                cod_cliente = cpf.cod_cliente ");
						if (hasParametroIntervaloDeDatas){
							sql.append("                                                and dat_inicio >= :datainicial and dat_fim <= :datafinal ");
						}
						sql.append("                    ) ");
						sql.append("    where ");
						sql.append("        1=1 ");
						sql.append("        and a.cod_cliente = c.cod_cliente  ");
						
						sql.append("        /* AÇÕES ABERTAS E PLANEJADAS */ ");
						sql.append("        and a.ind_situacao in ('A','P') ");
						
						sql.append("		/* DATA DE ENCERRAMENTO PREVISTO PREENCHIDO */ ");
						sql.append("		and dat_encerramento_previsto is not null ");
						
						sql.append("		/* DATA DE ENCERRAMENTO PREVISTO ANTERIOR A DATA CORRENTE */ ");
						sql.append("		and dat_encerramento_previsto < now() ");
						if (hasParametroIntervaloDeDatas){
							sql.append("        /* FILTRA O PERIODO INFORMADO EM TELA */ ");
							sql.append("        and cpf.dat_inicio >= :datainicial ");
							sql.append("        and cpf.dat_fim <= :datafinal ");
						}
						sql.append(") ");
				} else
					// EM ANDAMENTO
					// --------------------
					if (opcao == ClienteDAO.OPCAO_ACOES_EM_ANDAMENTO){
						sql.append("/* EXISTAM AÇÕES NA SITUACAO PLANEJADA E ABERTA */ ");
						sql.append(" and exists ");
						sql.append("(	select ");
						sql.append("        1 ");
						sql.append("    from ");
						sql.append("        acao as a ");
						sql.append("            inner join cliente_periodo_fechamento as cpf ");
						sql.append("            on a.cod_cliente_periodo_fechamento = cpf.cod_cliente_periodo_fechamento ");
						sql.append("            and cpf.dat_inicio = (	select ");
						sql.append("                                                max(dat_inicio)  ");
						sql.append("                                            from ");
						sql.append("                                                cliente_periodo_fechamento  ");
						sql.append("                                            where ");
						sql.append("                                                cod_cliente = cpf.cod_cliente ");
						if (hasParametroIntervaloDeDatas){
							sql.append("                                                and dat_inicio >= :datainicial and dat_fim <= :datafinal ");
						}
						sql.append("                    ) ");
						sql.append("    where ");
						sql.append("        1=1 ");
						sql.append("        and a.cod_cliente = c.cod_cliente  ");
						
						sql.append("        /* AÇÕES ABERTAS E PLANEJADAS */ ");
						sql.append("        and a.ind_situacao in ('A','P') ");
						if (hasParametroIntervaloDeDatas){
							sql.append("        /* FILTRA O PERIODO INFORMADO EM TELA */ ");
							sql.append("        and cpf.dat_inicio >= :datainicial ");
							sql.append("        and cpf.dat_fim <= :datafinal ");
						}
						sql.append(") ");
				} else
					// SEM PENDENCIAS
					// --------------------
					if (opcao == ClienteDAO.OPCAO_SEM_PENDENCIAS){
						sql.append("/* ONDE NÃO EXISTAM AÇÕES NA SITUACAO PLANEJADA E ABERTA */ ");
						sql.append(" and not exists ");
						sql.append("(	select ");
						sql.append("        1 ");
						sql.append("    from ");
						sql.append("        acao as a ");
						sql.append("            inner join cliente_periodo_fechamento as cpf ");
						sql.append("            on a.cod_cliente_periodo_fechamento = cpf.cod_cliente_periodo_fechamento ");
						sql.append("            and cpf.dat_inicio = (	select ");
						sql.append("                                                max(dat_inicio)  ");
						sql.append("                                            from ");
						sql.append("                                                cliente_periodo_fechamento  ");
						sql.append("                                            where ");
						sql.append("                                                cod_cliente = cpf.cod_cliente ");
						if (hasParametroIntervaloDeDatas){
							sql.append("                                                and dat_inicio >= :datainicial and dat_fim <= :datafinal ");
						}
						sql.append("                    ) ");
						sql.append("    where ");
						sql.append("        1=1 ");
						sql.append("        and a.cod_cliente = c.cod_cliente  ");
						
						sql.append("        /* AÇÕES ABERTAS E PLANEJADAS */ ");
						sql.append("        and a.ind_situacao in ('A', 'P') ");
						
						if (hasParametroIntervaloDeDatas){
							sql.append("        /* FILTRA O PERIODO INFORMADO EM TELA */ ");
							sql.append("        and cpf.dat_inicio >= :datainicial ");
							sql.append("        and cpf.dat_fim <= :datafinal ");
						}
						sql.append(") ");
				} else
					// ACIMA DA META QTD
					// --------------------
					if (opcao == ClienteDAO.OPCAO_ACIMA_DA_META_QUANTIDADE){
						sql.append("/* EXISTAM AÇÕES NA SITUACAO PLANEJADA, ABERTA OU ENCERRADA E O TOTAL DE AÇÕES SEJA MAIOR OU IGUAL A META DO PERIODO */ ");
						sql.append(" and  exists ");
						sql.append("(	select ");
						sql.append("        1 ");
						sql.append("    from ");
						sql.append("        acao as a ");
						sql.append("            inner join cliente_periodo_fechamento as cpf ");
						sql.append("            on a.cod_cliente_periodo_fechamento = cpf.cod_cliente_periodo_fechamento ");
						sql.append("            and cpf.dat_inicio = (	select ");
						sql.append("                                                max(dat_inicio)  ");
						sql.append("                                            from ");
						sql.append("                                                cliente_periodo_fechamento  ");
						sql.append("                                            where ");
						sql.append("                                                cod_cliente = cpf.cod_cliente ");
						if (hasParametroIntervaloDeDatas){
							sql.append("                                                and dat_inicio >= :datainicial and dat_fim <= :datafinal ");
						}
						sql.append("                    ) ");
						sql.append("    where ");
						sql.append("        1=1 ");
						sql.append("        and a.cod_cliente = c.cod_cliente  ");
						
						sql.append("        /* SOMENTE AÇÕES ABERTAS, PLANEJADAS E ENCERRADAS*/ ");
						sql.append("        and a.ind_situacao in ('A', 'P', 'R') ");
						
						if (hasParametroIntervaloDeDatas){
							sql.append("        /* FILTRA O PERIODO INFORMADO EM TELA */ ");
							sql.append("        and cpf.dat_inicio >= :datainicial ");
							sql.append("        and cpf.dat_fim <= :datafinal ");
						}
						sql.append(" group by cpf.qtd_meta_acoes ");
						sql.append(" having count(1) >= cpf.qtd_meta_acoes ");
						sql.append(") ");
				} else
					// ABAIXO DA META QTD
					// --------------------
					if (opcao == ClienteDAO.OPCAO_ABAIXO_DA_META_QUANTIDADE){
						sql.append("/* EXISTAM AÇÕES NA SITUACAO PLANEJADA, ABERTA OU ENCERRADA E O TOTAL DE AÇÕES SEJA MENOR A META DO PERIODO */ ");
						sql.append(" and exists ");
						sql.append("(	select ");
						sql.append("        1 ");
						sql.append("    from ");
						sql.append("        acao as a ");
						sql.append("            inner join cliente_periodo_fechamento as cpf ");
						sql.append("            on a.cod_cliente_periodo_fechamento = cpf.cod_cliente_periodo_fechamento ");
						sql.append("            and cpf.dat_inicio = (	select ");
						sql.append("                                                max(dat_inicio)  ");
						sql.append("                                            from ");
						sql.append("                                                cliente_periodo_fechamento  ");
						sql.append("                                            where ");
						sql.append("                                                cod_cliente = cpf.cod_cliente ");
						if (hasParametroIntervaloDeDatas){
							sql.append("                                                and dat_inicio >= :datainicial and dat_fim <= :datafinal ");
						}
						sql.append("                    ) ");
						sql.append("    where ");
						sql.append("        1=1 ");
						sql.append("        and a.cod_cliente = c.cod_cliente  ");
						
						sql.append("        /* SOMENTE AÇÕES ABERTAS, PLANEJADAS E ENCERRADAS*/ ");
						sql.append("        and a.ind_situacao in ('A', 'P', 'R') ");
						
						if (hasParametroIntervaloDeDatas){
							sql.append("        /* FILTRA O PERIODO INFORMADO EM TELA */ ");
							sql.append("        and cpf.dat_inicio >= :datainicial ");
							sql.append("        and cpf.dat_fim <= :datafinal ");
						}
						sql.append(" group by cpf.qtd_meta_acoes ");
						sql.append(" having count(1) < cpf.qtd_meta_acoes ");
						sql.append(") ");
				} else
					// ACIMA DA META VALOR
					// --------------------
					if (opcao == ClienteDAO.OPCAO_ACIMA_DA_META_VALOR){
						sql.append("/* EXISTAM AÇÕES NA SITUACAO PLANEJADA, ABERTA OU ENCERRADA E O TOTAL DE AÇÕES SEJA MAIOR OU IGUAL A META DO PERIODO */ ");
						sql.append(" and  exists ");
						sql.append("(	select ");
						sql.append("        1 ");
						sql.append("    from ");
						sql.append("        acao as a ");
						sql.append("            inner join cliente_periodo_fechamento as cpf ");
						sql.append("            on a.cod_cliente_periodo_fechamento = cpf.cod_cliente_periodo_fechamento ");
						sql.append("            and cpf.dat_inicio = (	select ");
						sql.append("                                                max(dat_inicio)  ");
						sql.append("                                            from ");
						sql.append("                                                cliente_periodo_fechamento  ");
						sql.append("                                            where ");
						sql.append("                                                cod_cliente = cpf.cod_cliente ");
						if (hasParametroIntervaloDeDatas){
							sql.append("                                                and dat_inicio >= :datainicial and dat_fim <= :datafinal ");
						}
						sql.append("                    ) ");
						sql.append("    where ");
						sql.append("        1=1 ");
						sql.append("        and a.cod_cliente = c.cod_cliente  ");
						
						sql.append("        /* SOMENTE AÇÕES ABERTAS, PLANEJADAS E ENCERRADAS*/ ");
						sql.append("        and a.ind_situacao in ('A', 'P', 'R') ");
						
						if (hasParametroIntervaloDeDatas){
							sql.append("        /* FILTRA O PERIODO INFORMADO EM TELA */ ");
							sql.append("        and cpf.dat_inicio >= :datainicial ");
							sql.append("        and cpf.dat_fim <= :datafinal ");
						}
						sql.append(" group by cpf.val_meta_acoes ");
						sql.append(" having sum(a.val_anuncio) >= cpf.val_meta_acoes ");
						sql.append(") ");
				} else
					// ABAIXO DA META VALOR
					// --------------------
					if (opcao == ClienteDAO.OPCAO_ABAIXO_DA_META_VALOR){
						sql.append("/* EXISTAM AÇÕES NA SITUACAO PLANEJADA, ABERTA OU ENCERRADA E O TOTAL DE AÇÕES SEJA MENOR A META DO PERIODO */ ");
						sql.append(" and exists ");
						sql.append("(	select ");
						sql.append("        1 ");
						sql.append("    from ");
						sql.append("        acao as a ");
						sql.append("            inner join cliente_periodo_fechamento as cpf ");
						sql.append("            on a.cod_cliente_periodo_fechamento = cpf.cod_cliente_periodo_fechamento ");
						sql.append("            and cpf.dat_inicio = (	select ");
						sql.append("                                                max(dat_inicio)  ");
						sql.append("                                            from ");
						sql.append("                                                cliente_periodo_fechamento  ");
						sql.append("                                            where ");
						sql.append("                                                cod_cliente = cpf.cod_cliente ");
						if (hasParametroIntervaloDeDatas){
							sql.append("                                                and dat_inicio >= :datainicial and dat_fim <= :datafinal ");
						}
						sql.append("                    ) ");
						sql.append("    where ");
						sql.append("        1=1 ");
						sql.append("        and a.cod_cliente = c.cod_cliente  ");
						
						sql.append("        /* SOMENTE AÇÕES ABERTAS, PLANEJADAS E ENCERRADAS*/ ");
						sql.append("        and a.ind_situacao in ('A', 'P', 'R') ");
						
						if (hasParametroIntervaloDeDatas){
							sql.append("        /* FILTRA O PERIODO INFORMADO EM TELA */ ");
							sql.append("        and cpf.dat_inicio >= :datainicial ");
							sql.append("        and cpf.dat_fim <= :datafinal ");
						}
						sql.append(" group by cpf.val_meta_acoes ");
						sql.append(" having sum(a.val_anuncio) < cpf.val_meta_acoes ");
						sql.append(") ");
				}
			}
			// Adiciona a ordenação
			sql.append(" order by p.nom_pessoa ASC ");
			
			// ----------------------------------------------------------------------------------------------
			// EXECUCAO DA QUERY DE CONTAGEM
			// Obs.: Não foi possível utilizar SQL_CALC_FOUND_ROWS com SELECT FOUND_ROWS(); por que o hibernate
			//       complementa o objeto realizando outra query antes que se possa executar o FOUND_ROWS()
			// ----------------------------------------------------------------------------------------------
			// Conta a quantidade de linhas
			final String countQuery = "SELECT COUNT(1) AS NRO_LINHAS FROM (" + sql.toString().replaceAll("\\{c.*}", "1") + ") AS D";
			final SQLQuery queryCount = session.createSQLQuery(countQuery);
			doLoadParameters(map, hasParametroIntervaloDeDatas, queryCount);
			
			// ----------------------------------------------------------------------------------------------
			// EXECUCAO DA QUERY
			// ----------------------------------------------------------------------------------------------		
			// Passa pelo template de paginaçao
			final String finalQuery = pagingTemplate.pageQuery(sql.toString(), page);
			// Cria query
			final SQLQuery query = session.createSQLQuery(finalQuery);
			query.addEntity("c", Cliente.class);
			// Carrega parametros
			doLoadParameters(map, hasParametroIntervaloDeDatas, query);
			
			// ----------------------------------------------------------
			// Executa a queries
			// ----------------------------------------------------------
			@SuppressWarnings({ "unchecked" })
			final List<Cliente> data = query.list();
			final Number n = (Number) queryCount.uniqueResult();
			
			// ----------------------------------------------------------
			// TO - Complementa as informações 
			// ----------------------------------------------------------
			// ----------------------------------------------------------
			
			retorno = new DataPage<Cliente>(data, page);
			if (n != null){
				retorno.setCount(n);
			}
		} finally {
			;
		}
		return retorno;
	}

	protected void doLoadParameters(final Map<String, CriterionType> map,
			boolean hasParametroIntervaloDeDatas, final SQLQuery queryCount) {
		// Seta valor ao parametro
		// ------------------------------
		// -> Id Pessoa
		if (map.containsKey(FILTER_IDPESSOA) && ((FilterType) map.get(FILTER_IDPESSOA)).getPropertyValue() != null){
			queryCount.setParameter(FILTER_IDPESSOA, (Long) ((FilterType) map.get(FILTER_IDPESSOA)).getPropertyValue());
		}
		// -> Nome Pessoa
		if (map.containsKey(FILTER_NOMEPESSOA) && ((FilterType) map.get(FILTER_NOMEPESSOA)).getPropertyValue() != null){
			queryCount.setParameter("nomepessoa", (String) ((FilterType) map.get(FILTER_NOMEPESSOA)).getPropertyValue());
		}
		// -> Data
		if (hasParametroIntervaloDeDatas){
			queryCount.setParameter(FILTER_DATAINICIAL, (Date) ((FilterType) map.get(FILTER_DATAINICIAL)).getPropertyValue());
			queryCount.setParameter(FILTER_DATAFINAL, (Date) ((FilterType) map.get(FILTER_DATAFINAL)).getPropertyValue());
		}
	} 
	
}