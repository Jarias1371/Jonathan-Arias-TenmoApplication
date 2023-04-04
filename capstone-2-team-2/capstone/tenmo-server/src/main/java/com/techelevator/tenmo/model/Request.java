package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Request {
    private int id;
    private int payingUserId;
    private int receivingUserId;
    private String requestStatus;
    private BigDecimal transferAmount;
    private Timestamp requestTime;

    public int getRequestId() {
        return id;
    }

    public void setRequestId(int id) {
        this.id = id;
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

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public Timestamp getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Timestamp requestTime) {
        this.requestTime = requestTime;
    }
}
