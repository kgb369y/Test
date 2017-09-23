package foo.accounts;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Wait;

import foo.fausets.Utils;
import foo.webdriver.StartChromeDriver;

public class CreateAccount implements Runnable {
    int BEEP = 1500;
    int WAITB = 20;

    WebDriver driver;
    Wait<WebDriver> wait;
    String url;

    String hotmail = "@hotmail.com";
    String dir;
    String pass;
    String consecutivo;
    String mail;
    String phone;
    String month = "January";
    String day = "18";
    String year = "1999";
    String g = "Female";
    String country = "Colombia";
    DesiredCapabilities capabilities = DesiredCapabilities.chrome();

    public CreateAccount(String url, String mail, String phone, String consecutivo) {
        this.url = url;
        this.mail = mail;
        this.phone = phone;
        this.consecutivo = consecutivo;
        dir = "grado11.kapo01." + consecutivo;
        pass = dir + "123.";
    }

    public void run() {
        driver = new StartChromeDriver().getDriver(driver);
        createMail();
    }

    private void createMail() {
        Utils.setSizeAndPosition(driver, new Dimension(683, 728),new Point(684, 0));
        driver.get(url);
        try {
            for (int i = 0; i < 6; i++) {
                createMailHotmail();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void createMailHotmail() throws InterruptedException {
        driver.findElement(By.id("FirstName")).sendKeys("grado");
        driver.findElement(By.id("LastName")).sendKeys("11");
        driver.findElement(By.id("MemberName")).sendKeys(dir + hotmail);
        ifIhadErrorNextConsecutive();
        pass = pass.replace(".", "");
        driver.findElement(By.id("Password")).click();
        driver.findElement(By.id("Password")).sendKeys(pass);
        driver.findElement(By.id("RetypePassword")).click();
        driver.findElement(By.id("RetypePassword")).sendKeys(pass);
        driver.findElement(By.id("Country")).sendKeys(country);
        driver.findElement(By.id("BirthMonth")).sendKeys(month);
        driver.findElement(By.id("BirthDay")).sendKeys(day);
        driver.findElement(By.id("BirthYear")).sendKeys(year);
        driver.findElement(By.id("Gender")).sendKeys(g);
        driver.findElement(By.id("PhoneCountry")).sendKeys(country);
        driver.findElement(By.id("PhoneNumber")).sendKeys(phone);
        driver.findElement(By.id("iAltEmail")).sendKeys(mail);
        if (!driver.findElement(By.id("iOptinEmail")).isSelected())
            driver.findElement(By.id("iOptinEmail")).click();
        WebElement element = driver.findElement(By.className("spHipNoClear"));
        Utils.scrollAndClick(element, driver);
        Utils.bip(BEEP, WAITB);
        driver.findElement(By.id("CredentialsAction")).click();
        Utils.accountIsLogged(driver);
        getNextConsecutive();
        Utils.logoutMail(driver, wait, url);
    }

    private void ifIhadErrorNextConsecutive() throws InterruptedException {
        try {
            try {
                driver.findElement(By.id("Password")).sendKeys("");
            } catch (Exception e) {
            }
            Thread.sleep(3000);
            if (!driver.findElement(By.id("MemberNameError")).getText().isEmpty()) {
                getNextConsecutive();
                driver.navigate().refresh();
                createMailHotmail();
            }
        } catch (Exception e) {
            return;
        }
    }

    private void getNextConsecutive() {
        int next = Integer.parseInt(consecutivo) + 1;
        consecutivo = (next < 10) ? "0" + next : String.valueOf(next);
        dir = "grado11.kapo01." + consecutivo;
        pass = dir + "123.";
    }

    public void createMailGmail() throws InterruptedException {
        driver.findElement(By.id("FirstName")).sendKeys("grado");
        driver.findElement(By.id("LastName")).sendKeys("11");
        driver.findElement(By.id("GmailAddress")).sendKeys(dir);
        driver.findElement(By.id("Passwd")).sendKeys(pass);
        driver.findElement(By.id("PasswdAgain")).sendKeys(pass);
        driver.findElements(By.className("jfk-select")).get(0).sendKeys(month);
        driver.findElement(By.id("BirthDay")).sendKeys(day);
        driver.findElement(By.id("BirthYear")).sendKeys(year);
        driver.findElements(By.className("jfk-select")).get(1).sendKeys(g);
        WebElement ph = driver.findElement(By.id("RecoveryPhoneNumber"));
        ph.clear();
        ph.sendKeys(phone);
        driver.findElement(By.id("RecoveryEmailAddress")).sendKeys(mail);
        driver.findElements(By.className("jfk-select")).get(2).sendKeys(country);
        submitUntilSeeExpectedElement(By.id("submitbutton"), By.id("tos-scroll-button"));
        submitUntilSeeExpectedElement(By.id("tos-scroll-button"), By.id("iagreebutton"));
        driver.findElement(By.id("iagreebutton")).click();
        driver.findElement(By.id("next-button")).click();// By.name("VerifyPhone")
        driver.findElement(By.id("submitbutton")).click();
        driver.findElement(By.id("Passwd")).sendKeys(pass);
        driver.findElement(By.id("signIn")).click();
        System.out.println("ok");

    }

    private void submitUntilSeeExpectedElement(By submit, By scrollTAndC) throws InterruptedException {
        int it = 0;
        while (it < 8) {
            it++;
            try {
                driver.findElement(submit).click();
                if (driver.findElement(scrollTAndC).isDisplayed())
                    return;
            } catch (Exception e) {
                Thread.sleep(1000);
            }
        }
    }

}
