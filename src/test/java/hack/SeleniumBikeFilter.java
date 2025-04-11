package hack;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SeleniumBikeFilter {
    public SeleniumBikeFilter() {
    }

    public SeleniumBikeFilter(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this); 
    }

    private WebDriver driver;
    private SeleniumBikeFilter page;

    @FindBy(linkText = "NEW BIKES")
    private WebElement newBikesLink;

    @FindBy(xpath = "//li[contains(@data-track-label, 'upcoming-tab')]")
    private WebElement upcomingTab;

    @FindBy(css = ".lnk-c[data-track-label='view-all-bike'][title='All Upcoming Bikes']")
    private WebElement upcomingBikesLink;

    @FindBy(xpath = "/a[Honda]")
    private WebElement brand;

    @FindBy(xpath = "//span[text()='Read More']")
    private WebElement readMoreElement;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.zigwheels.com/");
        page = new SeleniumBikeFilter(driver);
    }

    @Test(priority = 1)
    public void navigateToUpcomingBikes() {
        page.newBikesLink.click();

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", page.upcomingTab);
        js.executeScript("arguments[0].click();", page.upcomingBikesLink);

        Assert.assertTrue(driver.getCurrentUrl().contains("upcoming-bikes"), "Navigation to 'Upcoming Bikes' failed!");
    }
//
//    @Test(priority = 2)
//    public void validateDropdownOptions() {
//        Select select = new Select(page.brand);
//        List<WebElement> options = select.getOptions();
//
//        List<String> expectedManufacturers = List.of("TVS", "Bajaj", "Honda", "Hero Moto Corp", "Suzuki", "Yamaha");
//        List<String> actualManufacturers = options.stream()
//                .map(WebElement::getText)
//                .collect(Collectors.toList());
//
//        Assert.assertTrue(actualManufacturers.containsAll(expectedManufacturers), "Dropdown manufacturers are missing!");
//    }

    @Test(priority = 2)
    public void filterHondaBikes() {
        page.brand.click();

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].style.border='3px solid red'", page.readMoreElement);
        js.executeScript("arguments[0].click();", page.readMoreElement);

        List<WebElement> bikeRows = driver.findElements(By.cssSelector("table tbody tr"));

        List<Bike> affordableUpcomingBikes = bikeRows.stream()
                .map(SeleniumBikeFilter::extractBikeDetails)
                .filter(bike -> bike != null && bike.getNumericPrice() < 400000)
                .collect(Collectors.toList());

        System.out.println("Affordable Upcoming Honda Bikes (under 4L):");
        affordableUpcomingBikes.forEach(System.out::println);

        Assert.assertFalse(affordableUpcomingBikes.isEmpty(), "No affordable Honda bikes found under 4L!");
    }

//    @Test(priority = 4)
//    public void validateExtractedBikeData() {
//        WebElement firstRow = driver.findElement(By.cssSelector("table tbody tr:nth-child(1)"));
//        Bike bike = extractBikeDetails(firstRow);
//
//        Assert.assertNotNull(bike, "Bike data extraction failed!");
//        Assert.assertFalse(bike.getName().isEmpty(), "Bike name is missing!");
//        Assert.assertFalse(bike.getPrice().isEmpty(), "Bike price is missing!");
//    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private static Bike extractBikeDetails(WebElement row) {
        try {
            String name = row.findElement(By.cssSelector("td:nth-child(1)")).getText();
            String price = row.findElement(By.cssSelector("td:nth-child(2)")).getText();
            String releaseDate = row.findElement(By.cssSelector("td:nth-child(3)")).getText();

            String formattedDate = releaseDate.equalsIgnoreCase("Unrevealed") ? "Unrevealed" : formatReleaseDate(releaseDate);

            return new Bike(name, price, formattedDate);
        } catch (Exception e) {
            return null;
        }
    }

    private static String formatReleaseDate(String releaseDate) {
        try {
            DateFormat inputFormat = new SimpleDateFormat("MMM yyyy");
            DateFormat outputFormat = new SimpleDateFormat("MM/yyyy");
            Date date = inputFormat.parse(releaseDate);
            return outputFormat.format(date);
        } catch (Exception e) {
            return "Invalid Date";
        }
    }
}
