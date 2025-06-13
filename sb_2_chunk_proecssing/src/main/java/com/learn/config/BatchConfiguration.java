package com.learn.config;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.learn.domain.OSProduct;
import com.learn.domain.Product;
import com.learn.domain.ProductFieldSetMapper;
import com.learn.domain.ProductItemPreparedStatementSetter;
import com.learn.domain.ProductRowMapper;
import com.learn.domain.ProductValidator;
import com.learn.listener.MyChunkListener;
import com.learn.listener.MyItemProcessListener;
import com.learn.listener.MyItemReadListener;
import com.learn.listener.MyItemWriteListener;
import com.learn.processor.FilterProductItemProcessor;
import com.learn.processor.MyProductItemProcessor;
import com.learn.processor.TransformProductItemProcessor;
import com.learn.reader.ProductNameItemReader;


@Configuration
public class BatchConfiguration {

	
	
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
	
	/**
	 * This method create JdbcBatchItemWriter
	 * @return
	 */
	@Bean
	public ItemWriter<Product> jdbcBatchItemWriter(){
		
		JdbcBatchItemWriter<Product> itemWriter = new JdbcBatchItemWriter<>();
		
		//set datasource
		itemWriter.setDataSource(dataSource);
		
		//set sql
		itemWriter.setSql("insert into product_details_output values(:productId,:productName,:productCategory,:productPrice)");
		
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Product>());
		
		return itemWriter;
		
	}
	
	@Bean
	public ItemWriter<OSProduct> jdbcBatchItemWriterForDifferent_IO(){
		
		JdbcBatchItemWriter<OSProduct> itemWriter = new JdbcBatchItemWriter<>();
		
		//set datasource
		itemWriter.setDataSource(dataSource);
		
		//set sql
		itemWriter.setSql("insert into os_product_details values(:productId,:productName,:productCategory,:productPrice,:taxPercent,:sku,:shippingRate)");
		
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
		
		return itemWriter;
		
	}
	
	/**
	 * Item Processor
	 * @return item
	 */
	@Bean
	public ItemProcessor<Product, OSProduct> myProductItemProcessor(){
		return new MyProductItemProcessor();
	}
	
	/**
	 * Filter Product 
	 */
	@Bean
	public ItemProcessor<Product, Product> filterProductItemProcessor(){
		return new FilterProductItemProcessor();
	}
	
	/**
	 * Create bean of Validating Item Processor that's logic written in Product Validator
	 * @return ValidationItemProcessor 
	 * @throws Validation Exception
	 */
	@Bean
	public ValidatingItemProcessor<Product> validateProductItemProcessor(){
		ValidatingItemProcessor<Product> validatingItemProcessor = new ValidatingItemProcessor<Product>(new ProductValidator());
		validatingItemProcessor.setFilter(true);
		return validatingItemProcessor;
	}
	
	/**
	 * Processing or validating using bean validation Api
	 */
	@Bean
	public BeanValidatingItemProcessor<Product> beanValidateItemProcessor(){
		BeanValidatingItemProcessor<Product> BeanValidatingItemProcessor = new BeanValidatingItemProcessor<Product>();
		BeanValidatingItemProcessor.setFilter(true);
		return BeanValidatingItemProcessor;
	}
	
	@Bean
	public ItemProcessor<Product,OSProduct> transformProductItemProcessor(){
		return new TransformProductItemProcessor();
	}
	
	/**
	 * Composite Item Processor bean
	 */
	@Bean
	public CompositeItemProcessor<Product, OSProduct> compositeItemProcessor(){
		CompositeItemProcessor<Product, OSProduct> itemProcessor =  new CompositeItemProcessor<Product, OSProduct>();
		
		//create list of item processor
		List itemProcessors = new ArrayList();
		itemProcessors.add(validateProductItemProcessor());
		itemProcessors.add(filterProductItemProcessor());
		itemProcessors.add(transformProductItemProcessor());
		
		
		//pass this list to composite item processor
		itemProcessor.setDelegates(itemProcessors);
		
		return itemProcessor;
	}
	
	/**
	 * Chunk Listener implementation
	 */
	@Bean
	public MyChunkListener myChunkListener() {
		return new MyChunkListener();
	}
	
	/**
	 * Item Read Listener
	 */
	@Bean
	public MyItemReadListener myItemReadListener() {
		return new MyItemReadListener();
	}
	
	
	/**
	 * Item Process Listener
	 */
	@Bean
	public MyItemProcessListener myItemProcessListener() {
		return new MyItemProcessListener();
	}
	
	/**
	 * Item write listener
	 */
	@Bean
	public MyItemWriteListener myItemWriteListener() {
		return new MyItemWriteListener();
	}
	
	/**
	 * 
	 * @param jobRepository
	 * @param transactionManager
	 * @return
	 */
	@Bean
	public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager)  {
		return new StepBuilder("chunkBasedStep1",jobRepository)
				.<Product,OSProduct>chunk(3,transactionManager)
				.reader(jdbcPagingItemReader())
				.processor(compositeItemProcessor())
				.writer(jdbcBatchItemWriterForDifferent_IO())
				.listener(myChunkListener())
				.listener(myItemReadListener())
				.listener(myItemWriteListener())
				.listener(myItemProcessListener())
				.build();
	}
	

	
	@Bean
	public Job firstJob(JobRepository jobRepository, Step step1) throws Exception  {
		return new JobBuilder("job1", jobRepository)
				.start(step1)
				.build();
	}
}
