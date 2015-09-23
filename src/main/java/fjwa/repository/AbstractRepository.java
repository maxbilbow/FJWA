package fjwa.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import fjwa.model.IEntity;
import org.hibernate.HibernateException;

import click.rmx.Tests;
import fjwa.RMXError;
import fjwa.RMXException;

public abstract class AbstractRepository<E extends IEntity> implements EntityRepository<E> {

	@PersistenceContext
	protected EntityManager em;

	@Override
	public E save(E entity) throws RMXException {
		try {
			if (entity.getId() == null) {
				em.persist(entity);
				em.flush();
			} else {
				entity = em.merge(entity);
			}
		} catch (Exception e) {
			throw RMXException.unexpected(e);
		}
		return entity;
	}

	@Override
	public E remove(E entity) throws RMXException {	
		if (entity != null) 
			try {
				em.remove(em.contains(entity) ? entity : em.merge(entity));
				em.flush();
			} catch (Exception e) {
				throw RMXException.unexpected(e);
			} 
		return entity;
	}


	@Override
	@Deprecated
	public E synchronize(E entity) throws RMXException {
		try {
//			if (em.contains(entity)) {
				entity = em.merge(entity);
//				em.flush();
//			}
		} catch (Exception e) {
			throw RMXException.unexpected(e);
		}
		return entity; //TODO: check this is correct
	}
	
	protected class TypedQueryPair {
		public final String query;
		public final Class<E> Class;
		public TypedQueryPair(String query, Class<E> Class) {
			this.query = query;
			this.Class = Class;
		}
	}
	
	protected abstract String FIND_ALL();
	protected abstract Class<E> CLASS();
//	protected abstract String findAllInTable();
	
	@Override
	public List<E> loadAll() throws RMXException {
		try {
			TypedQuery<E> query = em.createNamedQuery(FIND_ALL(), CLASS());
//			Query query = em.create
			List<E> entities = query.getResultList();
			return entities;
		} catch (Exception e) {
			throw RMXException.unexpected(e);
		}

	}

	@Override
	public Object executeQuery(String query) throws RMXException {
		Object result = null;
		try {
			Query q = em.createQuery(query);
			result = q.getSingleResult();
		} catch (Exception e) {
			throw RMXException.unexpected(e);
		}
		return result;
	}
	
	
	@Override
	public Object queryList(String query) throws RMXException {
		Object result = null;
		try {
			Query q = em.createQuery(query);
			result = q.getResultList();//getSingleResult();
		} catch (Exception e) {
			throw RMXException.unexpected(e);
		}
		return result;
	}
}
