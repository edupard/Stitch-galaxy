/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hillert.spring.config;

import com.hillert.spring.validation.service.impl.NoOpClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.validation.Validator;
import javax.validation.Validator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 *
 * @author tarasev
 */
@Configuration
public class ValidatorContextConfig {
    
    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }
    
    @Bean
    public MethodValidationPostProcessor validationBeanPostProcessor()
    {
        return new MethodValidationPostProcessor();
    }
}
