package com.learn.domain;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

public class ProductValidator implements Validator<Product> {
	
	//we are skipping camera categories to check output
	
	List<String> validProductCategories = Arrays.asList("Televisions","Tablets","Sports","Mobile Phones","Sport Accessories");

	@Override
	public void validate(Product value) throws ValidationException {
		
		if( !validProductCategories.contains(value.getProductCategory())) {
			throw new ValidationException("Invalid Product Category");
		}
		
		if(value.getProductPrice() > 100000) {
			throw new ValidationException("Invalide Product Price");
		}
		
	}

}
