package com.example.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
//	@Qualifier("job2")
	private Job job;
	
	@GetMapping("/run/{id}")
	public void run(@PathVariable("id") String id) throws Exception{
		
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("param", id)
				.toJobParameters();
		
		jobLauncher.run(job, jobParameters);
	}
}
