package prime.db.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;

/**
 * Encapsulates (HQL-)queries and the underlying persistence system (db, LDAP, ...).
 * 
 * @author chw
 * 
 * @param <T>
 *            a db entity (table).
 * @param <ID>
 *            a numeric type representing the id of this db entity.
 */
public abstract class BasicDao<T, ID extends Serializable> implements IBasicDao<T, ID> {

	protected final Class<T>		clazz;
	private final SessionFactory	factory;

	protected Session getSession() {
		// NOTE: The "thread" based method is not intended for production use
		return factory.getCurrentSession();
	}

	public BasicDao(Class<T> clazz, SessionFactory factory) {
		this.clazz = clazz;
		this.factory = factory;
	}

	@Override
	public void delete(T object) {
		getSession().delete(object);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll() {
		return getSession().createCriteria(clazz).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByCriteria(Criterion criterion) {
		return getSession().createCriteria(clazz).add(criterion).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T findById(ID id) {
		return (T) getSession().get(clazz, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T merge(T object) {
		return (T) getSession().merge(object);
	}

	@Override
	public void save(T object) {
		getSession().save(object);
	}

	@Override
	public void saveOrUpdate(T object) {
		getSession().saveOrUpdate(object);
	}

	@Override
	public void update(T object) {
		getSession().update(object);
	}

}
