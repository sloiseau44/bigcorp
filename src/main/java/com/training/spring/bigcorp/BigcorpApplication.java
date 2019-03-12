package com.training.spring.bigcorp;

import com.training.spring.bigcorp.model.ApplicationInfo;
import com.training.spring.bigcorp.service.SiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BigcorpApplication {
	private final static Logger logger = LoggerFactory.getLogger(BigcorpApplication.class);

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(BigcorpApplication.class, args);
		ApplicationInfo applicationInfo = context.getBean(ApplicationInfo.class);
		logger.debug("========================================================================");
		logger.debug("Application [" + applicationInfo.getName() + "] - version: " + applicationInfo.getVersion());
		logger.debug("plus d'informations sur " +applicationInfo.getWebSiteUrl());
		logger.debug("========================================================================");
		context.getBean(SiteService.class).findById("test");
	}

}
