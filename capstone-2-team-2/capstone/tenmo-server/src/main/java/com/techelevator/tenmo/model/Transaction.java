package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transaction {

    private int transactionId;
    private int payingUserId;
    private int receivingUserId;
    private BigDecimal transferAmount;
    private Timestamp transactionTime;

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getPayingUserId() {
        return payingUserId;
    }

    public void setPayingUserId(int payingUserId) {
        this.payingUserId = payingUserId;
    }

    public int getReceivingUserId() {
        return receivingUserId;
    }

    public void setReceivingUserId(int receivingUserId) {
        this.receivingUserId = receivingUserId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public void setTransactionTime(Timestamp transactionTime) {
        this.transactionTime = transactionTime;
    }

    public Timestamp getTransactionTime() {
        return transactionTime;
    }

}
