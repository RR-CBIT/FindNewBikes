package utilities;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportManager {
    private static ExtentReports extent;
    private static ExtentTest test;

    public static ExtentReports setupExtentReport() {
        if (extent == null) {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String reportPath = "reports/ExtentReport_" + timestamp + ".html";

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
        }
        return extent;
    }
    public static ExtentTest createTest(String testName) {
        test = extent.createTest(testName);
        return test;
    }
    public static void logTestStep(String stepDescription) {
        test.info(stepDescription);
    }

    public static void captureScreenshot(String screenshotPath) {
        test.addScreenCaptureFromPath(screenshotPath);
    }

    public static void flushReport() {
        extent.flush();
    }
}
