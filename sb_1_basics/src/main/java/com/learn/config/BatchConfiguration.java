package com.learn.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.learn.decider.MyJobExecutionDecider;
import com.learn.listener.MyJobExecutionListener;
import com.learn.listener.MyStepExecutionListener;

@Configuration
//@EnableBatchProcessing // remove this
public class BatchConfiguration {
	
	
	/**
	 * This method create bean for StepExecutionListener
	 * @return object of step execution Listener
	 */
	@Bean
	public MyStepExecutionListener myStepExecutionListener() {
		return new MyStepExecutionListener();
	};
	
	
	/**
	 * This method create bean of JobExecutionDecider
	 * @return object of MyJobExecutionDecider 
	 */
	@Bean 
	JobExecutionDecider decider() {
		return new MyJobExecutionDecider();
	}
	
	/**
	 * This Method creates bean of step named step1
	 * @return return step object
	 */
	@Bean
	public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("step1", jobRepository)
				.tasklet(new Tasklet() {
					
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						System.out.println("Step 1 Executed");
						ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
						System.out.println("Job Execution context : " + jobExecutionContext);
//						jobExecutionContext.put("sk1", "abc");
						
						//set key value in step Execution context
						ExecutionContext stepExecutionContext = chunkContext.getStepContext().getStepExecution().getExecutionContext();
						stepExecutionContext.put("sk1", "ABC");
						
						return RepeatStatus.FINISHED;
					}
				},transactionManager)
				.listener(promotionListener())
				.build();
	}
	
	@Bean
	public Step step2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		
		return new StepBuilder("step2",jobRepository)
				.tasklet(new Tasklet() {
					
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						
						System.out.println("Step 2 Executed");
						ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
						System.out.println("Job Execution context : " + jobExecutionContext);
//						jobExecutionContext.put("sk2", "klm");
						
						//set key value in step Execution context
						ExecutionContext stepExecutionContext = chunkContext.getStepContext().getStepExecution().getExecutionContext();
						stepExecutionContext.put("sk2", "TUV");
						return RepeatStatus.FINISHED;
					}
				},transactionManager)
//				.listener(myStepExecutionListener()) //removed for jobexecutin decider used
				.listener(promotionListener())
				.build();
	}
	
	@Bean
	public Step step3(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("step3",jobRepository)
				.tasklet(new Tasklet() {
					
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						System.out.println("Step 3 Executed on Thread : "+ Thread.currentThread().getName());
						ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
						System.out.println("Job Execution context : " + jobExecutionContext);
						return RepeatStatus.FINISHED;
					}
				},transactionManager)
				.listener(myStepExecutionListener())
				.build();
	}
	
		
	
	@Bean
	public Step step4(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("step4",jobRepository)
				.tasklet(new Tasklet() {
					
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						System.out.println("Step 4 Executed on Thread : "+ Thread.currentThread().getName());
						return RepeatStatus.FINISHED;
					}
				},transactionManager).build();
	}
	
	@Bean
	public Step step5(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("step5",jobRepository)
				.tasklet(new Tasklet() {
					
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						System.out.println("Step 5 Executed on Thread : "+ Thread.currentThread().getName());
						return RepeatStatus.FINISHED;
					}
				},transactionManager).build();
	}
	
	@Bean
	public Step step6(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("step6",jobRepository)
				.tasklet(new Tasklet() {
					
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						System.out.println("Step 6 Executed on Thread : "+ Thread.currentThread().getName());
						return RepeatStatus.FINISHED;
					}
				},transactionManager).build();
	}
	
	@Bean
	public Step step7(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("step7",jobRepository)
				.tasklet(new Tasklet() {
					
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						System.out.println("Step 7 Executed on Thread : "+ Thread.currentThread().getName());
						return RepeatStatus.FINISHED;
					}
				},transactionManager).build();
	}
	
	@Bean
	public Step step8(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("step8",jobRepository)
				.tasklet(new Tasklet() {
					
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						System.out.println("Step 8 Executed on Thread : "+ Thread.currentThread().getName());
						return RepeatStatus.FINISHED;
					}
				},transactionManager).build();
	}
	
	
	/**
	 * Create flow for step3 and step4
	 */
	@Bean
	public Flow flow1(Step step3, Step step4) {
		FlowBuilder<Flow> flowBuilder = new FlowBuilder<Flow>("flow1");
		flowBuilder
			.start(step3)
			.next(step4)
			.end();
		return flowBuilder.build();
	}
	
	/**
	 * Create flow to use parallel flows
	 * @param step5
	 * @param step6
	 * @return Flow of Step5 and step6
	 */
	@Bean
	public Flow flow2(Step step5, Step step6) {
		FlowBuilder<Flow> flowBuilder = new FlowBuilder<Flow>("flow2");
		flowBuilder
			.start(step5)
			.next(step6)
			.end();
		return flowBuilder.build();
	}
	
	@Bean
	public Flow flow3(Step step7, Step step8) {
		FlowBuilder<Flow> flowBuilder = new FlowBuilder<Flow>("flow3");
		flowBuilder
			.start(step7)
			.next(step8)
			.end();
		return flowBuilder.build();
	}
	
	
	/**
	 * Create Split Flow inside a flow
	 */
	@Bean
	public Flow splitFlow(Flow flow1, Flow flow2, Flow flow3) {
		return new FlowBuilder<Flow>("splitFlow")
				.split(new SimpleAsyncTaskExecutor())
				.add(flow1,flow2,flow3)
				.build();
	}
	
	/**
	 * Bean for job Execution Listener
	 */
	@Bean
	public MyJobExecutionListener myJobExecutionListener() {
		return new MyJobExecutionListener();
	}
	
	/**
	 * Create JobStep nested job
	 */
	@Bean
	public Step job3Step(JobRepository jobRepository, Job job3) {
		return new StepBuilder("job3step",jobRepository)
				.job(job3)
				.build();
	}
	
	/**
	 * Create Promotion Listener
	 */
	@Bean
	public StepExecutionListener promotionListener() {
		ExecutionContextPromotionListener promotionListener = new ExecutionContextPromotionListener();
		
		//specify the keys which we want to promote
		promotionListener.setKeys(new String[] {"sk1","sk2"});
		return promotionListener;
	}

	/**
	 * Creating bean for Job 
	 * @return Job Object namd job1
	 */
	@Bean
	public Job job1(JobRepository jobRepository, Step step1, Step step2,Step step3,Step step4, Step step5, Flow flow1) {
		return new JobBuilder("job1", jobRepository)
				.listener(myJobExecutionListener())
				.start(step1)
				.next(step2)
				.next(decider())
					.on("STEP_3").to(step3)
				.from(decider())
					.on("STEP_4").to(step4)
				.from(decider())
					.on("STEP_5").to(step5)
				.end()
				.build();
	}
	
	@Bean
	public Job job2(JobRepository jobRepository,  Step job3Step,Flow splitFlow) {
		return new JobBuilder("job2", jobRepository)
				.listener(myJobExecutionListener())
				.start(splitFlow)
				.end()
				.build();
	}
	
	@Bean
	public Job job3(JobRepository jobRepository, Step step5, Step step6, Flow flow1) {
		return new JobBuilder("job3", jobRepository)
				.start(step5)
				.next(step6)
				.build();
	}
	
	
}
