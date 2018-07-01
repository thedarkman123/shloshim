package testcases;

import org.testng.annotations.Test;

import base.TestBase;
import utils.TestUtil;

public class OpenAccountTest extends TestBase {

	
	@Test(dataProviderClass=TestUtil.class,dataProvider="dp")
	public void openAccountTest(String customer, String currency){
		//click("addCustBtn_CSS");
	}
	
	
}