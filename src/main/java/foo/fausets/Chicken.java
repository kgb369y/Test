package foo.fausets;

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
    List<WebElement> ads = getPositionsOfAvailableView();

    String original = driver.getWindowHandle();
    if (ascendent) {
      pending = clik(0, ads.size() - 1, original, ads);
    } else {
      pending = clik(ads.size() - 1, 0, original, ads);
    }
    log.info("End of for in order " + ascendent);
    if (pending) {
      driver.navigate()
          .refresh();
      viewAds();
    }
  }

  private boolean clik(int start, int end, String original, List<WebElement> ads) {
    boolean sucess = false;
    for (int i = start; i == end;) {
      if (!clickOnAd(original, ads.get(i))) {
        sucess = true;
      }
      i = start == 0 ? i + 1 : i - 1;
      continue;
    }
    return sucess;
  }

  private List<WebElement> getPositionsOfAvailableView() {
    List<WebElement> adsParentAvailables = driver.findElements(By.className("hap"));
    adsParentAvailables.removeAll(driver.findElements(By.className("disabled_pbx")));
    adsParentAvailables.removeIf(element -> element.findElement(By.className("hap_title"))
        .getText()
        .contains("goostreet"));
    return adsParentAvailables;
  }

  private boolean clickOnAd(String original, WebElement ad) {
    try {
      Utils.scrollAndClick(ad, driver);
      changeOfWindowAndWaitComplete(original);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean changeOfWindowAndWaitComplete(String original) throws InterruptedException {
    Utils.goNextWindow(driver);
    String message = driver.findElement(By.id("desc"))
        .getText();
    if (message.contains("Already")) {
      Utils.backWindow(driver, original);
      return true;
    } else {
      while (!isCompleted()) {
        Thread.sleep(5000);
      }
      Utils.backWindow(driver, original);
      return true;
    }
  }

  private boolean isCompleted() {
    String headerTxt = driver.findElement(By.id("desc"))
        .getText();
    if (headerTxt.contains("Completed!") || headerTxt.contains("Invalid Transaction!"))
      return true;
    else
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
