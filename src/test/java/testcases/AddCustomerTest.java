package testcases;

import java.io.IOException;
import java.util.Hashtable;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.TestBase;
import utils.TestUtil;

public class AddCustomerTest extends TestBase {

	
	@Test(dataProviderClass=TestUtil.class,dataProvider="dp")
	public void addCustomerTest(Hashtable <String,String> data) throws InterruptedException, IOException {
		checkToSkip();
		if (!data.get("runmode").equalsIgnoreCase("y")) {
			throw new SkipException("Skipping the info addCustomerTest run mode is no");
		}
		click("addCustBtn_CSS");
		type("firstname_CSS",data.get("firstName"));
		type("lastname_CSS",data.get("lastName"));
		type("postcode_CSS",data.get("postcode"));
		click("addbtn_CSS");
		
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());
		
		Assert.assertTrue(alert.getText().contains(data.get("alerttext")));
		
		alert.accept();
	}
}
