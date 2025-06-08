package com.learn.processor;

import org.springframework.batch.item.ItemProcessor;

import com.learn.domain.Product;

public class FilterProductItemProcessor implements ItemProcessor<Product, Product> {

	@Override
	public Product process(Product item) throws Exception {
		//write only those product which have price greater than 100
		if(item.getProductPrice() > 100) {
			return item;
		}
		return null;
	}

}
