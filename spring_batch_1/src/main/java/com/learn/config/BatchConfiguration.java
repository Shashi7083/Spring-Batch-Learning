package com.learn.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.learn.decider.MyJobExecutionDecider;
import com.learn.listener.MyStepExecutionListener;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	/**
	 * This method create bean for StepExecutionListener
	 * @return object of step execution Listener
	 */
	@Bean
	public StepExecutionListener myStepExecutionListener() {
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
	public Step step1() {
		return this.stepBuilderFactory.get("step1")
				.tasklet(new Tasklet() {
					
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						System.out.println("Step 1 Executed");
						return RepeatStatus.FINISHED;
					}
				}).build();
	}
	
	@Bean
	public Step step2() {
		
		return this.stepBuilderFactory.get("step2")
				.tasklet(new Tasklet() {
					
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						boolean isFailure = false; // true for fail this step
						
						//to fail this job to restart again by doing isSuccess  as false
						if(isFailure) {
							throw new Exception("test exception");
						}
						
						System.out.println("Step 2 Executed");
						return RepeatStatus.FINISHED;
					}
				})
//				.listener(myStepExecutionListener()) //removed for jobexecutin decider used
				.build();
	}
	
	@Bean
	public Step step3() {
		return this.stepBuilderFactory.get("step3")
				.tasklet(new Tasklet() {
					
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						System.out.println("Step 3 Executed");
						return RepeatStatus.FINISHED;
					}
				}).build();
	}
	
	@Bean
	public Step step4() {
		return this.stepBuilderFactory.get("step4")
				.tasklet(new Tasklet() {
					
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						System.out.println("Step 4 Executed");
						return RepeatStatus.FINISHED;
					}
				}).build();
	}


	/**
	 * Creating bean for Job 
	 * @return Job Object namd job1
	 */
	@Bean
	public Job firstJob() {
		return this.jobBuilderFactory.get("job1")
				.start(step1())
					.on("COMPLETED").to(decider())
					.on("TEST_STATUS").to(step2())
					.from(decider())
						.on("*").to(step3())
				.end()
				.build();
	}
	
	
}
