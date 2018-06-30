package utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import base.TestBase;

public class TestUtil extends TestBase {

	public static String screenshotPath;
	public static String screenshotName;
	
	public static void captureScreenshot() throws IOException {
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		screenshotName = new Date().toString().replace(":","_").replace(" ","_")+".jpg";
	
		FileUtils.copyFile(scrFile,new File(System.getProperty("user.dir") + "\\target\\surefire-reports\\html\\"+screenshotName));
	}
	
}
