package com.techelevator.tenmo.model;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class RequestDto {

    public String payingUserName;
    public String receivingUserName;
    @DecimalMin(value = "1.00", message = "cannot be zero, negative, or non-number")
    public BigDecimal transferAmount;
    public Integer requestId;
    public String status;


    public RequestDto(String payingUserName, String receivingUserName, BigDecimal transferAmount, Integer requestId, String status) {
        this.payingUserName = payingUserName;
        this.receivingUserName = receivingUserName;
        this.transferAmount = transferAmount;
        this.requestId = requestId;
        this.status = status;
    }

}
