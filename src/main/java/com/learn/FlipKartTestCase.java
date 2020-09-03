package com.learn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class FlipKartTestCase extends FlipKartBusinessLogic{
	static List<String[]> dataLines = new ArrayList();
	static String product = "iPhones";
	
	@Parameters("url")	
	@Test
	public void testcase(String url) throws Exception {
		SetChromeProperties();
		navigate(url);
		search();
	}
	
	@AfterTest
	public void close() {
		driver.close();

	}
	

}
