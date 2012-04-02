package org.hook38.locationfinderandroid.objects;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

public class ResultObject {
	private String status;
	
	//private List<FetchedLocationObject> locations
		//= new ArrayList<FetchedLocationObject>();
	
	private List<Location> locations = new ArrayList<Location>();
		
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the results
	 */
	public List<Location> getLocations() {
		return locations;
	}
	/**
	 * @param results the results to set
	 */
	public void setLocations(List<Location> locations) {
		this.locations = locations;
		this.setStatus("Success");
	}
	
	public void addLocation(Location location){
		this.getLocations().add(location);
	}
	
	public void removeLocation(Location location){
		this.getLocations().remove(location);
	}
	

}
