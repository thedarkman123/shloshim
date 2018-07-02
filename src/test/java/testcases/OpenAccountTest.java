package testcases;

import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import base.TestBase;
import utils.TestUtil;

public class OpenAccountTest extends TestBase {

	
	@Test(dataProviderClass=TestUtil.class,dataProvider="dp")
	public void openAccountTest(String customer, String currency) throws InterruptedException{
		checkToSkip();
		click("openaccount_CSS"); //step 1
		select("customer_CSS",customer);
		select("currency_CSS",currency);
		click("process_CSS");
		Thread.sleep(2000);
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		alert.accept();
	}
	
	
}