package com.sg.sg_rest_api.test.configuration;

import com.sg.domain.service.SgService;
import com.sg.sg_rest_api.mail.MailService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tarasev
 */
@Configuration
public class MailMockContext {
    @Bean
    public MailService mailService() {
        return Mockito.mock(MailService.class);
    }
}