package ru.oshokin.store;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.util.List;

@TestPropertySource("classpath:application.properties")
@SpringBootTest(classes = Store.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SeleniumTests extends AbstractTestNGSpringContextTests {

    private WebDriver driver;

    @Value("${server.port}")
    private int serverPort;

    @Value("${server.servlet.context-path}")
    private String serverContextPath;

    @BeforeSuite
    public void startDriver() {
        String workDir = System.getProperty("user.dir");
        System.setProperty("webdriver.gecko.driver", Paths.get(workDir, "src", "test", "resources", "geckodriver.exe").toString());

        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("security.fileuri.strict_origin_policy", false);

        FirefoxOptions options = new FirefoxOptions();
        options.setProfile(profile);
        options.setCapability("marionette", true);

        options.setHeadless(true);
        driver = new FirefoxDriver(options);
    }

    @Test
    public void thereAreSomeProducts() {
        driver.get(String.format("http://localhost:%d%s/shop", serverPort, serverContextPath));
        List<WebElement> products = driver.findElements(By.xpath("//table/tbody/tr"));
        Assert.assertTrue(products.size() > 0);
    }

    @AfterSuite
    public void stopDriver() {
        if (driver != null) driver.quit();
    }

}