package com.hit.dao;

import java.io.Serializable;


public interface IDao<ID extends Serializable,T> {

	public void save(T entity);

	public void delete(T entity);

	public T find(ID name);

	public void deleteFlight(ID src, ID dest);
	
}
