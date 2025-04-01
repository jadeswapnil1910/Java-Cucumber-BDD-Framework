package hooks;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.swap.manager.ConfigReader;
import com.swap.manager.DriverMngr;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;
import utils.TestContextSetup;

public class Hooks {

	TestContextSetup testContextSetup;
	public WebDriver driver;
	String BrowserName;
	Properties prop;
	ConfigReader config;
	String URL;
	public static Scenario testCase;
	Collection<String> sourceTagNames;

	@Before(order = 0)
	public void getProperty(Scenario scenario) {
		testCase = scenario;
		config = new ConfigReader();
		prop = config.init_prop("global");
		System.out.println("[Test Case] : " + testCase.getName());
		
		String[] split = CustomEventListener.source.split("\n");
		
		System.out.println(split[2]);
//		System.out.println(CustomEventListener.source);
		

	}

	@Before(order = 1)
	public void launchBrowser() throws InterruptedException {
		sourceTagNames = testCase.getSourceTagNames();
		testCase.log("Tags : " + sourceTagNames);

		// Don't launch browser if tag contains @DataBase

		if (sourceTagNames.contains("@Database")) {
			testCase.log("Backend scenario hence browser is not initialized.");
		} else {
			BrowserName = prop.getProperty("browser");
			URL = prop.getProperty("QAUrl");
			DriverMngr driverMngr = new DriverMngr();
			driver = driverMngr.init_driver(BrowserName);
			driver.get(URL);
			System.out.println("[PASS] : URL - " + URL);

		}
	}

	@After
	public void AfterScenario() throws IOException {
		
		
		driver.quit();
	}

	@AfterStep
	public void AddScreenshot(Scenario scenario) throws IOException {
		
		System.out.println("[ STEP Name ] : "+CustomEventListener.stepName);
		System.out.println("[ STEP Status ] : "+CustomEventListener.stepStatus);

		if (scenario.isFailed()) {
			// screenshot
			File sourcePath = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			byte[] fileContent = FileUtils.readFileToByteArray(sourcePath);
			scenario.attach(fileContent, "image/png", "image");

		}

	}

}
