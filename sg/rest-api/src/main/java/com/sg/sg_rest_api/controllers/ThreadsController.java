/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.sg_rest_api.controllers;

import com.sg.constants.RequestPath;
import com.sg.constants.ThreadOperationStatus;
import com.sg.dto.request.ThreadCreateDto;
import com.sg.dto.request.ThreadDeleteDto;
import com.sg.dto.request.ThreadUpdateDto;
import com.sg.domain.service.SgService;
import com.sg.domain.service.exception.SgDataValidationException;
import com.sg.domain.service.exception.SgThreadAlreadyExistsException;
import com.sg.domain.service.exception.SgThreadNotFoundException;
import com.sg.dto.response.OperationStatusDto;
import com.sg.dto.response.ThreadsListDto;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ThreadsController {

    @Autowired
    SgService service;

    @RequestMapping(value = RequestPath.REQUEST_THREAD_ADD, method = RequestMethod.POST)
    public @ResponseBody
    OperationStatusDto create(@Valid @RequestBody ThreadCreateDto dto) throws SgDataValidationException {
        OperationStatusDto result = new OperationStatusDto();
        result.setStatus(ThreadOperationStatus.STATUS_SUCCESS);
        try {
            service.create(dto);
        } catch (SgThreadAlreadyExistsException e) {
            result.setStatus(ThreadOperationStatus.THREAD_ALREADY_EXISTS);
        }
        return result;
    }

    @RequestMapping(value = RequestPath.REQUEST_THREAD_LIST, method = RequestMethod.GET)
    public @ResponseBody
    ThreadsListDto list() {
        return service.listThreads();
    }

    @RequestMapping(value = RequestPath.REQUEST_THREAD_DELETE, method = RequestMethod.POST)
    public @ResponseBody
    OperationStatusDto delete(@Valid @RequestBody ThreadDeleteDto dto) throws SgDataValidationException {
        OperationStatusDto result = new OperationStatusDto();
        result.setStatus(ThreadOperationStatus.STATUS_SUCCESS);
        try {
            service.delete(dto);
        } catch (SgThreadNotFoundException e) {
            result.setStatus(ThreadOperationStatus.THREAD_NOT_FOUND);
        }
        return result;
    }

    @RequestMapping(value = RequestPath.REQUEST_THREAD_UPDATE, method = RequestMethod.POST)
    public @ResponseBody
    OperationStatusDto update(@Valid @RequestBody ThreadUpdateDto dto) throws SgDataValidationException {
        OperationStatusDto result = new OperationStatusDto();
        result.setStatus(ThreadOperationStatus.STATUS_SUCCESS);
        try {
            service.update(dto);
        } catch (SgThreadAlreadyExistsException e) {
            result.setStatus(ThreadOperationStatus.THREAD_ALREADY_EXISTS);
        } catch (SgThreadNotFoundException e) {
            result.setStatus(ThreadOperationStatus.THREAD_NOT_FOUND);
        }
        return result;
    }
}