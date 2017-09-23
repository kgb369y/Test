package foo.runnable;

import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import foo.fausets.Utils;
import foo.webdriver.StartChromeDriver;

public class PrintWallIDs implements Runnable {
    int BEEP = 1500;
    int WAITB = 20;

    WebDriver driver;
    Wait<WebDriver> wait;
    String url;
    String hotmail = "@hotmail.com";
    String dir;
    String pass;
    String consecutivo;
    HashMap<String, String> walls = new HashMap<String, String>();

    DesiredCapabilities capabilities = DesiredCapabilities.chrome();

    public PrintWallIDs(String url, String consecutivo) {
        this.url = url;
        this.consecutivo = consecutivo;
        dir = "grado11.kapo01." + consecutivo;
        pass = dir + "123.";
    }

    public void run() {
        driver = new StartChromeDriver().getDriver(driver);
        wait = new WebDriverWait(driver, 10);
        try {
            printWallets();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printWallets() {
        Utils.setSizeAndPosition(driver, new Dimension(683, 728), new Point(684, 0));
        try {
            System.out.println("******************************************************************");
            for (int i = 0; i < 3; i++) {
                loginInCoinB();
                Thread.sleep(1000);
                getWalls();
                logoutCoinB();
                getNextConsecutive();
            }
            System.out.println("******************************************************************");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void getWalls() {
        driver.get(url + "accounts");
        
        driver.findElements(By.className("muted")).get(2).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("code")));
        String key = driver.findElement(By.tagName("code")).getText();
        walls.put(dir, key);
        System.out.println(dir + hotmail + " | " + key); 
        driver.navigate().refresh();
    }

    private void loginInCoinB() {
        driver.get(url + "signin");
        driver.findElement(By.id("email")).sendKeys(dir + hotmail);
        driver.findElement(By.id("password")).sendKeys(pass);
        driver.findElement(By.id("signin_button")).click();
    }

    private void logoutCoinB() {
        driver.get(url + "signout");
    }

    private void getNextConsecutive() {
        int next = Integer.parseInt(consecutivo) + 1;
        consecutivo = (next < 10) ? "0" + next : String.valueOf(next);
        dir = "grado11.kapo01." + consecutivo;
        pass = dir + "123.";
    }

}
