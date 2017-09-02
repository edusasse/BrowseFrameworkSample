package br.com.browseframeworksample.domain.to;

import java.util.HashMap;
import java.util.Map;

public class AbstractGenericTO {
	private Map<String, Object> properties;
	
	public AbstractGenericTO() {
		setProperties(new HashMap<String, Object>());
	}

	public void put(String key, Object value){
		getProperties().put(key, value);
	}
	
	// GETTERS && SETTERS
	
	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
}
