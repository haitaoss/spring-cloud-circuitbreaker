/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.circuitbreaker.springretry;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

/**
 * @author Ryan Baxter
 * @author Eric Bussieres
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RetryTemplate.class)
public class SpringRetryAutoConfiguration {

	// 通过依赖注入得到 Customizer<SpringRetryCircuitBreakerFactory>
	@Autowired(required = false)
	private List<Customizer<SpringRetryCircuitBreakerFactory>> customizers = new ArrayList<>();

	@Bean
	@ConditionalOnMissingBean(CircuitBreakerFactory.class)
	public CircuitBreakerFactory springRetryCircuitBreakerFactory() {
		// SpringRetryCircuitBreakerFactory 继承 CircuitBreakerFactory
		SpringRetryCircuitBreakerFactory factory = new SpringRetryCircuitBreakerFactory();
		/**
		 * 使用 List<Customizer<SpringRetryCircuitBreakerFactory>> 对 SpringRetryCircuitBreakerFactory 进行定制化
		 * */
		customizers.forEach(customizer -> customizer.customize(factory));
		return factory;
	}

}
