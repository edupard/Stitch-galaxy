/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.domain.service.exception;

/**
 *
 * @author tarasev
 */
public class SgInstallationAlreadyCompletedException extends SgServiceLayerRuntimeException {
    public SgInstallationAlreadyCompletedException()
    {
        super("Stitch galaxy service installtion already completed.");
    }
}