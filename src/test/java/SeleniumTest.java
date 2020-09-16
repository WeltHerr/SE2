import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class SeleniumTest {

    private WebDriver driver = null;

    @Before
    public void setUpClass(){
        System.setProperty("webdriver.gecko.driver","F:\\Uni\\4. Semester\\SE II\\Projekt\\Geckodriver\\geckodriver.exe");
        File pathBinary = new File("E:\\Mozilla Firefox\\firefox.exe");
        FirefoxBinary firefoxBinary = new FirefoxBinary(pathBinary);
        DesiredCapabilities desired = DesiredCapabilities.firefox();
        FirefoxOptions options = new FirefoxOptions();
        desired.setCapability(FirefoxOptions.FIREFOX_OPTIONS, options.setBinary(firefoxBinary));
        driver = new FirefoxDriver(options);
    }

    @Test
    public void startWebDriver() {

        //Öffne Seite
        driver.get("http://localhost:8080/SE2/#!login");

        //Fullsize
        driver.manage().window().maximize();

        //Daten eingeben
        driver.findElement(By.xpath("//*[@id=\"emailField\"]")).sendKeys("test@test.de");
        driver.findElement(By.xpath("//*[@id=\"passwortField\"]")).sendKeys("test");

        //Click on Button "Login"
        driver.findElement(By.xpath("//*[@id=\"loginButton\"]")).click();

        //Sleep bis Seite aufgerufen
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Check ob gleich
        assertEquals("http://localhost:8080/SE2/#!main",driver.getCurrentUrl());
    }
    @After
    public void tearDownClass() {
        driver.quit();
    }

}