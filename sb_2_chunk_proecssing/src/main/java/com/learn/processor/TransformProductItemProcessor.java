package com.learn.processor;


import org.springframework.batch.item.ItemProcessor;

import com.learn.domain.OSProduct;
import com.learn.domain.Product;

public class TransformProductItemProcessor implements ItemProcessor<Product, OSProduct>{

	@Override
	public OSProduct process(Product item) throws Exception {
		
		System.out.println("TransformItemProcessor executed for product : "+item.getProductId());
		OSProduct osProduct = new OSProduct();
		
		osProduct.setProductId(item.getProductId());
		osProduct.setProductName(item.getProductName());
		osProduct.setProductCategory(item.getProductCategory());
		osProduct.setProductPrice(item.getProductPrice());
		
		osProduct.setTaxPercent(item.getProductCategory().equalsIgnoreCase("Sports Accessories")?5:18);
		osProduct.setSku(item.getProductCategory().substring(0,3)+ item.getProductId());
		osProduct.setShippingRate(item.getProductPrice() < 1000?75 : 0);
//		if(item.getProductPrice() == 500) {
//			throw new Exception("Test Exception");
//		}
		return osProduct;
		
		
	}

}
