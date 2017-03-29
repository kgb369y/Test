package foo.fausets;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Hello world!
 *
 */
public class App implements Runnable {
    int BEEP = 1500;
    int WAITB = 20;
    int MINUTES_TOWAIT = 5;

    WebDriver driver;
    Wait<WebDriver> wait;
    String url;
    String key;

    public App(String url, String key) {
        this.url = url;
        this.key = key;
    }

    public void run() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        huefaucet();
    }

    private void huefaucet() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 30);
        driver.get(url);
        try {
            if (getLogin()) {
                claim();
                Thread.sleep(MINUTES_TOWAIT * 60000);
                claim();
                Thread.sleep(MINUTES_TOWAIT * 60000);
                claim();
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
        driver.findElement(By.id("button")).click();
        driver.findElement(By.className("btn")).click();
        if (!passCatpcha()) {
            claim();
            System.out.println("it++");
        }
    }

    private boolean passCatpcha() {
        if (seeCaptcha()) {
            Utils.bip(BEEP, WAITB);
        }
        try {
            driver.findElement(By.id("button")).click();
        } catch (NoSuchSessionException e) {
            return false;
        }
        return true;
    }

    private boolean getLogin() {
        WebElement address = driver.findElement(By.name("address"));
        address.sendKeys(key);
        WebElement loginBtnE1 = driver.findElement(By.className("btn"));
        if (loginBtnE1.getText().toUpperCase().contains("LOGIN")) {
            loginBtnE1.click();
            if (!passCatpcha()) {
                loginBtnE1.click();
                passCatpcha();
            }
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
