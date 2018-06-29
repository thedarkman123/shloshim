package testcases;

import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.TestBase;

public class AddCustomerTest extends TestBase {

	
	@Test(dataProvider="getData")
	public void addCustomer(String firstName, String lastName,String postCode) throws InterruptedException {
		driver.findElement(By.cssSelector(OR.getProperty("addCustBtn"))).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(OR.getProperty("firstname"))).sendKeys(firstName);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(OR.getProperty("lastname"))).sendKeys(lastName);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(OR.getProperty("postcode"))).sendKeys(postCode);
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(OR.getProperty("addbtn"))).click();	
		Thread.sleep(3000);
	}
	
	@DataProvider
	public Object[][] getData(){
		String sheetName = "AddCustomerTest";
		int rows = excel.getRowCount(sheetName);
		int cols = excel.getColumnCount(sheetName);
		
		Object[][] data = new Object[rows-1][cols];
		
		for (int rowNum = 2; rowNum<= rows; rowNum++) {
			for (int colNum = 0; colNum < cols;colNum++) {
				data[rowNum-2][colNum] = excel.getCellData(sheetName,colNum,rowNum);
			}
		}
		
		return data;
	}
}
