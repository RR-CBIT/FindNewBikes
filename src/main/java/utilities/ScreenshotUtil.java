package utilities;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class ScreenshotUtil {
    public static String takeScreenshot(WebDriver driver, String screenshotName) {
    	File srcFile,destFile = null;
        try {
            srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            destFile = new File("screenshots/" + screenshotName + ".png");
            FileUtils.copyFile(srcFile, destFile);
            
        } catch (IOException e) {
            System.err.println("Error saving screenshot: " + e.getMessage());
        }
		return destFile.getAbsolutePath();
    }
}
