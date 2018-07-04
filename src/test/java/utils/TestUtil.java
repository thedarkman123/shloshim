package utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Hashtable;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.DataProvider;

import com.gargoylesoftware.htmlunit.javascript.host.Console;

import base.TestBase;

public class TestUtil extends TestBase {

	public static String screenshotPath;
	public static String screenshotName;
	
	public static void captureScreenshot() throws IOException {
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		screenshotName = new Date().toString().replace(":","_").replace(" ","_")+".jpg";
	
		FileUtils.copyFile(scrFile,new File(System.getProperty("user.dir") + "\\target\\surefire-reports\\html\\"+screenshotName));
	}
	
	@DataProvider(name="dp")
	public Object[][] getData(Method m){
		String sheetName = m.getName();
		int rows = excel.getRowCount(sheetName);//2
		int cols = excel.getColumnCount(sheetName);//1
		
		Object[][] data = new Object[rows-1][1];
		//the minus1 is because we dont need first row
		Hashtable<String,String> table = null; 
		
		//for each row, the key to it's value will be the first row name
		for (int rowNum = 2; rowNum<= rows; rowNum++) {
			table = new Hashtable<String,String>();
			for (int colNum = 0; colNum < cols;colNum++) {
				//the key wil be always the first row in the excel
				table.put(excel.getCellData(sheetName, colNum, 1), excel.getCellData(sheetName, colNum, rowNum));	
				data[rowNum-2][0] = table;
			}
		}
		
		return data;
	}
	
}
