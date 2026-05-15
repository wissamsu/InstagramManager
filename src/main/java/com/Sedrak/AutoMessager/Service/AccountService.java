package com.Sedrak.AutoMessager.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Sedrak.AutoMessager.Model.Account;
import com.Sedrak.AutoMessager.Repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

  private final AccountRepository accountRepo;
  private final AutomationService automationService;

  @Transactional(readOnly = true)
  public List<Account> findAll() {
    return accountRepo.findAll();
  }

  public String save(Account account) {
    try {
      if (automationService.checkIfUsernameExists(account.getUsername())) {
        accountRepo.save(account);
        return "saved";
      }
      return "Username doesnt belong to any account check again bro";
    } catch (Exception e) {
      return e.getMessage();
    }
  }

}
