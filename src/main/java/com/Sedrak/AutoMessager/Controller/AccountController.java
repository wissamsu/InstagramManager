package com.Sedrak.AutoMessager.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Sedrak.AutoMessager.Model.Account;
import com.Sedrak.AutoMessager.Service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
@Tag(name = "Account", description = "Account operations")
public class AccountController {

  private final AccountService accountService;

  @GetMapping("/findAll")
  public List<Account> findAll() {
    return accountService.findAll();
  }

  @PostMapping("/save/{username}")
  @Operation(summary = "Use this to add a new instagram account")
  public String save(@PathVariable String username) {
    Account account = Account.builder().username(username).build();
    return accountService.save(account);
  }

}
