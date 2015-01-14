/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.dto.command.cqrs;

/**
 *
 * @author tarasev
 */
public class CompleteSignupCommand implements Command {

    private final long accountId;

    /**
     * @return the accountId
     */
    public long getAccountId() {
        return accountId;
    }

    public CompleteSignupCommand(long accountId) {
        this.accountId = accountId;
    }
}
