package fausets;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AppBF implements Runnable {

    int BEEP = 1500;
    int WAITB = 20;
    int MINUTES_TOWAIT = 10;

    WebDriver driver;
    Wait<WebDriver> wait;

    String url;
    String user;
    String pass;

    public AppBF(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    public void run() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        bitfun();
    }

    private void bitfun() {

        ChromeOptions chrome_options = new ChromeOptions();
        chrome_options.addArguments("--incognito");
        DesiredCapabilities dc = DesiredCapabilities.chrome();
        dc.setCapability(ChromeOptions.CAPABILITY, chrome_options);
        driver = new ChromeDriver(dc);
        wait = new WebDriverWait(driver, 30);
        driver.get(url);
        try {
            getLogin();
            while (true) {
                claim();
                Thread.sleep(MINUTES_TOWAIT * 60000);
            }

        } catch (Exception e) {
            e.printStackTrace();
            driver.close();
        } finally {
            driver.close();
        }
    }

    private void getLogin() throws InterruptedException {
        List<WebElement> botons = driver.findElements(By.tagName("button"));
        for (WebElement boton : botons) {
            if (boton.getAttribute("data-target").contains("SignInModal")) {
                WebElement elementClicks = boton.findElement(By.xpath("../.."));
                elementClicks.click();
                break;
            }
        }
        Thread.sleep(1000);
        WebElement userField = driver.findElement(By.id("SignInEmailInput"));
        userField.sendKeys(user);
        WebElement password = driver.findElement(By.id("SignInPasswordInput"));
        password.sendKeys(pass);
        WebElement modalFootetLogin = driver.findElement(By.id("SignInModal"))
                .findElement(By.className("modal-footer"));
        WebElement bottonLogin = modalFootetLogin.findElements(By.tagName("button")).get(1);
        bottonLogin.click();
        Thread.sleep(3000);
    }

    private void claim() throws InterruptedException {
        Utils.getCurrentTime();
        WebElement button = driver.findElement(By.className("col-sm-5")).findElement(By.tagName("button"));
        if (button.getText().contains("Claim now")) {
            button.click();
            Set<String> hands = driver.getWindowHandles();
            if (hands.size() > 1) {
                for (String string : hands) {
                    driver.switchTo().window(string);
                    if (driver.getCurrentUrl().contains(url))
                        break;
                }
            }
        }
        WebElement check;
        try {
            check = driver.findElement(By.id("recaptcha-anchor"));
        } catch (NoSuchElementException e) {
            driver.findElement(By.className("col-sm-5")).findElement(By.tagName("button")).click();
            check = driver.findElement(By.id("recaptcha-anchor"));
        }
        check.click();

        /*
         * if (!passCatpcha()) { claim(); System.out.println("it++"); }
         */
    }

    /* Method under construction */
    private boolean passCatpcha() {
        if (seeCaptcha()) {
            Utils.bip(BEEP, WAITB);
        }
        /*
         * try { driver.findElement(By.id("button")).click(); } catch
         * (NoSuchSessionException e) { return false; }
         */
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