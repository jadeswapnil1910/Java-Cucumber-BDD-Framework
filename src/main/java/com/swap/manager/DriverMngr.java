package com.swap.manager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class DriverMngr {

	public WebDriver driver;
	// Concept of ThreadLocal if parallel execution is required
	public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<WebDriver>();

	/***
	 * This method is used to initialize the thradlocal driver on the basis of given
	 * browser
	 * 
	 * @param browser Greturn this will return tldaiven:
	 * @throws InterruptedException
	 ***/
	public WebDriver init_driver(String browser) throws InterruptedException {

//		Proxy proxy = new Proxy();
//		proxy.setHttpProxy("oproxy.fg.rbc.com:8080");
//		proxy.setSslProxy("oproxy.fg.rbc.com:8080");
		if (browser.equalsIgnoreCase("chrome")) {
			ChromeOptions option = new ChromeOptions();
//		option.setCapability(CapabilityType.PROXY, proxy);
			option.addArguments("--ignore-ssl-errors=yes");
			option.addArguments("--ignore-certificate-errors");
//			option.addArguments("--force-device-scale-factor=0.8");
			option.addArguments("start-maximized");
			option.addArguments("--remote-allow-origins=*");
			tlDriver.set(new ChromeDriver(option));
			System.out.println("[PASS] : Chrome browser is initialised successfully");
		} else if (browser.equalsIgnoreCase("edge")) {
			EdgeOptions options = new EdgeOptions();
//		options.setCapability("proxy", proxy);
			options.addArguments("--ignore-ssl-errors=yes");
			options.addArguments("--ignore-certificate-errors");
			options.addArguments("start-maximized");
			options.addArguments("--remote-allow-origins=*");
			tlDriver.set(new EdgeDriver(options));
			System.out.println("Edge browser is initialised successfully");
		} else {
			System.out.println("Please pass the correct browser value: " + browser);
		}
		getDriver().manage().deleteAllCookies();
		getDriver().manage().window().maximize();
		return getDriver();

	}

	/***
	 * this is used to get the driver with ThreadLocal
	 * 
	 * @return
	 ***/
	public static synchronized WebDriver getDriver() {
		return tlDriver.get();
	}

}
