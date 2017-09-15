package foo.fausets;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.Point;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import foo.runnable.RunAll;
import foo.webdriver.StartChromeDriver;

public class AppBF implements Runnable {

    int BEEP = 1500;
    int WAITB = 20;
    int MINUTES_TOWAIT = 10;

    WebDriver driver;

    String url;
    String user;
    String pass;

    Point p;
    Dimension s = RunAll.SIZE2;

    public AppBF(String url, String user, String pass, Point p) {
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.p = p;
    }

    public void run() {
        driver = new StartChromeDriver().getDriver(driver);
        bitfun();
    }

    private void bitfun() {
        driver.manage().window().setSize(s);
        driver.manage().window().setPosition(p);
        driver.get(url);
        try {
            getLogin();
            while (true) {
                claim();
                Thread.sleep(MINUTES_TOWAIT * 60000);
            }

        } catch (Exception e) {
            System.out.println("***E");
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
        WebElement button = driver.findElement(By.cssSelector("button.btn.btn-primary.btn-lg.claimButton"));
        System.out.println(button.getText());
        Utils.scrollAndClick(button, driver);
        backToOriginalPage();
        button = driver.findElement(By.cssSelector("button.btn.btn-primary.btn-lg.claimButton"));
        Utils.scrollAndClick(button, driver);
        driver.switchTo().frame(0);

        WebElement check = driver.findElement(By.id("recaptcha-anchor"));
        Utils.scrollAndClick(check, driver);

        Utils.bip(BEEP, WAITB);
        driver.switchTo().defaultContent();
        driver.switchTo().frame(10);
        String rcIS = driver.findElements(By.tagName("div")).get(1).getAttribute("id");
        System.out.println(rcIS);// rc-imageselect
        // driver.findElement(By.id("recaptcha-verify-button")).click();
        whenTheElementLeftDom();
        System.out.println("**l");
        driver.switchTo().defaultContent();
        driver.switchTo().frame(0);
        driver.switchTo().defaultContent();
        WebElement footer = driver.findElements(By.className("modal-footer")).get(0);
        WebElement claimFinal = footer.findElements(By.tagName("button")).get(1);
        System.out.println(claimFinal.getText());// Claim
        claimFinal.click();
        Thread.sleep(2000);
        WebElement close = driver.findElements(By.cssSelector("button.btn.btn-default")).get(1);
        System.out.println(close.getText());// 602
        Utils.scrollAndClick(close, driver);
    }

    private void backToOriginalPage() throws InterruptedException {
        ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
        if (tabs2.size() == 2) {
            try {
                driver.findElement(By.cssSelector("button.btn.btn-primary.btn-lg.claimButton"));
            } catch (UnhandledAlertException e) {
                try {
                    Alert alert = driver.switchTo().alert();
                    String alertText = alert.getText();
                    System.out.println("Alert data: " + alertText);
                    alert.accept();
                } catch (NoAlertPresentException e1) {
                    e1.printStackTrace();
                }
            }
            driver.close();
            driver.switchTo().window(tabs2.get(1));
            System.out.println(driver.findElement(By.cssSelector("button.btn.btn-primary.btn-lg.claimButton")).getText());
            Thread.sleep(3000);
        }
    }

    private void whenTheElementLeftDom() {
        while (true) {
            System.out.println("iteration");
            try {
                String verifyButton = driver.findElement(By.id("recaptcha-verify-button")).getAttribute("disabled");
                if (verifyButton.contains("true"))
                    return;
            } catch (NoSuchElementException e) {
                return;
            }
        }
    }
}
