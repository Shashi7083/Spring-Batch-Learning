package com.example.config;

import org.slf4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import org.slf4j.LoggerFactory;

@Component
public class JobCompletionNotificationImpl implements JobExecutionListener {

	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(JobCompletionNotificationImpl.class);
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		logger.info("Job Started");
	
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			logger.info("Job Completed");
		}
	}

	
}
