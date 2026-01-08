package com.devalFinance.presentation.controller;

import com.devalFinance.application.dto.request.CreateAccountRequest;
import com.devalFinance.application.dto.request.UpdateAccountRequest;
import com.devalFinance.application.dto.response.AccountResponse;
import com.devalFinance.application.usecase.account.CreateAccountUseCase;
import com.devalFinance.application.usecase.account.DeleteAccountUseCase;
import com.devalFinance.application.usecase.account.GetAccountByIdUseCase;
import com.devalFinance.application.usecase.account.GetAccountsUseCase;
import com.devalFinance.application.usecase.account.UpdateAccountUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    
    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountsUseCase getAccountsUseCase;
    private final GetAccountByIdUseCase getAccountByIdUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;
    private final DeleteAccountUseCase deleteAccountUseCase;

    public AccountController(CreateAccountUseCase createAccountUseCase,
                            GetAccountsUseCase getAccountsUseCase,
                            GetAccountByIdUseCase getAccountByIdUseCase,
                            UpdateAccountUseCase updateAccountUseCase,
                            DeleteAccountUseCase deleteAccountUseCase) {
        this.createAccountUseCase = createAccountUseCase;
        this.getAccountsUseCase = getAccountsUseCase;
        this.getAccountByIdUseCase = getAccountByIdUseCase;
        this.updateAccountUseCase = updateAccountUseCase;
        this.deleteAccountUseCase = deleteAccountUseCase;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request,
            @CurrentUser UUID userId) {
        AccountResponse response = createAccountUseCase.execute(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAccounts(@CurrentUser UUID userId) {
        List<AccountResponse> accounts = getAccountsUseCase.execute(userId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccountById(
            @PathVariable UUID accountId,
            @CurrentUser UUID userId) {
        AccountResponse account = getAccountByIdUseCase.execute(accountId, userId);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable UUID accountId,
            @Valid @RequestBody UpdateAccountRequest request,
            @CurrentUser UUID userId) {
        AccountResponse account = updateAccountUseCase.execute(accountId, request, userId);
        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable UUID accountId,
            @CurrentUser UUID userId) {
        deleteAccountUseCase.execute(accountId, userId);
        return ResponseEntity.noContent().build();
    }
}

