package foo.games;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import fausets.Utils;
import foo.webdriver.StartChromeDriver;
import runnable.RunAll;

public class Recolector  implements Runnable {
	int BEEP = 10;
    int WAITB = 5;
    int MINUTES_TOWAIT = 5;

    WebDriver driver;
    
    String url;
    String user;
    String pass;
    
    Point p;
    Dimension s = RunAll.SIZE;

    public Recolector(String url, String user, String pass, Point p) {
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.p= p;
    }
    public void run() {
        driver = new StartChromeDriver().getDriver(driver);
        gamer();
    }

    private void gamer() {
        driver.manage().window().setSize(s);
        driver.manage().window().setPosition(p);
        driver.get(url);
        try {
        	getLogin();
        	

            boolean iscaptcha = true;
            boolean jugar = true;
            while(jugar){
            	try{
            		iscaptcha = seeCaptchaPostLogin();
           			while(iscaptcha && seeCaptchaPostLogin());
            		WebElement img = driver.findElement(By.id("Win2"));
        			img.click();
            	}catch(ElementNotVisibleException e){
            		continue;
            	}
            }

        } catch (Exception e) {
            e.printStackTrace();
            driver.close();
        } finally {
            driver.close();
        }
    }
    
    private void getLogin() {
    	
    	WebElement form_user = driver.findElement(By.name("form_user"));
    	form_user.sendKeys(this.user);
    	WebElement form_pwd = driver.findElement(By.name("form_pwd"));
    	form_pwd.sendKeys(this.pass);
    	driver.findElement(By.name("routing_code")).sendKeys("");
    	Utils.bip(BEEP, WAITB);
    	while(seeCaptchaPreLogin());
    }
    
    private boolean seeCaptchaPreLogin(){
		try {
			driver.findElement(By.className("button"));
		} catch (Exception e) {
			return false;
		}
        return true;

    }
    
    private boolean seeCaptchaPostLogin(){
		try {
				driver.findElement(By.name("captcha"));
		} catch (Exception e) {
			return false;
		}
        return true;

    }
}
