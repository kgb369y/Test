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
    Utils.setSizeAndPosition(driver, s, p);
    driver.get(url);
    try {
      getLogin();
      viewAds();
      log.info("End of views...");
    } catch (Exception e) {
      log.error("Err ", e);
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
    List<WebElement> ads = getPositionsOfAvailableView();

    String original = driver.getWindowHandle();
    if (ascendent) {
      pending = clik(0, ads.size() - 1, original, ads);
    } else {
      pending = clik(ads.size() - 1, 0, original, ads);
    }
    
    if (pending) {
      driver.navigate()
          .refresh();
      viewAds();
    }
    log.info("End of for in order " + ascendent);
  }

  private boolean clik(int start, int end, String original, List<WebElement> ads) {
    List<Boolean> fails = new ArrayList<Boolean>();
    int i = start;
    while (i != end) {
      if (!clickOnAd(original, ads.get(i), i)) {
        fails.add(true);
      }
      i = start == 0 ? i + 1 : i - 1;
      continue;
    }
    return fails.size() == 0 ? true : false;
  }

  private List<WebElement> getPositionsOfAvailableView() {
    List<WebElement> adsParentAvailables = driver.findElements(By.className("hap"));
    adsParentAvailables.removeAll(driver.findElements(By.className("disabled_pbx")));
    adsParentAvailables.removeIf(element -> element.findElement(By.className("hap_title"))
        .getText()
        .contains("goostreet"));
    getEstimatedTime(adsParentAvailables.size());
    return adsParentAvailables;
  }

  private void getEstimatedTime(int size) {
    String unity = "seconds";
    int time = size * 35;
    if (time > 60) {
      time = time / 60;
      unity = "minutes";
    } 
    if (time > 60) {
      time = time / 60;
      unity = "hours";
    }
    if (time > 24) {
      time = time / 24;
      unity = "days";
    }
    log.info("Pending stimated time is " + time + unity);
  }

  private boolean clickOnAd(String original, WebElement ad, int i) {
    try {
      Utils.scrollAndClick(ad, driver);
      return changeOfWindowAndWaitComplete(original, i);
    } catch (Exception e) {
      log.error("Err view " + i, e);
      Utils.changeTabAndCloseNextOthers(driver, original);
      return false;
    }
  }

  private boolean changeOfWindowAndWaitComplete(String original, int i) throws InterruptedException {
    Utils.goNextWindow(driver);
    String message = driver.findElement(By.id("desc"))
        .getText();
    if (message.contains("Already")) {
      Utils.backWindow(driver, original);
      log.info("The ad " + i + " is " + message);
      return true;
    } else {
      while (!isCompleted(i)) {
        Thread.sleep(5000);
      }
      Utils.backWindow(driver, original);
      return true;
    }
  }

  private boolean isCompleted(int i) {
    String headerTxt = driver.findElement(By.id("desc"))
        .getText();
    if (headerTxt.contains("Completed!") || headerTxt.contains("Invalid Transaction!")) {
      log.info("The ad " + i + " is " + headerTxt);
      return true;
    } else
      return false;
  }

  private void getLogin() {
    WebDriverWait wait = new WebDriverWait(driver, 120);
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
