package com.swap.manager;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class PracticeSelenium {

	static WebDriver driver;

	public static void main(String[] args) throws InterruptedException {
		
		

	}

	static void Chrome(String URL) {
		ChromeOptions Options = new ChromeOptions();
		Options.addArguments("--guest");
		Options.addArguments("--start-maximized");

		driver = new ChromeDriver(Options);
//		driver.get("https://www.redbus.in/");
		driver.get(URL);
		System.out.println("ChromeDriver launched successfully...");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

		driver.close();
		driver.quit();
	}

}
