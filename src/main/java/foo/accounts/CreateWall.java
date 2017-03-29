package foo.accounts;

import java.awt.AWTException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import foo.fausets.Utils;
import foo.runnable.CustomException;
import foo.webdriver.StartChromeDriver;

public class CreateWall implements Runnable {
    int BEEP = 1500;
    int WAITB = 20;

    WebDriver driver;
    Wait<WebDriver> wait;
    String url;
    String urlMail;
    String name = "Juan";
    String lastName = "Perez";
    String urlOutlook;
    String hotmail = "@hotmail.com";
    String dir;
    String pass;
    String consecutivo;
    String idioma;

    DesiredCapabilities capabilities = DesiredCapabilities.chrome();

    public CreateWall(String url, String urlMail, String urlOutlook, String consecutivo) {
        this.url = url + "join/58d9cd3a77cc4a00adec6b52";
        this.urlMail = urlMail;
        this.urlOutlook = urlOutlook;
        this.consecutivo = consecutivo;
        dir = "grado11.kapo01." + consecutivo;
        pass = dir + "123.";
    }

    public void run() {
        driver = new StartChromeDriver().getDriver(driver);
        wait = new WebDriverWait(driver, 10);
        try {
            createWallet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createWallet() throws AWTException {
        driver.manage().window().setSize(new Dimension(683, 728));
        driver.manage().window().setPosition(new Point(684, 0));
        driver.get(url);
        String originalTab = driver.getWindowHandle();
        try {
            for (int i = 0; i < 6; i++) {
                try {
                    if (createw(dir, pass)) {
                        Utils.openNewTabAndSwitch(driver, "e");
                        loginInMail(dir, pass);
                        openMail();
                        validateAccountAndClose(originalTab);
                        logoutCoinB();
                        getNextConsecutive();
                        driver.get(url);
                        continue;
                    } else {
                        Utils.openNewTabAndSwitch(driver, "e");
                        loginInMail(dir, pass);
                        openMail();
                        validateAccountAndClose(originalTab);
                        logoutCoinB();
                        getNextConsecutive();
                        driver.get(url);
                        continue;
                    }
                } catch (CustomException e) {
                    Utils.closeAllWindowsAndBack(driver,new ArrayList<String>(driver.getWindowHandles()).get(0));
                    getNextConsecutive();
                    driver.get(url);
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean isTheFistTime() {
        try {
            boolean a = driver.findElement(By.tagName("h1")).getText().contains("Hi grado");
            List<WebElement> h2 = driver.findElements(By.tagName("h2"));
            boolean b = h2.get(0).getText().contains("Welcome to Outlook.");
            boolean c = h2.get(1).getText().contains("Your email, reimagined.");
            if (a && b && c)
                return true;
        } catch (Exception e) {
        }
        return false;
    }

    private void getNextConsecutive() {
        int next = Integer.parseInt(consecutivo) + 1;
        consecutivo = (next < 10) ? "0" + next : String.valueOf(next);
        dir = "grado11.kapo01." + consecutivo;
        pass = dir + "123.";
    }

    private void validateAccountAndClose(String original) throws InterruptedException {
        By mailFounded;
        if (idioma.equals("s"))
            mailFounded = By.partialLinkText("Verificar dirección de correo electr");
        else
            mailFounded = By.partialLinkText("Verify Email Address");
        wait.until(ExpectedConditions.visibilityOfElementLocated(mailFounded));
        Utils.scrollAndClick(driver.findElement(mailFounded), driver);
        Utils.changeTabAndCloseNextOthers(driver, urlOutlook);
        driver.get(urlMail);
        Thread.sleep(5000);
        Utils.logoutMail(driver, wait, urlMail);
        Utils.closeAllWindowsAndBack(driver, original);
    }

    private void openMail() throws InterruptedException, CustomException {
        if (isTheFistTime()) {
            closeWelcome();
        }
        int it = 0;
        while (it < 6) {
            it++;
            List<WebElement> mails = driver
                    .findElements(By.cssSelector("span.lvHighlightAllClass.lvHighlightSubjectClass"));
            for (WebElement msj : mails) {
                if (msj.getText().contains("Por favor, verifique")) {
                    msj.click();
                    idioma = "s";
                    driver.get("https://signup.live.com/");
                    Utils.logoutMail(driver, wait, url);
                    return;
                }
                if (msj.getText().contains("Please Verify Your")) {
                    msj.click();
                    idioma = "e";
                    driver.get("https://signup.live.com/");
                    Utils.logoutMail(driver, wait, url);
                    return;
                }
            }
            Thread.sleep(10000);
            driver.navigate().refresh();
        }
        driver.get("https://signup.live.com/");
        Utils.logoutMail(driver, wait, url);
        throw new CustomException("Mail not found");
    }

    private void closeWelcome() {
        for (int i = 0; i < 5; i++)
            driver.findElement(By.className("nextButton")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("goButton")));
        driver.findElement(By.className("goButton")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("_ariaId_28.folder")));
        System.out.println("ok");
    }

    private boolean loginInMail(String mail, String pass) throws InterruptedException {
        driver.get(urlMail);
        By fieldEmail = By.cssSelector("div.has-focus.placeholder");
        WebElement user = driver.findElement(fieldEmail);
        wait.until(ExpectedConditions.elementToBeClickable(fieldEmail));
        user.click();
        Actions actions = new Actions(driver);
        actions.moveToElement(user);
        actions.click();
        actions.sendKeys(mail + hotmail);
        actions.build().perform();
        driver.findElement(By.id("idSIButton9")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("i0118")));
        driver.findElement(By.id("i0118")).sendKeys(pass.replace(".", ""));
        driver.findElement(By.id("idSIButton9")).click();
        if (Utils.accountIsLogged(driver)) {
            driver.get(urlOutlook);
            return true;
        }
        return false;
    }

    public boolean createw(String mail, String pass) throws InterruptedException {
        try {

            driver.findElement(By.id("user_first_name")).sendKeys(name);
            driver.findElement(By.id("user_last_name")).sendKeys(lastName);
            driver.findElement(By.id("user_email")).sendKeys(mail + hotmail);
            driver.findElement(By.id("user_password")).sendKeys(pass);
            while (!driver.findElement(By.id("user_accepted_user_agreement")).isSelected()) {
                driver.findElement(By.id("user_accepted_user_agreement")).click();
            }
            driver.findElement(By.name("commit")).click();
            if (driver.getCurrentUrl().contains("signin")) {
                return false;
            }
            if (driver.getCurrentUrl().contains("dashboard")) {
                logoutCoinB();
                return false;
            }
            if (driver.findElement(By.cssSelector("div.session.verify")).findElement(By.tagName("h2")).getText()
                    .contains("Verify Your Email")) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    private void logoutCoinB() {
        driver.findElement(By.className("Dropdown__container___1kJL5")).click();
        driver.findElements(By.cssSelector("div.Flex.DropdownItem__container___3GCer")).get(2).click();
        driver.get(url);
    }

}
