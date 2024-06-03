package com.rmendes.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class FastFood {

	
	public String id;
	public String address;
	public String categories;
	public String city;
	public String country;
	public String name;
	public String province;
}
