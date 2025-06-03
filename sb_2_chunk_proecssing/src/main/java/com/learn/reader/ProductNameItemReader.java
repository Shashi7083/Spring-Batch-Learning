package com.learn.reader;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class ProductNameItemReader implements ItemReader<String> {

	//create reference of an iterator
	private Iterator<String> productListIterator;
	
	//create parametriezed constructor and assign iterator in productListIterator
	public ProductNameItemReader(List<String> productList) {
		this.productListIterator = productList.iterator();
	}
	
	@Override
	public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		
		return this.productListIterator.hasNext()?this.productListIterator.next():null;
	}

	

}
