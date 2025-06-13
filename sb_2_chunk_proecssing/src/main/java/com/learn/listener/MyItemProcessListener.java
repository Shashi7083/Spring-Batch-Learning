package com.learn.listener;

import org.springframework.batch.core.ItemProcessListener;

import com.learn.domain.OSProduct;
import com.learn.domain.Product;

public class MyItemProcessListener implements ItemProcessListener<Product, OSProduct> {

	@Override
	public void beforeProcess(Product item) {
		System.out.println("Before Process executed for product : "+item.getProductId());
	}

	@Override
	public void afterProcess(Product item, OSProduct result) {
		System.out.println("after Process executed for product : "+item.getProductId());
	}

	@Override
	public void onProcessError(Product item, Exception e) {
		System.out.println("onProcessError executed for product : "+item.getProductId());
	}

}
