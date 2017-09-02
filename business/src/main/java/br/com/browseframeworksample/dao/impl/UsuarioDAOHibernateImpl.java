package br.com.browseframeworksample.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import br.com.browseframeworksample.dao.UsuarioDAO;
import br.com.browseframeworksample.domain.Usuario;
import br.com.browseframework.hibernate.dao.impl.CrudDAOHibernateImpl;

@Repository(value = "usuarioDAO")
public class UsuarioDAOHibernateImpl extends CrudDAOHibernateImpl<Long, Usuario> implements UsuarioDAO {

	@Autowired
	public UsuarioDAOHibernateImpl(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
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
	 * @see br.com.browseframeworksample.dao.UsuarioDAO#findUsuariosClipping()
	 */
	@SuppressWarnings("unchecked")
	public List<Usuario> findUsuariosClipping() {
		List<Usuario> retorno = null;
		 
		Session session = null;
		try {
			session = getCurrentSession();
			
			final StringBuilder sql = new StringBuilder();
			sql.append("select {u.*} from usuario as u  ");
			sql.append("where exists (select 1 from usuario_perfil where cod_usuario = u.cod_usuario  ");
			sql.append("                and cod_perfil = (select val_parametro from parametro where des_parametro = 'PERFIL - CLIPPING') ");
			sql.append("             ) ");
			
			// Cria query
			final SQLQuery query = session.createSQLQuery(sql.toString());
			query.addEntity("u", Usuario.class);
			
			// Executa a query
			retorno = (List<Usuario>) query.list();
		} finally {
			;
		}
		
		return retorno;
	}
}