package com.learn.decider;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class MyJobExecutionDecider implements JobExecutionDecider {

	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		// TODO Auto-generated method stub
		String sk1 = jobExecution.getExecutionContext().getString("sk1");
		String sk2 = jobExecution.getExecutionContext().getString("sk2");
		
		String exitStatus = "";
		if(sk1.equals("ABC") && sk2.equals("KLM")) {
			exitStatus = "STEP_3";
		}else if(sk1.equals("ABC") && sk2.equals("TUV")) {
			exitStatus = "STEP_4";
		}else {
			exitStatus = "STEP_5";
		}
		return new FlowExecutionStatus(exitStatus);
	}

}
