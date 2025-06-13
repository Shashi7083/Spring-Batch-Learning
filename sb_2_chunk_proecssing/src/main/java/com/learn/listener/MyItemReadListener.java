package com.learn.listener;

import org.springframework.batch.core.ItemReadListener;

import com.learn.domain.Product;

public class MyItemReadListener implements ItemReadListener<Product> {

	@Override
	public void beforeRead() {
		System.out.println("Before Read Executed");
	}

	@Override
	public void afterRead(Product item) {
		System.out.println("After Read Executed : "+item.getProductId());
	}

	@Override
	public void onReadError(Exception ex) {
		System.out.println("onReadError Executed : "+ex.getMessage());
	}

}
