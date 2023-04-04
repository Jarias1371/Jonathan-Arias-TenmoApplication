package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Request;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcRequestDao implements RequestDao{
    private final JdbcTemplate jdbcTemplate;

    public JdbcRequestDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Integer createNewRequest(BigDecimal transferAmount, int payerId, int requesterId) {
        final String sql = "INSERT INTO requests(paying_user_id, receiving_user_id, transfer_amount)\n" +
                "VALUES(?, ?, ?) RETURNING id;";

        return jdbcTemplate.queryForObject(sql, Integer.class, payerId, requesterId, transferAmount);
    }

    @Override
    public List<Request> getPendingRequests(int id) {
        final String sql = "SELECT id, paying_user_id, receiving_user_id, transfer_amount, request_time, request_status\n" +
                            "\tFrom requests\n" +
                             "\tWHERE (paying_user_id = ? OR receiving_user_id = ?) AND request_status = 'Pending';";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id, id);
        List<Request> pendingRequestList = new ArrayList<>();
        while(rs.next()){
            pendingRequestList.add(mapRequest(rs));
        }

        return pendingRequestList;
    }

    @Override
    public Request getRequestById(int id) {
        Request request = null;
        final String sql = "SELECT id, paying_user_id, receiving_user_id, transfer_amount, request_time, request_status\n" +
                "\tFrom requests\n" +
                "\tWHERE id = ?;";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        if(rs.next()){
            request = mapRequest(rs);

        }
        return request;
    }

    @Override
    public boolean updateRequest(int id, String status) {
        final String sql = "UPDATE requests\n" +
                          "SET request_status = ?\n" +
                          "WHERE id = ?";

        jdbcTemplate.update(sql, status, id);
        return true;
    }

    //created mapRequest for readability purposes.
    private Request mapRequest(SqlRowSet rs) {
        Request request = new Request();
        request.setRequestId(rs.getInt("id"));
        request.setPayingUserId(rs.getInt("paying_user_id"));
        request.setReceivingUserId(rs.getInt("receiving_user_id"));
        request.setRequestTime(rs.getTimestamp("request_time"));
        request.setTransferAmount(rs.getBigDecimal("transfer_amount"));
        request.setRequestStatus(rs.getString("request_status"));
        return request;
    }
}
