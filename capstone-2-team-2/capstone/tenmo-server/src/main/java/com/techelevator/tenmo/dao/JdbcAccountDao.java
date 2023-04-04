package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {
    private final JdbcTemplate jdbcTemplate;


    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccountByAccountId(int id) {
        final String sql = "SELECT id, user_id, balance\n" +
                "\tFROM accounts\n" +
                "\tWHERE id = ?;";
        SqlRowSet rs = this.jdbcTemplate.queryForRowSet(sql, id);
        Account account = null;
        if (rs.next()) {
            account = new Account();
            account.setId(rs.getInt("id"));
            account.setUserId(rs.getInt("user_id"));
            account.setBalance(rs.getBigDecimal("balance"));
        }
        return account;
    }


    @Override
    public Account getAccountByUserId(int id) {
        final String sql = "SELECT id, user_id, balance\n" +
                "\tFROM accounts\n" +
                "\tWHERE user_id = ?;";
        SqlRowSet rs = this.jdbcTemplate.queryForRowSet(sql, id);
        Account account = null;
        if (rs.next()) {
            account = new Account();
            account.setId(rs.getInt("id"));
            account.setUserId(rs.getInt("user_id"));
            account.setBalance(rs.getBigDecimal("balance"));
        }
        return account;
    }

    @Override
    public Integer executeTransaction(int payerId, int receivingId, BigDecimal transferAmount) {
        final String sql =
                "UPDATE accounts\n" +
                "SET balance = balance + ?\n" +
                "WHERE user_id = ?; \n" +
                "UPDATE accounts\n" +
                "SET balance = balance - ?\n" +
                "WHERE user_id = ?;";

        jdbcTemplate.update(sql, transferAmount, receivingId, transferAmount, payerId);

        final String sql2 = "INSERT INTO transactions(paying_user_id, receiving_user_id, transfer_amount)\n" +
                "\tVALUES(?, ?, ?) RETURNING id;\n";

        return jdbcTemplate.queryForObject(sql2, Integer.class, payerId, receivingId, transferAmount);

    }

}
