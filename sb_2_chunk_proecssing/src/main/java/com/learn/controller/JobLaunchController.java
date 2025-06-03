package com.learn.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobLaunchController {
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	@Qualifier("firstJob") //it help us specify name of the bean which we inject this is helpful when we have multiple job beans in our program
	private Job job;
	
	@GetMapping("/launchJob/{id}")
	public void handle(@PathVariable("id") String id) throws Exception{
		
		//create Job parameters
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("param", id)
				.toJobParameters();
		
		//launch job 
		jobLauncher.run(job, jobParameters);
		
	}

}
