package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    Account getAccountByAccountId(int id);

    Account getAccountByUserId(int id);

    Integer executeTransaction(int payerId, int receivingId, BigDecimal transferAmount);



}
