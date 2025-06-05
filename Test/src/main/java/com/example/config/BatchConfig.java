package com.example.config;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.example.model.Product;

@Configuration
public class BatchConfig {
	
	
	@Bean
	public Job jobbean(
			JobRepository jobRepository,
			JobCompletionNotificationImpl listener,
			Step steps) {
		return new JobBuilder("job2", jobRepository)
		.listener(listener)
		.start(steps)
		.build();
	}
	
	@Bean
	public Step steps(
			JobRepository jobRepository,
			DataSourceTransactionManager transactionManager,
			ItemReader<Product> reader,
			ItemProcessor<Product, Product> processor,
			ItemWriter<Product> writer) {
		return new StepBuilder("jobStep2",jobRepository)
				.<Product,Product>chunk(5, transactionManager)
//				.reader(reader)
				.reader(jdbcCursorItemReader())
//				.processor(processor)
//				.writer(writer)
				.writer(flatFileItemWriter())
				.build();
		
	}
	
	//reader form text file
	@Bean
	public FlatFileItemReader<Product> reader(){
		
		return new FlatFileItemReaderBuilder<Product>()
				.name("itemReader")
				.linesToSkip(1)
				.resource(new ClassPathResource("test_product.txt"))
				.delimited()
				.delimiter(",") //to tell the seperator
				.names("productid","title","description","price","discount") //column name
				.targetType(Product.class)
				.build();
				
		
	}
	
	//item processor
	@Bean
	public ItemProcessor<Product, Product> itemProcessor(){
		return new CustomItemProcessor();
	}
	
	//item writer -> write in database
//	@Bean
//	public ItemWriter<Product> itemWriter(DataSource dataSource){
//		return new JdbcBatchItemWriterBuilder<Product>()
//				.sql("insert into test_products(product_id,title,description,discount,discounted_price) values(:productId,:title,:description,:discount,:discountedPrice)")
//				.dataSource(dataSource)
//				.beanMapped()
//				.build();
//				
//						
//	}
	
	
	@Autowired
	private DataSource dataSource;
	//Item Reader from database JdbcCursorItemReader
	@Bean
	public JdbcCursorItemReader<Product> jdbcCursorItemReader(){
		
		JdbcCursorItemReader<Product> itemReader = new JdbcCursorItemReader<Product>();
		
		//set datasource 
		itemReader.setDataSource(dataSource);
		
		//set sql query
		itemReader.setSql("select * from test_products order by product_id");
		
		//set mapper
		itemReader.setRowMapper(new RowMapper<Product>() {
			
			@Override
			public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
				Product product = new Product();
				product.setProductId(Integer.toString(rs.getInt("product_id")));
				product.setTitle(rs.getString("title"));
				product.setDescription(rs.getString("description"));
				product.setPrice(rs.getString("price"));
				product.setDiscount(rs.getString("discount"));
				product.setDiscountedPrice(rs.getString("discounted_price"));
				return product;
			}
		} );
		
		return itemReader;
		
	}
	
	//item writer -> in text file
	@Bean
	public FlatFileItemWriter<Product> flatFileItemWriter(){
		
		FlatFileItemWriter<Product> itemWriter = new FlatFileItemWriter<Product>();
		
		//set destination
		itemWriter.setResource(new FileSystemResource("output/Product_Details_Output.txt"));
		
		DelimitedLineAggregator<Product> lineAggregator = new DelimitedLineAggregator<Product>();
		lineAggregator.setDelimiter(",");
		
		BeanWrapperFieldExtractor<Product> fieldExtractor = new BeanWrapperFieldExtractor<Product>();
		
		//set the field name
		fieldExtractor.setNames(new String[] {"productId", "title", "description", "price", "discount", "discountedPrice"});

		lineAggregator.setFieldExtractor(fieldExtractor);
		
		itemWriter.setLineAggregator(lineAggregator);
		return itemWriter;
	}
	
	
	
}
