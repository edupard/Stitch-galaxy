/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.service;

import com.sg.domain.service.exception.SgCanvasAlreadyExistsException;
import com.sg.domain.service.exception.SgCanvasNotFoundException;
import com.sg.domain.service.exception.SgSignupAlreadyCompletedException;
import com.sg.domain.service.exception.SgThreadAlreadyExistsException;
import com.sg.domain.service.exception.SgThreadNotFoundException;
import com.sg.dto.CanvasDto;
import com.sg.dto.CanvasRefDto;
import com.sg.dto.CanvasUpdateDto;
import com.sg.dto.ThreadDto;
import com.sg.dto.ThreadRefDto;
import com.sg.dto.ThreadUpdateDto;
import com.sg.dto.AccountDto;
import com.sg.dto.CompleteSignupDto;
import com.sg.dto.SignupDto;
import java.util.List;

/**
 *
 * @author tarasev
 */
public interface SgService {
    
    public void create(ThreadDto dto) throws SgThreadAlreadyExistsException;
    public void delete(ThreadRefDto dto) throws SgThreadNotFoundException;
    public void update(ThreadUpdateDto dto) throws SgThreadNotFoundException, SgThreadAlreadyExistsException;
    public List<ThreadDto> listThreads();   
    
    public void create(CanvasDto dto) throws SgCanvasAlreadyExistsException;
    public void delete(CanvasRefDto dto) throws SgCanvasNotFoundException;
    public void update(CanvasUpdateDto dto) throws SgCanvasNotFoundException, SgCanvasAlreadyExistsException;
    public List<CanvasDto> listCanvases();   
    
    public AccountDto getUserByEmail(String email);
    public Long signup(SignupDto dto, String ... roles);
    
    public void completeSignup(Long accountId, CompleteSignupDto dto) throws SgSignupAlreadyCompletedException;
}