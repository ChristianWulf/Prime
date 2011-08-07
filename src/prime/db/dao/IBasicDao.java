package prime.db.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Criterion;

public interface IBasicDao<T, ID extends Serializable> {

	void save(T object);

	void update(T object);

	void delete(T object);

	void saveOrUpdate(T object);

	T merge(T object);

	T findById(ID id);

	List<T> findByCriteria(Criterion criterion);

	List<T> findAll();
}
