package com.learn.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;

public class MyStepExecutionListener {
//implements StepExecutionListener {

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		System.out.println("Step Name : "+stepExecution.getStepName());
		System.out.println("Exit Status : "+stepExecution.getExitStatus());
		System.out.println("Start Time :"+stepExecution.getStartTime());
		System.out.println(stepExecution.getStepName()+" Executed on Thread  : "+Thread.currentThread().getName());
		
	}

	@AfterStep
	public ExitStatus afterStep(StepExecution stepExecution) {
		System.out.println("Step Name : "+stepExecution.getStepName());
		System.out.println("Exit Status : "+stepExecution.getExitStatus());
		System.out.println("End Time :"+stepExecution.getEndTime());
		System.out.println(stepExecution.getStepName()+" Executed on Thread  : "+Thread.currentThread().getName());
		return null;  // if we return null then default exit status will be used
	}

}
