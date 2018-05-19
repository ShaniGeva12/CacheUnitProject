package com.hit.dao;

public interface IDao <ID extends java.io.Serializable,T>
{
	void save(T entity);
	//Saves a given entity.

	void delete(T entity);
	//Throws: java.lang.IllegalArgumentException 
	//in case the given entity is null
	
	T find(ID id);
	//Returns: the entity with the given id or null if none found
	//Throws: java.lang.IllegalArgumentException - if id is null
	
}
