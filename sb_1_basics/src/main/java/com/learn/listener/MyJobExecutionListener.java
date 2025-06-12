package com.learn.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class MyJobExecutionListener implements JobExecutionListener {

		
	@Override
	public void beforeJob(JobExecution jobExecution) {
		System.out.println("jobName : "+jobExecution.getJobInstance().getJobName());
		System.out.println("Job Parameters: "+jobExecution.getJobParameters());
		System.out.println("Job Start Time: "+jobExecution.getStartTime());
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		System.out.println("jobName : "+jobExecution.getJobInstance().getJobName());
		System.out.println("Job Parameters: "+jobExecution.getJobParameters());
		System.out.println("Job Start Time: "+jobExecution.getEndTime());
	}
	
}
