package testcases;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import base.TestBase;

public class BankManagerLoginTest extends TestBase{
	
	@Test
	public void loginAsBankManager() throws InterruptedException {
		//to handle the text as html
		System.setProperty("org.uncommons.reportng.escape-output", "false");
		log.debug("Inside Login Test");
		driver.findElement(By.cssSelector(OR.getProperty("bmlBtn"))).click();
		
		Assert.assertTrue(isElementPresent(By.cssSelector(OR.getProperty("addCustBtn"))),"Login Not Succesful");
		
		log.debug("Loggin Succesfully executed");
		Reporter.log("Loggin Succesfully executed");
		
		Reporter.log("<a target=\"_blank\" href=\"https://simgbb.com/images/logo.png\">Screenshot</a>");
	}
} 
