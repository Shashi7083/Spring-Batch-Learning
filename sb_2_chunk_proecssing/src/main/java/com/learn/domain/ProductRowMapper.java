package com.learn.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ProductRowMapper implements RowMapper<Product> {

	@Override
	public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		System.out.println("Result set = "+rs);
		
		Product product = new Product();
		//set value in product using rs
		product.setProductId(rs.getInt("product_id"));
		product.setProductName(rs.getString("product_name"));
		product.setProductCategory(rs.getString("product_category"));
		product.setProductPrice(rs.getInt("product_price"));
				
		return product;
	}

}
