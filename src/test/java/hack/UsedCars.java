package hack;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UsedCars {
	public static void main(String[] args) {

		WebDriver driver= new ChromeDriver();
		driver.get("https://www.zigwheels.com/");
		driver.manage().window().maximize();
		WebElement moreDropdown = driver.findElement(By.cssSelector("span.c-p.icon-down-arrow"));
        moreDropdown.click();

        WebElement usedCarsLink = driver.findElement(By.cssSelector("a[data-track-label='nav-used-car']"));
        usedCarsLink.click();
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement chennaiLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@title='Chennai']")));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", chennaiLink);        
        List<WebElement> checkboxes = driver.findElements(By.cssSelector("input.carmmCheck"));
        
        System.out.println("Popular Models in Chennai:");
        for (WebElement checkbox : checkboxes) {
            System.out.println(checkbox.getAttribute("car_name"));
        }
        driver.quit();
	}
}
