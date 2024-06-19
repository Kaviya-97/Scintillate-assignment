package Scintillate;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class FlipkartWebAutomation {

    public static void main(String[] args) {
        // Set path to ChromeDriver executable
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.flipkart.com/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//input[@class='Pke_EE']")).sendKeys("Hindi Books");
        driver.findElement(By.xpath("//button[@class='_2iLD__']")).click();
        WebElement bookPage=driver.findElement(By.xpath("//section[@class='Iu4qXa']"));
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOf(bookPage));
	     List<WebElement> productName = driver.findElements(By.xpath("//a[@class='wjcEIp']"));
	     List<WebElement> productPrice=driver.findElements(By.xpath("//div[@class='Nx9bqj']"));
	     List<WebElement> userRating=driver.findElements(By.xpath("//div[@class='XQDdHH']"));
	     JSONArray productsArray = new JSONArray(); 
	     JSONObject productObj = new JSONObject();
	     for(int i=0;i<productName.size();i++) {
	
	     for(WebElement Name:productName) {
	    	 String name =Name.getText();
	    	 productObj.put("name", name);
	     }
	     for(WebElement userrating:userRating) {
	    	 String rating =userrating.getText();
	    	 productObj.put("rating", rating);
	     }
	     for(WebElement productprice:productPrice) {
	    	 String price =productprice.getText();
	    	 productObj.put("price", price);
	     }
	     productsArray.put(productObj);     }
	     System.out.println("Scraped Data from Flipkart:");
	     System.out.println(productsArray.toString());
         ////////////////////////////////
	     AppiumDriver Appdriver = null;
	     DesiredCapabilities capabilities = new DesiredCapabilities();
         capabilities.setCapability(CapabilityType.PLATFORM_NAME, "Android");
         capabilities.setCapability("deviceName", "emulator-5554"); // Replace with your device name
         capabilities.setCapability("app", "/path/to/your/app.apk"); // Replace with your app path
         URL url = new URL("http://localhost:4723/wd/hub");
         Appdriver = new AndroidDriver(url, capabilities);
         File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
         String screenshotPath = "\"C:\\Users\\kumar\\eclipse-workspace\\Assignment\\Screenshot\""; // Replace with your desired path
         org.apache.commons.io.FileUtils.copyFile(screenshotFile, new File(screenshotPath));
         System.out.println("Screenshot captured: " + screenshotPath);
         /// using OCR
         File screenshotFileFlipkart = new File("\"C:\\Users\\kumar\\eclipse-workspace\\Assignment\\Screenshot\"");
         String extractedTextFlipkart = performOCR(screenshotFileFlipkart);
         File screenshotFileAppium = new File("\"C:\\Users\\kumar\\eclipse-workspace\\Assignment\\Screenshot\"");
         String extractedTextAppium = performOCR(screenshotFileAppium);
         JSONArray productsFromSelenium = new JSONArray(productsArray.toString());
         for (int i = 0; i < productsFromSelenium.length(); i++) {
        	    JSONObject productSelenium = productsFromSelenium.getJSONObject(i);
        	    String nameSelenium = productSelenium.getString("name");
        	    String priceSelenium = productSelenium.getString("price");
        	    JSONObject productOCR = extractProductFromOCR(extractedTextAppium, nameSelenium);
        	    if (productOCR != null) {
        	        String priceOCR = productOCR.getString("price");

        	        // Compare prices
        	        if (!priceSelenium.equals(priceOCR)) {
        	            System.out.println("Price discrepancy found for product: " + nameSelenium);
        	            System.out.println("Selenium price: " + priceSelenium);
        	            System.out.println("OCR price: " + priceOCR);
        	        }
        	    }
        	}
         private static String performOCR(File screenshotFile1) {
        	    // Implement OCR logic here using Tesseract or other OCR libraries
        	    // Example:
        	    String extractedText = ""; // Replace with actual OCR logic
        	    return extractedText;
        	}
    }

	
    
    
}

