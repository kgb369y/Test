package foo.fausets;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import foo.webdriver.StartChromeDriver;

public class Chicken implements Runnable {
  private static Logger log = LoggerFactory.getLogger(Chicken.class);
  int BEEP = 1500;
  int WAITB = 20;
  int MINUTES_TOWAIT = 5;

  WebDriver driver;
  String url;
  String user;
  String pass;
  Point p;
  Dimension s;
  boolean ascendent;
  DesiredCapabilities capabilities = DesiredCapabilities.chrome();

  public Chicken(String url, String user, String pass, Point p, Dimension s, boolean ascendent) {
    log.info("Running tests");
    log.info("I'm testing slf4j the url is, {} " + url);
    this.url = url;
    this.user = user;
    this.pass = pass;
    this.p = p;
    this.s = s;
    this.ascendent = ascendent;
  }

  public void run() {
    driver = new StartChromeDriver().getDriver(driver);
    chicken();
  }

  private void chicken() {
    driver.manage()
        .window()
        .setSize(s);
    driver.manage()
        .window()
        .setPosition(p);

    driver.get(url);
    try {
      getLogin();
      viewAds();
      log.info("End of views...");
    } catch (Exception e) {
      e.printStackTrace();
      driver.close();
    } finally {
      driver.close();
    }

  }

  private void viewAds() throws InterruptedException {
    boolean pending = false;
    driver.findElement(By.partialLinkText("View Ads"))
        .click();
    Utils.zoomTo(driver, 0.5);
    List<Integer> ads = getPositionsOfAvailableView();
    String original = driver.getWindowHandle();
    if (ascendent) {
      for (int i = 0; i < ads.size(); i++) {
        if (clickOnAd(original, ads.get(i)))
          continue;
      }
      log.info("End of for in order " + ascendent);
    } else {
      for (int i = ads.size() - 1; i > -1; i--) {
        if (!clickOnAd(original, ads.get(i))) {
          pending = true;
          continue;
        }
      }
      log.info("End of for in order " + ascendent);
    }
    if (pending) {
      driver.navigate().refresh();
      viewAds();
    }
  }

  private List<Integer> getPositionsOfAvailableView() {
    List<Integer> positions = new ArrayList<Integer>();
    List<WebElement> adsParent = driver.findElement(By.id("right"))
        .findElements(By.tagName("a"));
    log.info("the TOTAL of views elements are " + adsParent.size());
    int p = 0;
    for (WebElement ad : adsParent) {
      if (!ad.findElement(By.tagName("div"))
          .getAttribute("class")
          .contains("disabled_pbx")) {
        positions.add(p);
      }
      p++;
    }
    log.info("the size of CURRENT positions is " + positions.size() + " /n and the views to clicked are " + positions);
    return positions;
  }

  private boolean clickOnAd(String original, int i) {
    try {
      WebElement ad = driver.findElement(By.id("right"))
          .findElements(By.tagName("a"))
          .get(i);
      if (ad.findElement(By.tagName("div"))
          .getAttribute("class")
          .contains("disabled_pbx")) {
        log.info("add " + i + " is disabled");
        return true;
      }
      if (ad.findElement(By.className("hap_title"))
          .getText()
          .contains("goostreet")) {
        log.info("add " + i + " is an specific add invalid with title goostreet");
        return true;
      }
      Utils.scrollAndClick(ad, driver);
      ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
      driver.switchTo()
          .window(tabs.get(1));
      String message = driver.findElement(By.id("desc"))
          .getText();
      if (message.contains("Already")) {
        driver.close();
        driver.switchTo()
            .window(original);
        log.info("add " + i + " is already viewed");
        return true;
      } else {
        while (!(driver.findElement(By.id("desc"))
            .getText()
            .contains("Completed!")
            || driver.findElement(By.id("desc"))
                .getText()
                .contains("Invalid Transaction!"))) {
          Thread.sleep(5000);
        }
        log.info("add " + i + " " + driver.findElement(By.id("desc"))
            .getText());
        driver.close();
        driver.switchTo()
            .window(original);
        return true;
      }
    } catch (Exception e) {
      log.info("add " + i + " with exception " + e.getCause()
          .getMessage());
      return false;
    }
  }

  private void getLogin() {
    WebDriverWait wait = new WebDriverWait(driver, 60);
    driver.get("http://bitcofarm.com/login");
    driver.findElement(By.name("username"))
        .sendKeys(user);
    driver.findElement(By.name("password"))
        .sendKeys(pass);
    driver.findElement(By.className("g-recaptcha"))
        .click();
    Utils.bip(BEEP, WAITB);
    wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.partialLinkText("View Ads")));

  }

}
