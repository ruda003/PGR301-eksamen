package com.pgr301.exam;

import com.pgr301.exam.model.Account;
import com.pgr301.exam.model.Transaction;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static java.math.BigDecimal.*;
import static java.util.Optional.ofNullable;

@RestController
public class BankAccountController implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private BankingCoreSystmeService bankService;

    protected static void postError(String msg, Throwable cause) {
        Metrics.counter("bankaccount.errors", "error", msg, "cause", cause.getMessage()).increment();
    }
    protected static void postError(String msg) {
       Metrics.counter("bankaccount.errors", "error", msg, "cause", null).increment();
    }
    protected static void postError(RuntimeException ex) {
        StringBuilder output = new StringBuilder();
        for (StackTraceElement elem : ex.getStackTrace()) {
           output.append(elem.toString()).append("\n");
        }
        Metrics.counter("bankaccount.errors", "error", output.toString(), "cause", null).increment();
    }

    @PostMapping(path = "/account/{fromAccount}/transfer/{toAccount}", consumes = "application/json", produces = "application/json")
    public void transfer(@RequestBody Transaction tx, @PathVariable String fromAccount, @PathVariable String toAccount) {
        bankService.transfer(tx, fromAccount, toAccount);
    }

    @PostMapping(path = "/account", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Account> updateAccount(@RequestBody Account a) {
        bankService.updateAccount(a);
        return new ResponseEntity<>(a, HttpStatus.OK);
    }

    @GetMapping(path = "/account/{accountId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Account> balance(@PathVariable String accountId) {
        Account account = ofNullable(bankService.getAccount(accountId)).orElseThrow(AccountNotFoundException::new);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "video not found")
    public static class AccountNotFoundException extends RuntimeException {
        public AccountNotFoundException(String msg, Throwable cause) {
            super(msg, cause);
            BankAccountController.postError(msg, cause);
        }
        public AccountNotFoundException(String msg) {
           super(msg);
            BankAccountController.postError(msg);
        }

        public AccountNotFoundException() {
           super();
            BankAccountController.postError(this);
        }
    }
}

