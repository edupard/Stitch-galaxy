/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.service;

import com.sg.domain.service.exception.SgAccountNotFoundException;
import com.sg.domain.service.exception.SgCanvasAlreadyExistsException;
import com.sg.domain.service.exception.SgCanvasNotFoundException;
import com.sg.domain.service.exception.SgDataValidationException;
import com.sg.domain.service.exception.SgEmailNonVerifiedException;
import com.sg.domain.service.exception.SgInstallationAlreadyCompletedException;
import com.sg.domain.service.exception.SgInvalidPasswordException;
import com.sg.domain.service.exception.SgSignupAlreadyCompletedException;
import com.sg.domain.service.exception.SgSignupForRegisteredButNonVerifiedEmailException;
import com.sg.domain.service.exception.SgThreadAlreadyExistsException;
import com.sg.domain.service.exception.SgThreadNotFoundException;
import com.sg.dto.request.CanvasCreateDto;
import com.sg.dto.request.CanvasDeleteDto;
import com.sg.dto.request.CanvasUpdateDto;
import com.sg.dto.request.ThreadCreateDto;
import com.sg.dto.request.ThreadDeleteDto;
import com.sg.dto.request.ThreadUpdateDto;
import com.sg.dto.request.CompleteSignupDto;
import com.sg.dto.response.AccountDto;
import com.sg.dto.request.SigninDto;
import com.sg.dto.request.SignupDto;
import com.sg.dto.response.CanvasesListDto;
import com.sg.dto.response.ThreadsListDto;
import java.util.List;

/**
 *
 * @author tarasev
 */
public interface SgService {
    
    public void create(ThreadCreateDto dto) throws SgDataValidationException, SgThreadAlreadyExistsException;
    public void delete(ThreadDeleteDto dto) throws SgDataValidationException, SgThreadNotFoundException;
    public void update(ThreadUpdateDto dto) throws SgDataValidationException, SgThreadNotFoundException, SgThreadAlreadyExistsException;
    public ThreadsListDto listThreads();   
    
    public void create(CanvasCreateDto dto) throws SgDataValidationException, SgCanvasAlreadyExistsException;
    public void delete(CanvasDeleteDto dto) throws SgDataValidationException, SgCanvasNotFoundException;
    public void update(CanvasUpdateDto dto) throws SgDataValidationException, SgCanvasNotFoundException, SgCanvasAlreadyExistsException;
    public CanvasesListDto listCanvases();   
   
    public void install() throws SgInstallationAlreadyCompletedException;
    
    public void signupUser(SignupDto dto) throws SgDataValidationException, SgSignupForRegisteredButNonVerifiedEmailException, SgSignupAlreadyCompletedException;
    public void signupAdmin(SignupDto dto) throws SgDataValidationException, SgSignupForRegisteredButNonVerifiedEmailException, SgSignupAlreadyCompletedException;
    
    //put email to dto and check validity
    public Long getAccountIdByRegistrationEmail(String email) throws SgDataValidationException, SgAccountNotFoundException;
    //put accountId to dto and check validity
    public void completeSignup(Long accountId, CompleteSignupDto dto) throws SgDataValidationException, SgAccountNotFoundException, SgSignupAlreadyCompletedException;
    //put accountId to dto and check validity
    public AccountDto getAccountInfo(Long accountId) throws SgDataValidationException, SgAccountNotFoundException;
    
    public void signIn(SigninDto dto) throws SgDataValidationException, SgAccountNotFoundException, SgInvalidPasswordException, SgEmailNonVerifiedException;
    
    public void ping() throws Exception;
}
