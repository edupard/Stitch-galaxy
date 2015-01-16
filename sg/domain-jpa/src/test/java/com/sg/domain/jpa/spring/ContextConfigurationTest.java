package com.sg.domain.jpa.spring;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.sg.domain.jpa.spring.PersistenceContextConfig;
import com.sg.domain.jpa.spring.test.TestJpaServicePropertiesContextConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 *
 * @author tarasev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {TestJpaServicePropertiesContextConfiguration.class, PersistenceContextConfig.class})
public class ContextConfigurationTest {
        
    public ContextConfigurationTest() {
    }
    
    @Test
    public void contextTest(){
    }
}
