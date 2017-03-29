package foo.runnable;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Wait;

import foo.fausets.Utils;
import foo.webdriver.StartChromeDriver;

public class CreateFb implements Runnable {
    int BEEP = 1500;
    int WAITB = 20;

    WebDriver driver;
    Wait<WebDriver> wait;
    String url;
    String name = "Juan";
    String lastName = "Perez";

    String hotmail = "@hotmail.com";
    String dir;
    String pass;
    String consecutivo;
    String phone;
    String month = "January";
    String day = "18";
    String year = "1999";

    DesiredCapabilities capabilities = DesiredCapabilities.chrome();

    public CreateFb(String url, String phone, String consecutivo) {
        this.url = url;
        this.consecutivo = consecutivo;
        this.phone = phone;
        dir = "grado11.kapo01." + consecutivo;
        pass = dir + "123.";
    }

    public void run() {
        driver = new StartChromeDriver().getDriver(driver);
        createMail();
    }

    private void createMail() {
        driver.manage().window().setSize(new Dimension(683, 728));
        driver.manage().window().setPosition(new Point(684, 0));
        driver.get(url);
        try {
            for (int i = 0; i < 6; i++) {
                createFb();
                if (isLogued())
                    logout();
                getNextConsecutive();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void logout() throws InterruptedException {
        driver.findElement(By.id("pageLoginAnchor")).click();
        Thread.sleep(3000);
        driver.findElement(By.partialLinkText("Log Out")).click();
    }

    private boolean isLogued() {
        try {
            WebElement nav = driver.findElement(By.id("userNav"));
            if (!nav.findElement(By.tagName("a")).getAttribute("title").isEmpty())
                return true;
        } catch (Exception e) {
        }
        return false;
    }

    private void createFb() throws InterruptedException {
        driver.findElement(By.id("u_0_1")).sendKeys(name);
        driver.findElement(By.id("u_0_3")).sendKeys(lastName);
        driver.findElement(By.id("u_0_6")).sendKeys(dir + hotmail);
        driver.findElement(By.id("u_0_9")).sendKeys(dir + hotmail);
        driver.findElement(By.id("u_0_d")).sendKeys(pass);
        driver.findElement(By.id("month")).sendKeys(month);
        driver.findElement(By.id("day")).sendKeys(day);
        driver.findElement(By.id("year")).sendKeys(year);
        driver.findElement(By.id("u_0_g")).click();
        driver.findElement(By.id("u_0_l")).click();
        Thread.sleep(2000);
        if (Utils.presenceOfElement(driver, By.partialLinkText("Enter a mobile number"))) {
            driver.findElement(By.partialLinkText("Enter a mobile number")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("country")).sendKeys("Colombia");
            driver.findElement(By.name("contact_point")).sendKeys(phone);
            WebElement sectionButtons = driver.findElement(By.className("_14"));
            WebElement ok = sectionButtons.findElement(By.tagName("button"));
            Utils.scrollAndClick(ok, driver);
            Utils.bip(BEEP, WAITB);
            while(true) {
                if (Utils.presenceOfElement(driver, By.name("activate_sms"))) {
                    Utils.scrollAndClick(driver.findElement(By.name("activate_sms")), driver);
                    driver.findElement(By.id("u_7_3")).sendKeys("Only Me");
                    sectionButtons = driver.findElement(By.className("_14"));
                    ok = sectionButtons.findElement(By.tagName("button"));
                    Utils.scrollAndClick(ok, driver);
                    break;
                }
                Thread.sleep(1000);
            }
        }
        Utils.scrollAndClickWBuild(By.partialLinkText("Next"), driver);
        Thread.sleep(8000);
        driver.findElement(By.partialLinkText("Skip step")).click();
        Thread.sleep(3000);
        System.out.println("sss");
    }

    private void getNextConsecutive() {
        int next = Integer.parseInt(consecutivo) + 1;
        consecutivo = (next < 10) ? "0" + next : String.valueOf(next);
        dir = "grado11.kapo01." + consecutivo;
        pass = dir + "123.";
    }

}
