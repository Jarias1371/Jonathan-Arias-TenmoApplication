package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

import static com.techelevator.dao.JdbcUserDaoTests.*;

public class JdbcAccountDaoTests extends BaseDaoTests {

    protected static final Account account1 = new Account(1, USER_1.getId(), new BigDecimal("1000.00"));
    protected static final Account account2 = new Account(2, USER_2.getId(), new BigDecimal("1000.00"));
    private static final Account account3 = new Account(3, 1003, new BigDecimal("1000.00"));

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void get_account_id_should_get_expected_account_id() {
        Account actual = sut.getAccountByAccountId(1);
        Assert.assertEquals(account1.getId(), actual.getId());
    }

    @Test
    public void get_account_id_should_get_expected_account_user_id() {
        Account actual = sut.getAccountByUserId(1001);
        Assert.assertEquals(account1.getUserId(), actual.getUserId());
    }



    @Test
    public void get_back_new_transaction_id_after_execution_of_transaction () {
        Integer actualId = sut.executeTransaction(account1.getUserId(), account2.getUserId(), new BigDecimal("100.00"));
        Integer expected = 1;
        Assert.assertEquals(expected, actualId);
    }





}
