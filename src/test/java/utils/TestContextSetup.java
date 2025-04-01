package utils;

import java.io.IOException;

import org.openqa.selenium.WebDriver;

import com.swap.manager.DriverMngr;

import pageObjects.PageObjectManager;

public class TestContextSetup {

	public String landingPageProductName;
	public PageObjectManager pageObjectManager;
	public GenericUtils genericUtils;

	
	public TestContextSetup() throws IOException
	{
		pageObjectManager = new PageObjectManager(DriverMngr.getDriver());
		genericUtils = new GenericUtils(DriverMngr.getDriver());

	}
	
}
