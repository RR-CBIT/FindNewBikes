package hack;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class UsedCars{
    private WebDriver driver;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.zigwheels.com/");
    }

    @Test(priority = 1)
    public void navigateToUsedCars() {
        driver.findElement(By.cssSelector("span.c-p.icon-down-arrow")).click();
        driver.findElement(By.cssSelector("a[data-track-label='nav-used-car']")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("used-car"), "Failed to select used cars.");
        
    }

    @Test(priority = 2)
    public void selectCityChennai() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement chennaiLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title='Chennai']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", chennaiLink);
        
        Assert.assertTrue(driver.getCurrentUrl().contains("used-car/Chennai"), "Failed to select used cars.");
    }

    @Test(priority = 3)
    public void verifyPopularModels() {
        List<WebElement> checkboxes = driver.findElements(By.cssSelector("input.carmmCheck"));
        Assert.assertFalse(checkboxes.isEmpty(), "No popular models found.");
        for (WebElement checkbox : checkboxes) {
            System.out.println("Model: " + checkbox.getAttribute("car_name"));
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
