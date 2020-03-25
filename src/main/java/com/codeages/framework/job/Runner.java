package com.codeages.framework.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements ApplicationRunner {
	@Autowired
	private QuartzManager quartzManager;

	@Autowired
	private List<AbstractJob> jobs;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		jobs.forEach(job -> {
			quartzManager.addJob(job.getClass(), job.getJobName(), job.getJobGroupName(), job.getCron());
		});
	}
}
