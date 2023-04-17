package cn.haitaoss.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.springframework.cloud.circuitbreaker.resilience4j.*;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author haitao.chen
 * email haitaoss@aliyun.com
 * date 2023-04-17 16:51
 *
 */
@Component
public class Resilience4JCircuitBreakerFactoryConfig {
    /**
     * 1. Resilience4JCircuitBreakerFactory 会依赖这个类型的bean 进行自定义
     *      {@link Resilience4JAutoConfiguration#resilience4jCircuitBreakerFactory(CircuitBreakerRegistry, TimeLimiterRegistry, Resilience4jBulkheadProvider, Resilience4JConfigurationProperties)}
     *
     * 2. Resilience4JCircuitBreakerFactory 生成的 CircuitBreaker 会依赖 Customizer<CircuitBreaker> 配置规则
     *      {@link Resilience4JCircuitBreaker#run(Supplier, Function)}
     * @return
     */
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
        // 重设默认项
        return factory -> {
            factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                    // 配置时间限制器
                    .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build())
                    // 配置 断路器
                    .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults()).build());


            factory.addCircuitBreakerCustomizer(new Customizer<CircuitBreaker>() {
                @Override
                public void customize(CircuitBreaker circuitBreaker) {
                    // circuitBreaker.getEventPublisher().onError()
                }
            }, "helloServer");
        };
    }

}
