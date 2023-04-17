# 资料

[Spring Cloud 官网文档](https://docs.spring.io/spring-cloud/docs/2021.0.5/reference/html/)

[Spring Cloud Circuit Breaker 官网文档](https://docs.spring.io/spring-cloud-circuitbreaker/docs/2.1.5/reference/html/)

[示例代码](../source-note-spring-cloud-circuitbreaker)

# Spring Cloud Circuit Breaker 介绍

Spring Cloud CircuitBreaker 只是将  [Resilience4J](https://github.com/resilience4j/resilience4j) 和 [Spring Retry](https://github.com/spring-projects/spring-retry) 通过自动装配 注册 CircuitBreakerFactory 到应用中而已。而 CircuitBreakerFactory 是  [Spring Cloud Commons](https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/#spring-cloud-circuit-breaker)  定义的接口，定义了 Spring Cloud 断路器的规范，比如 [Spring Cloud OpenFeign](https://github.com/haitaoss/spring-cloud-openfeign/blob/source-v3.1.5/note/spring-cloud-openfeign-source-note.md#feigncircuitbreakertargeter) 会根据IOC容器中存在 CircuitBreakerFactory 的bean， 则使用 CircuitBreakerFactory 创建的 CircuitBreaker 来执行 Feign 接口的方法，从而将方法的执行委托给 CircuitBreaker。

CircuitBreaker 意思是断路器，我们把 方法的执行逻辑 交给断路器，由断路器决定是否要真正的执行方法，下面是最简单的断路器示例。

```java
public class MyCircuitBreaker implements CircuitBreaker {
    public boolean flag;

    @Override
    public <T> T run(Supplier<T> toRun, Function<Throwable, T> fallback) {
        try {
            // 开关打开，那就不执行方法，而是直接执行 fallback 
            if (Boolean.TRUE.equals(flag)) {
                return fallback.apply(new RuntimeException("fast-fail"));
            }
            // 执行方法
            return toRun.get();
        } catch (Exception e) {
            // 出错就使用 fallback 做补偿措施
            return fallback.apply(e);
        }
    }

    public static void main(String[] args) {
        MyCircuitBreaker myCircuitBreaker = new MyCircuitBreaker();
        myCircuitBreaker.run(() -> {
            // method.invoke(obj)
            return "invoke method...";
        }, throwable -> {
            return "invoke fallback...";
        });
    }
}
```

# 核心功能源码分析

## 集成 spring-retry 的断路器

[spring-retry](https://github.com/spring-projects/spring-retry) 为 Spring 应用程序提供声明式重试支持

[示例代码](../source-note-spring-cloud-circuitbreaker/src/main/java/cn/haitaoss/config/RetryCircuitBreakerFactoryConfig.java)

`spring-cloud-circuitbreaker-spring-retry.jar!/META-INF/spring.factories` 的内容

```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
     org.springframework.cloud.circuitbreaker.springretry.SpringRetryAutoConfiguration
```

### SpringRetryAutoConfiguration

```java
/**
 * SpringRetryAutoConfiguration
 *      注册 SpringRetryCircuitBreakerFactory ,其是 CircuitBreakerFactory 的实现类，依赖 Customizer<SpringRetryCircuitBreakerFactory> 对其进行自定义
 *
 *      注：CircuitBreakerFactory 是 spring-cloud-commons 定义的API，若开发人员想实现自定义的断路器应当实现这个类。比如在 OpenFeign 的源码中定义了如何使用 CircuitBreakerFactory {@link FeignCircuitBreakerInvocationHandler#invoke(Object, Method, Object[])}
 * */
```

## 集成 resilience4j 的断路器

[resilience4j](https://github.com/resilience4j/resilience4j) 是一个轻量级的容错库，提供了 重试、断路器、速率限制器、时间限制器、限制并发执行、结果缓存 和Fallback

[示例代码](../source-note-spring-cloud-circuitbreaker/src/main/java/cn/haitaoss/config/Resilience4JCircuitBreakerFactoryConfig.java)

`spring-cloud-circuitbreaker-resilience4j.jar!/META-INF/spring.factories`的部分内容

```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
     org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JAutoConfiguration,\
```

### Resilience4JAutoConfiguration

```java
/**
 * Resilience4JAutoConfiguration
 *      注册一个绑定属性的bean @EnableConfigurationProperties(Resilience4JConfigurationProperties.class)
 *
 *      注册 Resilience4JCircuitBreakerFactory ,其是 CircuitBreakerFactory 的实现类，依赖 Customizer<Resilience4JCircuitBreakerFactory> 对其进行自定义
 *          还依赖 CircuitBreakerRegistry、Resilience4jBulkheadProvider、TimeLimiterRegistry、Resilience4JConfigurationProperties
 *
 *      注册 Resilience4jBulkheadProvider , 可以使用 Customizer<Resilience4jBulkheadProvider> 对齐进行修改
 *
 *      注册 MeterFilter ，不知道是干啥的，反正是 resilience4j 的东西
 *
 *      配置 MeterRegistry
 *
 *      注：
 *          1. 都是些 resilience4j 需要的配置类，想弄明白最好是先学会如何使用
 *          2. 重点关注 Resilience4JCircuitBreakerFactory 即可
 * */
```