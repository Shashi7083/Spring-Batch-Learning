package com.learn.processor;

import java.util.Random;

import org.springframework.batch.item.ItemProcessor;

import com.learn.domain.Product;
import com.learn.exception.MyException;

public class FilterProductItemProcessor implements ItemProcessor<Product, Product> {

	@Override
	public Product process(Product item) throws Exception {
		
		System.out.println("Filter Product Item Processor executed for product : "+item.getProductId());
		//write only those product which have price greater than 100
//		if(item.getProductPrice() > 100) {
//			return item;
//		}
//		return null;
		
		Random random = new Random();
		
		
		if(item.getProductPrice() == 500 && random.nextInt(3) == 2) {
			System.out.println("Exception Thrown");
			throw new MyException("Test Exception");
		}
		return item;
	}

}
