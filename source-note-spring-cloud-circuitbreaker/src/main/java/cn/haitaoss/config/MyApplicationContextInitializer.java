package cn.haitaoss.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class MyApplicationContextInitializer implements ApplicationContextInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        applicationContext.getEnvironment()
                .getConversionService()
                .addConverter(new Converter<String, Class>() {
                    @Override
                    public Class convert(String source) {
                        try {
                            return Class.forName(source);
                        } catch (Exception e) {
                            log.warn("String converter to Class Fail, error msg is {}", e.getMessage());
                        }
                        return null;
                    }
                });
    }
}