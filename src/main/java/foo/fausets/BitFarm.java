package foo.fausets;

import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

import foo.runnable.RunAll;
import foo.webdriver.StartChromeDriver;

public class BitFarm implements Runnable {
    int TIMETOHAVEPAGEOPENS = 60;
    int BEEP = 1500;
    int WAITB = 20;

    WebDriver driver;
    String url;
    String key;
    Dimension s = RunAll.SIZE;
    Point p;

    public BitFarm(String url, String key, Point p) {
        this.url = url;
        this.key = key;
        this.p = p;
    }

    public void run() {
        driver = new StartChromeDriver().getDriver(driver);
        bitFarm();
    }

    private void bitFarm() {
        Utils.setSizeAndPosition(driver, s, p);
        driver.get(url);
        try {
            getLogin();
            Thread.sleep(TIMETOHAVEPAGEOPENS * 60000);
        } catch (Exception e) {
            e.printStackTrace();
            driver.close();
        } finally {
            driver.close();
        }

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
        Utils.bip(BEEP, WAITB);
    }
}