package foo.fausets;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;

public class Utils {

  public static void bip(int beep, int waitb) {

    byte[] buf = new byte[1];
    AudioFormat af = new AudioFormat((float) 44100, 8, 1, true, false);
    SourceDataLine sdl;
    try {
      sdl = AudioSystem.getSourceDataLine(af);
      sdl.open();
      sdl.start();
      for (int i = 0; i < beep * (float) 44100 / 1000; i++) {
        double angle = i / ((float) 44100 / 440) * 2.0 * Math.PI;
        buf[0] = (byte) (Math.sin(angle) * 100);
        sdl.write(buf, 0, 1);
      }
      sdl.drain();
      sdl.stop();
      Thread.sleep(waitb * 1000);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public static void getCurrentTime() {
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    Date date = new Date();
    System.out.println(dateFormat.format(date));

  }

  public static String getDate() {
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    Date date = new Date();
    return dateFormat.format(date);
  }

  public static void scrollAndClick(WebElement element, WebDriver driver) {
    int elementPositionX = element.getLocation()
        .getX();
    int elementPositionY = element.getLocation()
        .getY();
    JavascriptExecutor executor = (JavascriptExecutor) driver;
    String js = String.format("window.scroll(" + elementPositionX + "," + elementPositionY + ")");
    executor.executeScript(js);
    executor.executeScript("arguments[0].click();", element);
  }

  public static void scrollAndClickWBuild(By id, WebDriver driver) {
    WebElement element = driver.findElement(id);
    Actions build = new Actions(driver);
    build.moveToElement(element)
        .build()
        .perform();// 402
    driver.findElement(id)
        .click();
  }

  public static boolean accountIsLogged(WebDriver driver) throws InterruptedException {
    int it = 0;
    while (true) {
      it++;
      try {
        if (!driver.findElement(By.id("greeting-message"))
            .getText()
            .isEmpty())
          return true;
        if (driver.findElement(By.cssSelector("span.folderHeaderLabel.ms-font-xl.ms-fwt-r"))
            .getText()
            .contains("Inbox"))
          return true;
      } catch (Exception e) {
        if (it > 5)
          return false;
        Thread.sleep(3000);
      }
    }
  }

  public static void closeAllWindowsAndBack(WebDriver driver, String original) {
    ArrayList<String> h = new ArrayList<String>(driver.getWindowHandles());
    while (h.size() > 1) {
      driver.switchTo()
          .window(h.get(h.size() - 1));
      driver.close();
      h.remove(h.size() - 1);
    }
    driver.switchTo()
        .window(original);
  }

  public static void logoutMail(WebDriver driver, Wait<WebDriver> wait, String url) throws InterruptedException {
    driver.findElement(By.className("msame_Header_picframe"))
        .click();
    By logout = By.partialLinkText("Sign out");
    wait.until(ExpectedConditions.visibilityOfElementLocated(logout));
    driver.findElement(logout)
        .click();
    Thread.sleep(8000);
    driver.get(url);
    Thread.sleep(3000);
  }

  public static void changeTabAndCloseNextOthers(WebDriver driver, String where) {
    ArrayList<String> h = new ArrayList<String>(driver.getWindowHandles());
    while (h.size() > 0) {
      driver.switchTo()
          .window(h.get(h.size() - 1));
      if (driver.getCurrentUrl()
          .contains(where)) {
        return;
      }
      driver.close();
      h.remove(h.size() - 1);
    }
  }

  public static void openNewTabAndSwitch(WebDriver driver, String partialLink)
      throws AWTException, InterruptedException {
    String currently = driver.getWindowHandle();
    WebElement link;
    if (driver.getCurrentUrl()
        .contains("coinbase.com/users/verify")) {
      driver.findElement(By.id("main_nav_btn"))
          .click();
      link = driver.findElement(By.partialLinkText("Help"));
    } else {
      link = driver.findElement(By.partialLinkText(partialLink));
    }

    Actions a = new Actions(driver);
    a.keyDown(Keys.CONTROL)
        .click(link)
        .keyUp(Keys.CONTROL)
        .build()
        .perform();
    a.keyDown(Keys.CONTROL)
        .sendKeys(Keys.TAB)
        .keyUp(Keys.CONTROL)
        .build()
        .perform();
    Robot rb = new Robot();
    rb.keyPress(KeyEvent.VK_CONTROL);
    rb.keyPress(KeyEvent.VK_TAB);
    rb.keyRelease(KeyEvent.VK_CONTROL);
    rb.keyRelease(KeyEvent.VK_TAB);
    Thread.sleep(8000);
    ArrayList<String> hands = new ArrayList<String>(driver.getWindowHandles());
    int intCurrently = hands.indexOf(currently);
    String next = hands.get(intCurrently + 1);
    driver.switchTo()
        .window(next);
  }

  public static boolean presenceOfElement(WebDriver driver, By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (Exception e) {
    }
    return false;
  }

  public static void zoomTo(WebDriver driver, double i) {
    try {
      JavascriptExecutor js = (JavascriptExecutor) driver;
      js.executeScript("document.body.style.zoom='" + i + "'");
      Thread.sleep(2000);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
