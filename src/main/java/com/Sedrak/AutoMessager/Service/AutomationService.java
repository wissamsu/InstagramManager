package com.Sedrak.AutoMessager.Service;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import com.Sedrak.AutoMessager.Model.Account;
import com.Sedrak.AutoMessager.Repository.AccountRepository;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AutomationService {

  private final AccountRepository accountRepo;
  private final BrowserContext context;

  public boolean checkIfUsernameExists(String username) throws IOException {

    Connection.Response response = Jsoup
        .connect("https://www.instagram.com/api/v1/users/web_profile_info/?username=" + username)
        .userAgent(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0 Safari/537.36")
        .referrer("https://www.instagram.com/")
        .header("x-ig-app-id", "936619743392459")
        .timeout(10_000)
        .followRedirects(true)
        .ignoreHttpErrors(true)
        .ignoreContentType(true)
        .execute();

    int statusCode = response.statusCode();
    if (statusCode == 200) {
      String body = response.body();
      if (body.contains("\"data\":{\"user\"")) {
        return true;
      }

      throw new IOException("Instagram returned a successful response, but it did not contain user data.");
    }
    if (statusCode == 404) {
      return false;
    }

    throw new IOException(
        "Instagram returned status "
            + statusCode
            + " for username "
            + username
            + ". Try again later or use an authenticated API if this continues.");
  }

  public boolean checkIfAccountResponded(Page page, Account account) throws Exception {
    page.waitForLoadState();
    page.waitForSelector("div[aria-describedby='Message']");
    page.waitForTimeout(3000);
    System.out.println(page.locator("a[aria-label^='Open the profile page of']").count());
    if (page.locator("a[aria-label^='Open the profile page of']").count() > 0) {
      return true;
    }
    return false;
  }

  public void sendMessages(String username) throws Exception {
    Account account = accountRepo.findByUsername(username).orElseThrow(() -> new Exception("Account not found"));

    try (Page page = context.newPage()) {
      if (checkIfUsernameExists(username)) {
        System.out.println("Account exists");
      } else {
        System.out.println("Account doesnt exist");
        accountRepo.delete(account);
        return;
      }
      page.navigate("https://www.instagram.com/" + username);
      page.getByText("Message").nth(2).highlight();
      page.getByText("Message").nth(2).click();
      if (checkIfAccountResponded(page, account)) {
        System.out.println("Account already responded");
        return;
      }
      if (!account.isMessage1Sent()) {
        page.locator("div[aria-describedby='Message']").fill(account.getMessage1());
        page.locator("svg[aria-label='Send']").first().click();
        account.setMessagesSent(account.getMessagesSent() + 1);
        account.setMessage1Sent(true);
        accountRepo.save(account);
        return;
      }

      if (!account.isMessage2Sent()) {
        page.locator("div[aria-describedby='Message']").fill(account.getMessage2());
        page.locator("svg[aria-label='Send']").first().click();
        account.setMessagesSent(account.getMessagesSent() + 1);
        account.setMessage2Sent(true);
        accountRepo.save(account);
        return;
      }
      if (!account.isMessage3Sent()) {
        page.locator("div[aria-describedby='Message']").fill(account.getMessage3());
        page.locator("svg[aria-label='Send']").first().click();
        account.setMessagesSent(account.getMessagesSent() + 1);
        account.setMessage3Sent(true);
        accountRepo.save(account);
        return;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
