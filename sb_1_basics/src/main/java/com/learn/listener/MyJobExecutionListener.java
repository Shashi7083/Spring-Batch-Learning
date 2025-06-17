package com.learn.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.item.ExecutionContext;

public class MyJobExecutionListener {
//implements JobExecutionListener {

		
	@BeforeJob
	public void beforeJob(JobExecution jobExecution) {
		System.out.println("jobName : "+jobExecution.getJobInstance().getJobName());
		System.out.println("Job Parameters: "+jobExecution.getJobParameters());
		System.out.println("Job Start Time: "+jobExecution.getStartTime());
		
		ExecutionContext jobExecutionContext = jobExecution.getExecutionContext();
		//put key value pair
		jobExecutionContext.put("jk1", "xyz");
	}

	@AfterJob
	public void afterJob(JobExecution jobExecution) {
		System.out.println("jobName : "+jobExecution.getJobInstance().getJobName());
		System.out.println("Job Parameters: "+jobExecution.getJobParameters());
		System.out.println("Job Start Time: "+jobExecution.getEndTime());
	}
	
}
