package com.learn.domain;

public class OSProduct extends Product{
	
	//add new fields
	private Integer taxPercent;
	private String sku;
	private Integer shippingRate;
	
	
	@Override
	public String toString() {
		return "OSProduct [taxPercent=" + taxPercent + ", sku=" + sku + ", shippingRate=" + shippingRate + "]";
	}
	
	public Integer getTaxPercent() {
		return taxPercent;
	}
	public void setTaxPercent(int taxPercent) {
		this.taxPercent = taxPercent;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public Integer getShippingRate() {
		return shippingRate;
	}
	public void setShippingRate(Integer shippingRate) {
		this.shippingRate = shippingRate;
	}

}
