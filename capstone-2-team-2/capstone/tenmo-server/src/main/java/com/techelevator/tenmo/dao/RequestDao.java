package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Request;

import java.math.BigDecimal;
import java.util.List;

public interface RequestDao {

    Integer createNewRequest (BigDecimal transferAmount, int payerId, int requesterId);
    List<Request> getPendingRequests(int id);

    Request getRequestById(int id);

    boolean updateRequest(int id, String status);


}
