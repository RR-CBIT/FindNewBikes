package hack;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Set;
public class UserLogin {

    private WebDriver driver;
    private WebDriverWait wait;
    private String originalWindow;

    @BeforeClass
    public void setUp() {
    	ChromeOptions options = new ChromeOptions();
    	options.addArguments("--disable-popup-blocking"); 
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.zigwheels.com");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement loginButton = driver.findElement(By.id("des_lIcon"));
        loginButton.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("googleSignIn"))).click();

        originalWindow = driver.getWindowHandle();
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        Set<String> windowHandles = driver.getWindowHandles();
        for (String handle : windowHandles) {
            if (!handle.equals(originalWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }
    }

    @Test(priority = 1)
    public void testInvalidEmailFormat() {
        String email = "wrong-email@exa";
        enterEmail(email);

        // Validate error message for invalid email format
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@jsname='B34EJ' and @class='dEOOab RxsGPe']//div[@class='Ekjuhf Jj6Lae']")));
        String actualMessage = errorMessage.getText();
        String expectedMessage = "Enter a valid email or phone number";
        Assert.assertEquals(actualMessage, expectedMessage, "Error message does not match for invalid email format!");
    }

    @Test(priority = 2)
    public void testNonExistentEmail() {
        String email = "wrongemail@example.com";
        enterEmail(email);

        // Validate error message for non-existent email
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@jsname='B34EJ' and @class='dEOOab RxsGPe']//div[@class='Ekjuhf Jj6Lae']")));
        String actualMessage = errorMessage.getText();
        String expectedMessage = "Couldn’t find your Google Account";
        Assert.assertEquals(actualMessage, expectedMessage, "Error message does not match for non-existent email!");
    }

    @Test(priority = 3)
    public void testValidEmailRequiresPassword() {
        String email = "mandasriharsha04@gmail.com";
        enterEmail(email);

    }

    @Test(priority = 4)
    public void testBrowserInsecureError() {

        try {
            WebElement nextStepErrorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='dMNVAe' and contains(text(), 'This browser or app may not be secure')]")));
            String actualMessage = nextStepErrorMessage.getText();
            String expectedMessage1 = "This browser or app may not be secure.";
            String expectedMessage2 = "This browser or app may not be secure. Learn more";

            Assert.assertTrue(
                actualMessage.equals(expectedMessage1) || actualMessage.equals(expectedMessage2),
                "Error message does not match for browser security issue!"
            );

        } catch (Exception e) {
            Assert.fail("No 'This browser or app may not be secure' error message found.");
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void enterEmail(String email) {
        WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("identifierId")));
        emailField.clear();
        emailField.sendKeys(email);

        WebElement nextButton = driver.findElement(By.id("identifierNext"));
        nextButton.click();
    }
}
