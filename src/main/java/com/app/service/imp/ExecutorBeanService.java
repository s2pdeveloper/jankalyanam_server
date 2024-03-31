package com.app.service.imp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;


@Configuration
@Service
public class ExecutorBeanService {

    @Bean("cachedThreadPool")
    public ExecutorService cachedThreadPool() {
		return Executors.newCachedThreadPool();
	}
}
