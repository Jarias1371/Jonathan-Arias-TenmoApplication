package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;

import java.util.List;

public interface TransactionDao {

     Transaction getTransactionById(int transactionId);
     List<Transaction> getAllTransactions(int id);

    }



