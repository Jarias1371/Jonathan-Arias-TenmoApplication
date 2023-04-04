package com.techelevator.tenmo.controller;

//TenmoController controls all services within the Tenmo application. Requests for account information
//account transfers, and the account queue for Requests

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.RequestDao;
import com.techelevator.tenmo.dao.TransactionDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TenmoController {

    private AccountDao daoAccount;
    private UserDao daoUser;
    private TransactionDao daoTransaction;
    private RequestDao daoRequest;

    //Dependency injection AccountDao, UserDao, TransactionDao, and RequestDao
    public TenmoController(AccountDao daoAccount, UserDao daoUser, TransactionDao daoTransaction, RequestDao daoRequest) {
        this.daoAccount = daoAccount;
        this.daoUser = daoUser;
        this.daoTransaction = daoTransaction;
        this.daoRequest = daoRequest;
    }
    //User can check current account balance
    //Most users will start here after login to confirm their 1000 tenmo dollars
    //Users are able to check only their account balance and can not see other user balances
    @GetMapping("/accounts/")
    public AccountDto getAccount(Principal principal) {
        final User user = daoUser.findByUsername(principal.getName());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no account with that ID");
        }
        final Account account = daoAccount.getAccountByUserId(user.getId());
        if (account.getUserId() != (user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "access denied - nice try Mike");
        }

        final AccountDto accountDto = new AccountDto();
        accountDto.id = account.getId();
        accountDto.userId = account.getUserId();
        accountDto.balance = account.getBalance();

        return accountDto;
    }


    //If a user wants to send a payment or request a payment they are able to check all currently registered users
    //users will only be able to see userid and usernames on this request
    @GetMapping("/users/")
    public List<User> getUserList() {
        return daoUser.findAll();
    }

    //Users can check all of their completed transactions
    @GetMapping("/transactions/")
    public List<Transaction> listAllTransactions(Principal principal) {
        User user = daoUser.findByUsername(principal.getName());
     return daoTransaction.getAllTransactions(user.getId());
    }

    //users can search for a specific transaction id
    @GetMapping("/transactions/{id}") // need principal
    public Transaction getTransactionById(@PathVariable int id) {
        final Transaction transaction = daoTransaction.getTransactionById(id);
        if (transaction == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no transaction with that ID");
        }
        return transaction;

    }

    //Users can create a new transaction
    //when a user creates a transaction it does not need to be approved because it is not pending
    //users can not send money to themselves, error handling for multiple cases.
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/transactions/create")
    public Integer createNewTransaction(Principal principal, @Valid @RequestBody TransactionDto transactionDto) {
        User payer = daoUser.findByUsername(principal.getName());
        User recipient = daoUser.findByUsername(transactionDto.receivingUserName);
        if(!payer.getUsername().equals(transactionDto.payingUserName)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "unauthorized");
        }
        if (payer == null || recipient== null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no user with that name");
        }
        if (payer.equals(recipient)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "cannot send money to yourself");
        }

        if(transactionDto.transferAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cannot send amount of $0 or negative amount");
        }

        return daoAccount.executeTransaction(payer.getId(), recipient.getId(), transactionDto.transferAmount);
    }

    //users can create a new request
    //when a request is created it is automatically "PENDING" until the requested user either selects, "APPROVED" or "DECLINED"
    //request will sit in the request table queue in pending status
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/requests/new")
    public boolean createRequest(Principal principal, @Valid @RequestBody RequestDto requestDto) {
        User recipient = daoUser.findByUsername(principal.getName());
        User payer = daoUser.findByUsername(requestDto.payingUserName);
        if(!recipient.getUsername().equals(requestDto.receivingUserName)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "unauthorized");
        }
        if(payer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no user with that name");
        }
        if(recipient.equals(payer)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "cannot request money from yourself weirdo");
        }
        if(requestDto.transferAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cannot send amount of $0 or negative amount");
        }

        int requestId = daoRequest.createNewRequest(requestDto.transferAmount, payer.getId(), recipient.getId());

        return requestId > 0;
    }

    //Users are able to see and check their currently pending request queue
    @GetMapping("/requests/pending")
    public List<Request> getPendingRequestList (Principal principal) {
        User user = daoUser.findByUsername(principal.getName());
        return daoRequest.getPendingRequests(user.getId());
    }


    //Users will either APPROVE or DECLINED in this call
    //Once approved acct balance and transaction table will be updated
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/requests/update")
    public boolean updateRequest( @RequestBody RequestDto requestDto, Principal principal) {
        User payer = daoUser.findByUsername(principal.getName());
        Request request = daoRequest.getRequestById(requestDto.requestId);
//        User recipient = daoUser.findByUsername(requestDto.receivingUserName);
        if(request == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found");
       }
        Account payerAccount = daoAccount.getAccountByUserId(payer.getId());
        if(request.getPayingUserId() != payerAccount.getUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "unauthorized");
        }
        if(!request.getRequestStatus().equals("Pending")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "unauthorized");
        }

        if(requestDto.status.equalsIgnoreCase("APPROVE")) {
            daoAccount.executeTransaction(request.getPayingUserId(), request.getReceivingUserId(), request.getTransferAmount());
            daoRequest.updateRequest(request.getRequestId(), requestDto.status);
        }
        if(requestDto.status.equalsIgnoreCase("DECLINE")){
            daoRequest.updateRequest(request.getRequestId(), requestDto.status);
        }

        return request.getRequestId() > 0;
    }





}
