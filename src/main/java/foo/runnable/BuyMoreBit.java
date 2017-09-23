package foo.runnable;

import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

import foo.fausets.Utils;
import foo.webdriver.StartChromeDriver;

public class BuyMoreBit implements Runnable {
    int BEEP = 1500;
    int WAITB = 20;

    WebDriver driver;
    String bitFarm;
    String key;
    
    public BuyMoreBit(String bitFarm, String key) {
        this.bitFarm = bitFarm;
        this.key = key;
    }

    public void run() {
        driver = new StartChromeDriver().getDriver(driver);
        bitFarm();
    }

    private void bitFarm() {
        Utils.setSizeAndPosition(driver, new Dimension(683, 728), new Point(684, 0));
        driver.get(bitFarm);
        try {
            getLogin();
            while(true) {
                buyMore();
                Thread.sleep(3600000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.close();
        }
    }

    private void buyMore() {
        driver.findElements(By.tagName("button")).get(4).click();
    }

    private void getLogin() throws InterruptedException {
        Thread.sleep(4000);
        driver.findElement(By.name("address")).sendKeys(key);
        driver.findElement(By.id("btnSubmit")).click();
        Set<String> handles = driver.getWindowHandles();
        if (handles.size() > 1) {
            Iterator<String> it = handles.iterator();
            String firstP = it.next();
            String secondP = it.next();
            driver.switchTo().window(secondP).close();
            driver.switchTo().window(firstP);
        }
        if (!Utils.presenceOfElement(driver, By.id("logout")))
            Utils.bip(BEEP, WAITB);
    }

}
