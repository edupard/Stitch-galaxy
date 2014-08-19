/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.controllers;

import com.sg.constants.CompleteSignupStatus;
import com.sg.constants.RequestPath;
import com.sg.dto.AccountDto;
import com.sg.domain.service.SgService;
import com.sg.dto.SigninDto;
import com.sg.dto.SinginAttempthResultDto;
import com.sg.constants.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sg.constants.SigninStatus;
import com.sg.constants.SignupStatus;
import com.sg.constants.TokenExpirationType;
import com.sg.domain.service.exception.SgSignupAlreadyCompletedException;
import com.sg.dto.CompleteSignupAttempthResultDto;
import com.sg.dto.CompleteSignupDto;
import com.sg.dto.SignupDto;
import com.sg.dto.SingupAttempthResultDto;
import com.sg.domain.service.SgMailService;
import com.sg.domain.service.AuthToken;
import com.sg.domain.service.SgCryptoServiceImpl;
import java.io.IOException;
import java.util.Arrays;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author tarasev
 */
@Controller
public class SigninSignupController {

    @Autowired
    SgMailService mailService;

    @Autowired
    SgService service;

    @Autowired
    SgCryptoServiceImpl security;

    @RequestMapping(value = RequestPath.REQUEST_SIGNIN, method = RequestMethod.POST)
    public @ResponseBody
    SinginAttempthResultDto signin(@RequestBody SigninDto dto) throws IOException {
        SinginAttempthResultDto result = new SinginAttempthResultDto();
        AccountDto accountDto = service.getUserByEmail(dto.getEmail());
        if (accountDto == null) {
            result.setStatus(SigninStatus.STATUS_USER_NOT_FOUND);
        } else {
            if (!accountDto.getEmailVerified()) {
                result.setStatus(SigninStatus.STATUS_EMAIL_NOT_VERIFIED);
            } else if (!accountDto.getPassword().equals(dto.getPassword())) {
                result.setStatus(SigninStatus.STATUS_WRONG_PASSWORD);
            } else {
                result.setStatus(SigninStatus.STATUS_SUCCESS);

                AuthToken token = new AuthToken(accountDto.getId(), accountDto.getRoles(), TokenExpirationType.USER_SESSION_TOKEN);
                result.setAuthToken(security.getTokenString(token));
            }
        }
        return result;
    }

    @RequestMapping(value = RequestPath.REQUEST_SIGNUP_USER, method = RequestMethod.POST)
    public @ResponseBody
    SingupAttempthResultDto signupUser(@RequestBody SignupDto dto) throws IOException {
        return sinupUserWithRoles(dto, Roles.ROLE_USER);
    }

    @RequestMapping(value = RequestPath.REQUEST_SIGNUP_ADMIN_USER, method = RequestMethod.POST)
    public @ResponseBody
    SingupAttempthResultDto signupAdmin(@RequestBody SignupDto dto) throws IOException {
        return sinupUserWithRoles(dto, Roles.ROLE_USER, Roles.ROLE_ADMIN);
    }

    private SingupAttempthResultDto sinupUserWithRoles(SignupDto dto, String... roles) throws IOException {
        SingupAttempthResultDto result = new SingupAttempthResultDto();
        AccountDto accountDto = service.getUserByEmail(dto.getEmail());
        if (accountDto != null) {
            if (accountDto.getEmailVerified() == Boolean.TRUE) {
                result.setStatus(SignupStatus.STATUS_EMAIL_ALREADY_REGISTERED);
            } else {
                AuthToken authToken = new AuthToken(accountDto.getId(), accountDto.getRoles(), TokenExpirationType.NEVER_EXPIRES);

                mailService.sendEmailVerificationEmail(security.getTokenString(authToken), dto.getEmail());
                result.setStatus(SignupStatus.STATUS_CONFIRMATION_EMAIL_RESENT);
            }
        } else {
            Long userId = service.signup(dto, roles);

            AuthToken authToken = new AuthToken(userId, Arrays.asList(roles), TokenExpirationType.NEVER_EXPIRES);
            mailService.sendEmailVerificationEmail(security.getTokenString(authToken), dto.getEmail());
            result.setStatus(SignupStatus.STATUS_SUCCESS);
        }
        return result;
    }

    @RequestMapping(value = RequestPath.REQUEST_COMPLETE_SIGNUP, method = RequestMethod.GET)
    public @ResponseBody
    CompleteSignupAttempthResultDto completeSignup(@RequestBody CompleteSignupDto dto) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CompleteSignupAttempthResultDto attemptResult = new CompleteSignupAttempthResultDto();

        try {
            service.completeSignup(userId, dto);
        } catch (SgSignupAlreadyCompletedException e) {
            attemptResult.setStatus(CompleteSignupStatus.STATUS_ALREADY_COMPLETED);
            return attemptResult;
        }

        attemptResult.setStatus(CompleteSignupStatus.STATUS_SUCCESS);
        return attemptResult;
    }

}
