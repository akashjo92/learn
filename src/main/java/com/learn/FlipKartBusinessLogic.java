package com.learn;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public class FlipKartBusinessLogic extends FlipkartObjects{
	static WebDriver driver;
	static String path = System.getProperty("user.dir");
	String Chromepath2 = path+"\\Drivers\\chromedriver.exe";
	static List<String[]> dataLines = new ArrayList();
//	static String urlFlipKart = "https://www.flipkart.com/";
	static String product = "iPhones";

	
	public static void navigate(String urlFlipKart) {
		driver.get(urlFlipKart);
		WebDriverWait wait = new WebDriverWait(driver, 150);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(closeIcon)));	
		driver.findElement(By.xpath(closeIcon)).click();
		driver.manage().window().maximize();
	}
	
	
	public static void search() throws Exception {
		WebDriverWait wait1 = new WebDriverWait(driver, 150);
		wait1.until(ExpectedConditions.elementToBeClickable(By.xpath(searchInput)));	
		driver.findElement(By.xpath(searchInput)).sendKeys(product);
		driver.findElement(By.xpath(searchIcon)).click();
		wait1.until(ExpectedConditions.elementToBeClickable(By.xpath(maxpriceDropdown)));
		Select dropdownMax = new Select(driver.findElement(By.xpath(maxpriceDropdown)));
		dropdownMax.selectByValue("50000");
		wait1.until(ExpectedConditions.elementToBeClickable(By.xpath(lowhighLink)));
		driver.findElement(By.xpath(lowhighLink)).click();
		FlipKartBusinessLogic r = new FlipKartBusinessLogic();		
		dataLines.add(new String[] 
				  { "Device Details", "Price", "Ratings"});
		r.givenDataArray_whenConvertToCSV_thenOutputCreated(dataLines);
		List<WebElement> PagesNex = driver.findElements(By.xpath(totalNoPages));
		for (int i = 1; i < PagesNex.size(); i++) {
			driver.navigate().refresh();
			List<WebElement> name = driver.findElements(By.xpath("//div[@class='bhgxx2 col-12-12']/div/div/div/a/div[2]/div/div[contains(text(),'Apple')]"));
			
			for (int j = 0; j < name.size(); j++) {
				wait1.until(ExpectedConditions.elementToBeClickable(name.get(j)));
//				System.out.println("Name: "+name.get(j).getText());
				String Ratings = driver.findElement(By.xpath("(//span[contains(text(),'Ratings')])["+(j+1)+"]")).getText();
				String[] spRate = Ratings.split(" ");
//				System.out.println("Ratings:"+spRate[0]);
				
				String price = driver.findElement(By.xpath("(//div[@class='col col-5-12 _2o7WAb']/div/div/div[1])["+(j+1)+"]")).getText();
				String remPrice = price.substring(1, price.length());
//				System.out.println("Price:"+remPrice);
				writeCSV(name.get(j).getText(), spRate[0], remPrice);
			}
			
		if(driver.findElements(By.xpath(nextButton)).size()>=1) {
			driver.findElement(By.xpath(nextButton)).click();
			}
		}
		
		
	}
	private static void writeCSV(String text, String string, String remPrice) throws Exception {
		FlipKartBusinessLogic r = new FlipKartBusinessLogic();
		
		dataLines.add(new String[] 
				  { text, string, remPrice});
		r.givenDataArray_whenConvertToCSV_thenOutputCreated(dataLines);
	}

	public static void SetChromeProperties() {
		FlipKartBusinessLogic object = new FlipKartBusinessLogic();
		System.setProperty("webdriver.chrome.driver", object.Chromepath2);
		driver = new ChromeDriver();
	}
	public String escapeSpecialCharacters(String data) {
	    String escapedData = data.replaceAll("\\R", " ");
	    if (data.contains(",") || data.contains("\"") || data.contains("'")) {
	        data = data.replace("\"", "\"\"");
	        escapedData = "\"" + data + "\"";
	    }
	    return escapedData;
	}
	public String convertToCSV(String[] data) {
	    return Stream.of(data)
	      .map(this::escapeSpecialCharacters)
	      .collect(Collectors.joining(","));
	}
	public void givenDataArray_whenConvertToCSV_thenOutputCreated(List<String[]> dataLines) throws IOException {
	    File csvOutputFile = new File(path+"\\FlipKartOutputFile\\output.csv");
	    try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
	        dataLines.stream()
	          .map(this::convertToCSV)
	          .forEach(pw::println);
	    }
	    assertTrue(csvOutputFile.exists());
	}
}
