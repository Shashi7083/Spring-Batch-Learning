package com.learn.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.learn.domain.Product;
import com.learn.domain.ProductFieldSetMapper;
import com.learn.reader.ProductNameItemReader;

@EnableBatchProcessing
@Configuration
public class BatchConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public ItemReader<String> itemReader(){
		List<String> productList = new ArrayList<>();
		productList.add("Product 1");
		productList.add("Product 2");
		productList.add("Product 3");
		productList.add("Product 4");
		productList.add("Product 5");
		productList.add("Product 6");
		productList.add("Product 7");
		productList.add("Product 8");
		
		return new ProductNameItemReader(productList);
	}
	
	/**
	 * THis method reads data from csv file
	 * @return
	 */
	@Bean
	public ItemReader<Product> flatFileItemReader() {

		FlatFileItemReader<Product> itemReader = new FlatFileItemReader<>();

		// skip reading header row , set the lines to skip (no of lines at start of the
		// file)
		itemReader.setLinesToSkip(1); // skip first line

		// where to find our file
		itemReader.setResource(new ClassPathResource("/data/product_Details.csv"));

		// tell the item reader , how to parse the file and map to product object
		DefaultLineMapper<Product> lineMapper = new DefaultLineMapper<Product>(); // map single line in our file to
																					// product object

		// parse line and map to product
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

		// tell line tokenizer name of column in file
		lineTokenizer.setNames("product_id", "product_name", "product_category", "product_price"); // write all column
																									// names seperated
																									// by comma

		lineMapper.setLineTokenizer(lineTokenizer);

		// provide field set mapper , so that it can map to product
		lineMapper.setFieldSetMapper(new ProductFieldSetMapper());

		itemReader.setLineMapper(lineMapper);

		return itemReader;

	}
	
	@Bean
	public Step step1() {
		return this.stepBuilderFactory.get("chunkBasedStep1")
				.<Product,Product>chunk(3)
				.reader(flatFileItemReader())
				.writer(new ItemWriter<Product>() {

					@Override
					public void write(List<? extends Product> items) throws Exception {
						System.out.println("Chunk Procession started : ");
						items.forEach(System.out::println);
						System.out.println("chunk ended");
					}
				})
				.build();
	}
	
	@Bean
	public Job firstJob() {
		return this.jobBuilderFactory
				.get("job1")
				.start(step1())
				.build();
	}
}
