package com.techelevator.tenmo.model;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class TransactionDto {

    public String payingUserName;
    public String receivingUserName;
    @DecimalMin(value = "1.00", message = "cannot be zero, negative, or non-number")
    public BigDecimal transferAmount;

    public TransactionDto(String payingUserName, String receivingUserName, BigDecimal transferAmount) {
        this.payingUserName = payingUserName;
        this.receivingUserName = receivingUserName;
        this.transferAmount = transferAmount;
    }
}
