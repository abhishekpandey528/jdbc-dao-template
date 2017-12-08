package com.codeway.daoTemplate.dao;

import java.io.Serializable;
import java.util.List;

/**
*
* @author Abhishek Pandey
*/
public interface IGenericDao<Pk extends Serializable, Entity> {

	public Entity get(Pk id) throws Exception;
	public List<Entity> getAll() throws Exception;
	public Entity save(Entity entity) throws Exception;
	public Entity update(Entity entity) throws Exception;
	public void remove(Pk id) throws Exception;
}
