package base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import utils.ExcelReader;
import utils.ExtentManager;
import utils.TestUtil;

//initializors
public class TestBase {
	/*
	 * Webdriver  - done
	 * Properties - done
	 * Logs       - done
	 * 
	 */
	
	public static WebDriver driver;
	public static Properties config = new Properties();
	public static Properties OR = new Properties();
	public static FileInputStream fis;
	public static Logger log = Logger.getLogger("devpinoyLogger");
	public static ExcelReader excel  = new ExcelReader(System.getProperty("user.dir") + "\\src\\test\\resources\\excel\\testdata.xlsx");
	public static WebDriverWait wait;
	protected ExtentReports rep = ExtentManager.getInstance();
	public static ExtentTest test;
	public static String currentTestName;
	
	@BeforeSuite
	public void setup() {
		if (driver == null) {
			try {
				fis = new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\properties\\Config.properties");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				config.load(fis);
				log.debug("config file loaded");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    try {
				fis = new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\properties\\OR.properties");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				OR.load(fis);
				log.debug("Or file loaded");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		DesiredCapabilities dc = new DesiredCapabilities();
		dc.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
		if (config.getProperty("browser").equals("firefox")) {
			//add in case working with selenium 3 build
			System.setProperty("webdriver.gecko.driver",System.getProperty("user.dir")+"\\src\\test\\resources\\executables\\geckodriver.exe");
			driver = new FirefoxDriver(dc);
		} else if(config.getProperty("browser").equals("chrome")) {
			System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"\\src\\test\\resources\\executables\\chromedriver.exe");
			driver = new ChromeDriver(dc);
			log.debug("Chrome launched");
		} else {
			System.setProperty("webdriver.ie.driver",System.getProperty("user.dir")+"\\src\\test\\resources\\executables\\IEDriverServer.exe");
			driver = new InternetExplorerDriver(dc);
		}
		
		String testSiteUrl = config.getProperty("testsiteurl");
		driver.get(testSiteUrl);
		log.debug("Navigated to : " + testSiteUrl );
		driver.manage()
		      .window()
		      .maximize();
		driver.manage()
		      .timeouts()
		      .implicitlyWait(Integer.parseInt(config.getProperty("implicit.wait")),TimeUnit.SECONDS);
	
		wait = new WebDriverWait(driver,5);
	}
	
	public boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch(NoSuchElementException e) {
			return false;
		}
	}
	
	public void click(String locator) {
		if(locator.endsWith("_CSS")) {
			driver.findElement(By.cssSelector(OR.getProperty(locator))).click();
		} else if (locator.endsWith("_XPATH")) {
			driver.findElement(By.xpath(OR.getProperty(locator))).click();
		} else if (locator.endsWith("_ID")) {
			driver.findElement(By.id(OR.getProperty(locator))).click();
		}
		test.log(LogStatus.INFO, "Clicking on: " + locator);
	}
	
	static WebElement dropdown;
	
	public void select(String locator, String value) {
		if(locator.endsWith("_CSS")) {
			dropdown = driver.findElement(By.cssSelector(OR.getProperty(locator)));
		} else if (locator.endsWith("_XPATH")) {
			dropdown = driver.findElement(By.xpath(OR.getProperty(locator)));
		} else if (locator.endsWith("_ID")) {
			dropdown = driver.findElement(By.id(OR.getProperty(locator)));
		}
		
		Select select = new Select(dropdown);
		select.selectByVisibleText(value);
		test.log(LogStatus.INFO, "selecting from dropdown: " + locator + ", the value: " + value);
	}
	
	public void checkToSkip() {
		//the current test name comes from the listener, assign to static var
		if (!TestUtil.isTestRunnable(currentTestName,excel)) {
			throw new SkipException("Skipping the test openAccountTest run mode is no");
		}
	}
	
	public void type(String locator, String value) {
		if(locator.endsWith("_CSS")) {
			driver.findElement(By.cssSelector(OR.getProperty(locator))).sendKeys(value);
		} else if (locator.endsWith("_XPATH")) {
			driver.findElement(By.xpath(OR.getProperty(locator))).sendKeys(value);
		} else if (locator.endsWith("_ID")) {
			driver.findElement(By.id(OR.getProperty(locator))).sendKeys(value);
		}		
		test.log(LogStatus.INFO, "typing in: " + locator + ", entered value as: " + value);
	}
	
	public static boolean isTestRunnable(String testName, ExcelReader excel) {
		String sheetName = "test_suite";
		
		int rows = excel.getRowCount(sheetName);
		
		for (int rNum = 2; rNum <= rows; rNum++) {
			String testCase = excel.getCellData(sheetName, "TCID", rNum);
			if(testCase.equalsIgnoreCase(testName)) {
				String runmode = excel.getCellData(sheetName, "Runmode", rNum);
				if (runmode.equals("Y")) {
					return true;
				} else {
					return false;
				}
			}
		}
		
		return false;
	}
	
	public static void verifyEquals(String expected,String actual) throws IOException {
		try {
			Assert.assertEquals(expected, actual);
		} catch (Throwable t) {
			TestUtil.captureScreenshot();
			Reporter.log("<br> Verifcation failure:" + t.getMessage()+"</br>");
			Reporter.log("<a target=\"_blank\" href=\""+ TestUtil.screenshotName + "\"><img src=\""+ TestUtil.screenshotName + "\" width=200 height=200 ></a>");
			Reporter.log("<br>");
			Reporter.log("<br>");
			test.log(LogStatus.FAIL, "Verification failed with exception: " + t.getMessage().toUpperCase());
			test.log(LogStatus.FAIL,test.addScreenCapture(TestUtil.screenshotName));
		}
	}
	
	
	@AfterSuite
	public void teardown() {
		
		if (driver != null) {
			driver.quit();
			System.out.println("we are done mate");
			log.debug("test execution completed!!! ");
		}
		
	}
}
