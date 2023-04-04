package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransactionDao implements TransactionDao{
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransactionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Transaction getTransactionById(int transactionId) {
        String sql = "SELECT id, paying_user_id, receiving_user_id, transfer_amount, transaction_time\n" +
                "FROM transactions\n" +
                "WHERE id = ?;";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, transactionId);
        Transaction transaction = null;
        if(rs.next()) {
            transaction = new Transaction();
            transaction.setTransactionId(rs.getInt("id"));
            transaction.setPayingUserId(rs.getInt("paying_user_id"));
            transaction.setReceivingUserId(rs.getInt("receiving_user_id"));
            transaction.setTransferAmount(rs.getBigDecimal("transfer_amount"));
            transaction.setTransactionTime(rs.getTimestamp("transaction_time"));
        }
        return transaction;
    }

    @Override
    public List<Transaction> getAllTransactions(int id) {
        final String sql = "SELECT id, paying_user_id, receiving_user_id, transfer_amount, transaction_time\n" +
                "\tFROM transactions\n" +
                "\tWHERE paying_user_id = ? OR receiving_user_id = ?;";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql,id, id);
        List<Transaction> transactionList = new ArrayList<>();
        while(rs.next()) {
            Transaction transaction = new Transaction();
            transaction.setTransactionId(rs.getInt("id"));
            transaction.setPayingUserId(rs.getInt("paying_user_id"));
            transaction.setReceivingUserId(rs.getInt("receiving_user_id"));
            transaction.setTransactionTime(rs.getTimestamp("transaction_time"));
            transaction.setTransferAmount(rs.getBigDecimal("transfer_amount"));
            transactionList.add(transaction);
        }
        return transactionList;

    }


}
