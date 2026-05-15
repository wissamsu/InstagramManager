package com.Sedrak.AutoMessager.Controller;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Sedrak.AutoMessager.Model.Account;
import com.Sedrak.AutoMessager.Service.AccountService;
import com.Sedrak.AutoMessager.Service.AutomationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/automation")
@RequiredArgsConstructor
@Tag(name = "Automation")
public class AutomationController {

  private final AutomationService automationService;
  private final AccountService accountService;

  private Object synchronizedObject = new Object();

  @PostMapping("/sendMessagesToEveryoneNow")
  @Operation(summary = "Send messages to everyone now")
  @Scheduled(cron = "0 0 0 * * *")
  public void sendMessages() {
    List<Account> accounts = accountService.findAll();
    try {
      synchronized (synchronizedObject) {
        for (Account account : accounts) {
          automationService.sendMessages(account.getUsername());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
