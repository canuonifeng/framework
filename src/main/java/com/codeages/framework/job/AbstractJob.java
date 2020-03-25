package com.codeages.framework.job;

import org.springframework.scheduling.quartz.QuartzJobBean;

abstract public class AbstractJob extends QuartzJobBean {
	abstract public String getJobName();

	public String getJobGroupName() {
		return "default";
	}

	abstract public String getCron();
}
