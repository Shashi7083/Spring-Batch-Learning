package com.example.config;

import org.springframework.batch.item.ItemProcessor;

import com.example.model.Product;

public class CustomItemProcessor implements ItemProcessor<Product, Product> {

	@Override
	public Product process(Product item) throws Exception {
		//calculate discounted price 
		//original price - discount
		int discountPer = Integer.parseInt(item.getDiscount().trim());
		Double originalPrice = Double.parseDouble(item.getPrice().trim());
		double discount = (discountPer/100)*originalPrice;
		
		double finalPrice = originalPrice - discount;
		
		item.setDiscountedPrice(String.valueOf(finalPrice));
		return item;
	}

}
