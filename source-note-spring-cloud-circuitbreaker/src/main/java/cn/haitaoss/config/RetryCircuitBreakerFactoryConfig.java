package cn.haitaoss.config;

import org.springframework.cloud.circuitbreaker.springretry.SpringRetryAutoConfiguration;
import org.springframework.cloud.circuitbreaker.springretry.SpringRetryCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

/**
 * @author haitao.chen
 * email haitaoss@aliyun.com
 * date 2023-04-17 15:23
 *
 */
@Component
public class RetryCircuitBreakerFactoryConfig {
    /**
     * 1. SpringRetryCircuitBreakerFactory 会依赖这个类型的bean 进行自定义
     *      {@link SpringRetryAutoConfiguration#springRetryCircuitBreakerFactory()}
     *
     * 2. SpringRetryCircuitBreakerFactory 生成的 CircuitBreaker 会依赖 Customizer<RetryTemplate> 配置 重试的规则
     *      {@link SpringRetryCircuitBreakerFactory#create(String)}
     * @return
     */
    @Bean
    public Customizer<SpringRetryCircuitBreakerFactory> retryCircuitBreakerFactoryCustomizer() {
        return new Customizer<SpringRetryCircuitBreakerFactory>() {
            @Override
            public void customize(SpringRetryCircuitBreakerFactory springRetryCircuitBreakerFactory) {
                // 配置这个
                springRetryCircuitBreakerFactory
                        .addRetryTemplateCustomizers(new Customizer<RetryTemplate>() {
                            @Override
                            public void customize(RetryTemplate retryTemplate) {

                            }
                        }, "helloServer");
            }
        };
    }
}
