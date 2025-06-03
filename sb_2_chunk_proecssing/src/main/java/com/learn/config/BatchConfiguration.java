package com.learn.config;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import com.learn.domain.Product;
import com.learn.domain.ProductFieldSetMapper;
import com.learn.domain.ProductRowMapper;
import com.learn.reader.ProductNameItemReader;

@EnableBatchProcessing
@Configuration
public class BatchConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private DataSource dataSource;
	
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
	 * @return flatFileItemReader
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
	
	/**
	 * This function creates a jdbc Cursor item reader 
	 * @return jdbc Cursor item reader
	 */
	
	@Bean
	public ItemReader<Product> jdbcCursorItemReader(){
		
		JdbcCursorItemReader<Product> itemReader = new JdbcCursorItemReader<>();
		
		//set dataSource to jdbc cursor item reader
		itemReader.setDataSource(dataSource);
		
		//write sql query to get data from database
		itemReader.setSql("select * from product_details order by product_id");
		
		//set row mapper-> map row from resultset to our model (pojo)
		itemReader.setRowMapper(new ProductRowMapper());
		
		return itemReader;
		
	}
	
	/**
	 * This function creates Jdbc Paging Item Reader 
	 * @return jdbcPagingItemReader
	 * @throws Exception 
	 */
	@Bean
	public ItemReader<Product> jdbcPagingItemReader() {
		
		JdbcPagingItemReader<Product> itemReader = new JdbcPagingItemReader<>();
		
		//set Datasource
		itemReader.setDataSource(dataSource);
		
		//Paging query provider
		SqlPagingQueryProviderFactoryBean factory = new SqlPagingQueryProviderFactoryBean();
		
		//give clause in factory
		factory.setDataSource(dataSource);
		factory.setSelectClause("select product_id,product_name,product_category,product_price");
		factory.setFromClause("from product_details");
		factory.setSortKey("product_id"); //sort key should be unique
		
		try {
			itemReader.setQueryProvider(factory.getObject());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//specify our row mapper
		itemReader.setRowMapper(new ProductRowMapper());
		
		//specify the page size -> How many items there in a page or how many items read from database for each query run
		itemReader.setPageSize(3); // 3 rows at a time
		
		return itemReader;
	}
	
	/*
	 * Item Writer
	 */
	
	/**
	 * Create Flat file item writer to write in a csv file
	 */
	@Bean
	public ItemWriter<Product> flatFileItemWriter(){
		
		FlatFileItemWriter<Product> itemWriter = new FlatFileItemWriter<>();
		
		//set path 
		itemWriter.setResource(new FileSystemResource("output/Product_Details_Output.csv"));
		
		//take product pojo and convert to line in csv
		DelimitedLineAggregator<Product> lineAggregator = new DelimitedLineAggregator<Product>();
		
		//set delimiter
		lineAggregator.setDelimiter(",");
		
		//create field extractor object BeanWrapperFieldExtractor
		BeanWrapperFieldExtractor<Product> fieldExtractor = new BeanWrapperFieldExtractor<Product>();
		
		//set the field name
		fieldExtractor.setNames(new String[] {"productId","productName","productCategory","productPrice"});
		
		//set fieldExtractor on lineAggregator
		lineAggregator.setFieldExtractor(fieldExtractor);
		
		//set line aggregator in item writer
		itemWriter.setLineAggregator(lineAggregator);
		
		return itemWriter;
	}
	
	
	
	@Bean
	public Step step1()  {
		return this.stepBuilderFactory.get("chunkBasedStep1")
				.<Product,Product>chunk(3)
				.reader(jdbcPagingItemReader())
				.writer(flatFileItemWriter())
				.build();
	}
	
	@Bean
	public Job firstJob() throws Exception  {
		return this.jobBuilderFactory
				.get("job1")
				.start(step1())
				.build();
	}
}
