package com.hit.dao;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hit.dm.Country;
import com.hit.dm.Flight;

public class DaoFileImpl implements IDao<String, Country> {
	private String path;
	private Gson gson;
	private Map<String, Country> daoMap; //Map is faster for delete/search/add

	public DaoFileImpl(String path) {
		gson = new Gson();
		this.path = path;
		this.daoMap = new HashMap<>();
		readMapFromFile();
	}

	public Map<String, Country> getDaoMap() {
		return daoMap;
	}

	/*
	 * delete flight from our daomap (our db as object) and re write the data to the file 
	 */
	@Override
	public void deleteFlight(String src,String dest) {

		if (src == null || dest==null) {
			throw new IllegalArgumentException();
		}
		try {
			readMapFromFile();
			Country country = this.daoMap.get(src);
			int index =0;
			for(Flight flights : country.getFlights()) {
				if(flights.getSrc().equals(src) && flights.getDest().equals(dest))
				{
					country.getFlights().remove(index);
					daoMap.put(src, country);
					break;
				}
				index++;
			}
			writeMapToFile();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void save(Country entity) {
		try {
			readMapFromFile();
			if (entity != null) {
				daoMap.put(entity.getName(), entity);
				writeMapToFile();	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * delete entity from the daomap and then write to the file
	 */
	@Override
	public void delete(Country entity) {
		if (entity == null) {
			throw new IllegalArgumentException();
		}
		try {
			readMapFromFile();
			this.daoMap.remove(entity.getName());
			writeMapToFile();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * find country from the DB
	 */
	@Override
	public Country find(String name) {
		Country retDM = null;

		if (name == null) {
			throw new IllegalArgumentException("");
		}
		try {
			readMapFromFile();
			retDM=this.daoMap.get(name);

		} catch (JsonSyntaxException | JsonIOException e) {
			e.printStackTrace();
		}
		return retDM;
	}

	/*
	 * write daomap data to the file
	 */
	private void writeMapToFile() {
		try {
			String dm = gson.toJson(daoMap);// gson.toJson(entity);
			FileWriter fileWriter = new FileWriter(path);
			
			fileWriter.write(dm);
			fileWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * read db from the file
	 */
	@SuppressWarnings("unchecked")
	private void readMapFromFile() {
		try {
			Type type = new TypeToken<HashMap<String, Country>>() {}.getType();
			this.daoMap = (HashMap<String, Country>) gson.fromJson(new FileReader(path), type);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			e.printStackTrace();
			
		}

	}
	
	
}
