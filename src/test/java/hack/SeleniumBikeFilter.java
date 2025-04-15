package hack;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import org.testng.annotations.*;

import models.Bike;
import utilities.DriverSetup;

public class SeleniumBikeFilter {
    
    private WebDriver driver;
    
    // Page Factory Elements
    @FindBy(linkText = "NEW BIKES")
    private WebElement newBikesLink;

    @FindBy(xpath = "//li[contains(@data-track-label, 'upcoming-tab')]")
    private WebElement upcomingTab;

    @FindBy(css = ".lnk-c[data-track-label='view-all-bike'][title='All Upcoming Bikes']")
    private WebElement upcomingBikesLink;

    @FindBy(xpath = "//a[text()='Honda']")
    private WebElement brand;

    @FindBy(css = ".lnk-hvr.block.of-hid.h-height")
    private List<WebElement> upcomingBikes;

    @FindBy(css = ".b.fnt-15")
    private List<WebElement> bikePrices;

    @FindBy(css = ".clr-try.fnt-14")
    private List<WebElement> bikeDates;

    public SeleniumBikeFilter() {
        // Default constructor (necessary for TestNG instantiation)
    }

    public SeleniumBikeFilter(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this); 
    }

    @BeforeClass
    public void setUp() {
        driver= DriverSetup.getDriver();
        driver.get("https://www.zigwheels.com/");
        PageFactory.initElements(driver, this);
    }

    @Test(priority = 1)
    public void navigateToUpcomingBikes() {
        newBikesLink.click();
        
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", upcomingTab);
        js.executeScript("arguments[0].click();", upcomingBikesLink);

        Assert.assertTrue(driver.getCurrentUrl().contains("upcoming-bikes"), "Navigation to 'Upcoming Bikes' failed!");
    }

    @Test(priority = 2)
    public void verifyHondaBikeFiltering() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", brand);
        Assert.assertFalse(upcomingBikes.isEmpty(), "No Honda bikes found after applying filter!");
    }

    @Test(priority = 3)
    public void filterHondaBikes() {
        List<Bike> affordableHondaBikes = new ArrayList<>();
        List<Bike> testBikes = new ArrayList<>();
        
        System.out.println("Affordable Upcoming Honda Bikes (under 4L):");

        // Validate List Sizes Before Iterating
        if (upcomingBikes.size() != bikePrices.size() || upcomingBikes.size() != bikeDates.size()) {
            throw new RuntimeException("Mismatch in extracted bike details! Check XPath selectors.");
        }

        for (int i = 0; i < upcomingBikes.size(); i++) {
            String priceText = bikePrices.get(i).getText();
            String bikeName = upcomingBikes.get(i).getText();
            String launchDate = formatReleaseDate(bikeDates.get(i).getText());

            testBikes.add(new Bike(bikeName, priceText, launchDate));
        }

        affordableHondaBikes = testBikes.stream()
            .filter(bike -> processPrice(bike.getPrice()) < 4)
            .collect(Collectors.toList());

        Assert.assertFalse(affordableHondaBikes.isEmpty(), "No affordable Honda bikes found under 4L!");

        for (Bike bike : affordableHondaBikes) {
            System.out.println(bike);
        }
    }

    @AfterClass
    public void tearDown() {
        DriverSetup.quitDriver();
    }

    // Format Date: Convert "Expected Launch : Jul 2025" -> "07/2025"
    private static String formatReleaseDate(String releaseDate) {
        try {
            releaseDate = releaseDate.replaceAll(".*?:", "").trim(); // Remove prefix like "Expected Launch :"
            DateFormat inputFormat = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
            DateFormat outputFormat = new SimpleDateFormat("MM/yyyy");

            return outputFormat.format(inputFormat.parse(releaseDate));
        } catch (ParseException e) {
            System.err.println("Error parsing date: " + releaseDate);
            return "Invalid Date";
        }
    }

    // Process Price: Convert "Rs. 79,000" -> 0.79L, Keep Original String Format
    public static double processPrice(String priceText) {
        if (priceText == null || priceText.isEmpty()) {
            System.err.println("Price is missing or invalid.");
            return -1; // Indicator of failure
        }

        // Remove unwanted text (keeping only digits and a single decimal)
        String cleanPrice = priceText.replaceAll("[^0-9.]", "").trim();

        // Ensure the first character isn't an unexpected period (like ".6.00")
        if (cleanPrice.startsWith(".")) {
            cleanPrice = cleanPrice.substring(1);
        }

        try {
            if (priceText.contains(",")) {  
                return Double.parseDouble(cleanPrice) / 100000; // Convert 79,000 to 0.79L
            } else {
                return Double.parseDouble(cleanPrice); // Already in Lakh format
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing price: " + priceText + " | Cleaned price: " + cleanPrice);
            return -1;
        }
    }

}
