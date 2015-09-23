package fjwa.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fjwa.model.IEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import fjwa.RMXException;
import fjwa.repository.EntityRepository;

public abstract class AbstractEntityService<E extends IEntity> implements EntityService<E> {

	private List<E> entities = new ArrayList<>();
	private Map<String,String> seriousErrors = new HashMap<>();
	protected String errorLog = "";


	protected abstract EntityRepository<E> repository();
	
	@Override
	@Transactional
	public List<E> findAllEntities() {
		try {
			List<E> newList = repository().loadAll();
			if ( newList != null )
				this.entities = newList;
		} catch (RMXException e) {
			this.addError(e);
		}
		return this.entities;
	}

	@Override
	public String getErrors() {
		String log = "ERRORS: " + this.errorLog;
		for (String error : seriousErrors.values()) {
			log += "<br/>" + error;
		}
		this.errorLog = "";
		return log;//.replace("\n", "<br/>");
	}

	@Override
	@Transactional
	public E save(E entity) {
		try {
			this.getEntities().add(entity);
			return repository().save(entity);
		} catch (RMXException e) {
			this.addError(e);
		}
		return entity;
	}


	
	@Override
	@Transactional
	public boolean remove(E entity) {
		try { 
			if (entities.contains(entity))
				entities.remove(entity);
			repository().remove(entity);
			return true;
		} catch (RMXException e) {
			this.addError(e);
		} 
		return false;
	}
	
	@Override
	@Transactional
	public List<E> removeAll() {
		for (E entity : entities) {
			try {
				this.repository().remove(entity);
			} catch (RMXException e) {
				addError(RMXException.unexpected(e));
			}
		}
		entities = null;
		return this.findAllEntities();
	}
	
	public RMXException addError(RMXException e) {
		if (e.isSerious())
			this.seriousErrors.put(e.getCause().getLocalizedMessage(),e.html());
		else 
			this.errorLog += "<br/>" + e.html();
		return e;
	}
	
	@Override
	@Transactional
	public List<E> synchronize() {
		try {
//			List<E> qList = repository().loadAll();
			for (E entity : entities) {
				try {
					repository().save(entity);//.synchronize(entity);
				} catch (Exception e) {
					addError(RMXException.unexpected(e,"Could not sync " + entity));
				}
			}
//			getEntities().clear();
//			for (E entity : qList) {
//				entities.add(entity);
//			}
//			return entities;
		} catch (Exception e) {
			this.addError(RMXException.unexpected(e));
		}
		return this.findAllEntities();
		
	}

	/**
	 * @return the entities
	 */
	public List<E> getEntities() {
		return entities;
	}

	

}
