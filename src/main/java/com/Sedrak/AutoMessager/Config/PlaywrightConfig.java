package com.Sedrak.AutoMessager.Config;

import java.nio.file.Paths;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Browser.NewContextOptions;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

@Configuration
public class PlaywrightConfig {

  @Bean(destroyMethod = "close")
  Playwright playwright() {
    return Playwright.create();
  }

  @Bean(destroyMethod = "close")
  Browser browser(Playwright playwright) {
    return playwright.chromium()
        .launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(500d));
  }

  @Bean(destroyMethod = "close")
  BrowserContext browserContext(Browser browser) {

    if (Paths.get("contexts/instagram.json").toFile().exists()) {
      return browser.newContext(new NewContextOptions().setStorageStatePath(Paths.get("contexts/instagram.json")));
    }

    BrowserContext context = browser.newContext();
    Page page = context.newPage();
    page.navigate("https://www.instagram.com/accounts/login/");
    page.getByLabel("Mobile number, username or email").fill("wissamguild@gmail.com");
    page.getByLabel("Password").fill("Dontplay.1234");
    page.locator("span:has-text('Log in')").nth(1).click();
    page.waitForTimeout(5000);
    context.storageState(
        new BrowserContext.StorageStateOptions()
            .setPath(Paths.get("contexts/instagram.json")));
    page.close();
    return context;
  }

}
