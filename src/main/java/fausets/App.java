package fausets;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import foo.webdriver.StartChromeDriver;
import runnable.RunAll;

/**
 * Hello world!
 *
 */
public class App implements Runnable {
    int BEEP = 1500;
    int WAITB = 20;
    int MINUTES_TOWAIT = 5;

    WebDriver driver;
    String url;
    String key;
    Point p;
    Dimension s = RunAll.SIZE1;
    DesiredCapabilities capabilities = DesiredCapabilities.chrome();

    public App(String url, String key, Point p) {
        this.url = url;
        this.key = key;
        this.p = p;
    }

    public void run() {
        driver = new StartChromeDriver().getDriver(driver);
        huefaucet();
    }

    private void huefaucet() {
        driver.manage().window().setSize(s);
        driver.manage().window().setPosition(p);
        driver.get(url);
        try {
            if (getLogin()) {
                while(true) {
                    claim();
                    Thread.sleep(MINUTES_TOWAIT * 60000);
                }
            }
            Thread.sleep(MINUTES_TOWAIT * 60000);
            claim();
        } catch (Exception e) {
            e.printStackTrace();
            driver.close();
        } finally {
            driver.close();
        }
    }

    private void claim() {
        Utils.getCurrentTime();
        try {
            driver.findElement(By.id("button")).click();
            driver.findElement(By.className("btn")).click();
            Utils.bip(BEEP, WAITB);
            while (seeCaptcha());
            System.out.println("satochi recogido");
        } catch (Exception e) {
            System.out.println("Los satochis ya fueron recogidos");
        }
    }

    private boolean getLogin() {
        WebElement address = driver.findElement(By.name("address"));
        address.sendKeys(key);
        WebElement loginBtnE1 = driver.findElement(By.className("btn"));
        if (loginBtnE1.getText().toUpperCase().contains("LOGIN")) {
            loginBtnE1.click();
        } else
            return false;
        return true;
    }

    private boolean seeCaptcha() {
        try {
            driver.findElement(By.id("adcopy-instr"));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
