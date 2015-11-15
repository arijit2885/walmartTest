package automationFramework;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class WalmartStore {
	
	
	/*
	 * Method to load the appropriate selenium driver based on browser type from input.
	 */
	public WebDriver initializeDriver (String driverType, String driverPath){
		if (driverType.equalsIgnoreCase("Firefox")){
			WebDriver driver = new FirefoxDriver ();
			return driver;
		}
		else if (driverType.equalsIgnoreCase("Chrome")){
			System.setProperty("webdriver.chrome.driver", driverPath);
			WebDriver driver = new ChromeDriver();
			return driver;
		}
		else if (driverType.equalsIgnoreCase("Safari")){
			WebDriver driver = new SafariDriver();
			return driver;
		}
		/* Added ability to load android driver. Required to start selendroid server.  
		 *  Commented out this code as we are using the standalone server for now.
		 */
//		else if (driverType.equalsIgnoreCase("Android")) {
//			WebDriver driver = new RemoteWebDriver(DesiredCapabilities.android());
//		}
		else {
			System.out.println("Error: Invalid driverType specified.");
			return null;
		}

	}
	/*
	 * Method to read the properties file which has the test data.
	 */
	public Properties propertiesReader (){
		Properties prop = new Properties();
		InputStream input = null;
		try {
				input = new FileInputStream("test.properties");	
				// load a properties file
				prop.load(input);
				// get the property value and print it out
				System.out.println("URL under test is " + prop.getProperty("testUrl"));
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}
	
	/*
	 * Method to search for a particular item based on the brand name.
	 */
	
	public String searchItem (WebDriver driver, WebDriverWait wait, String item, String brand) {
		
		driver.findElement(By.cssSelector(".js-searchbar-input.js-header-instant-placeholder.searchbar-input.tt-input")).sendKeys(item);
		//driver.findElement(By.id("Passwd")).sendKeys("password");
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.findElement(By.cssSelector(".Modal-closeButton")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".searchbar-submit.js-searchbar-submit")));
		driver.findElement(By.cssSelector(".searchbar-submit.js-searchbar-submit")).click();
		WebElement product = driver.findElement(By.cssSelector("a[href*='" + brand + "']"));
		String prod_id = product.getAttribute("href").substring(product.getAttribute("href").lastIndexOf("/") +1);
		product.click(); 
		return prod_id;
	}
	
	/*
	 * Method to add the selected item to cart and view it
	 */
	
	public void addItemToCartAndView (WebDriver driver){
		driver.findElement(By.id("WMItemAddToCartBtn")).click();
		driver.findElement(By.id("PACViewCartBtn")).click();
	}
	
	/*
	 *  Method to get the number of items in the cart
	 */
	
	public String getItemCountOfCart (WebDriver driver) {
		String itemCount = driver.findElement(By.cssSelector("h3.cart-list-title span")).getText();
		return itemCount;
	}
	
	/*
	 * Method to search and validate a particular item in cart based in product Id
	 */
	
	public boolean checkItemInCart (WebDriver driver,String prodId) {
		List<WebElement>selected_Items = driver.findElements(By.id("CartItemInfo"));
		for (WebElement item : selected_Items ) {
				String item_id = item.getAttribute("data-us-item-id");
				if ( item_id == prodId)
					System.out.println(item_id + " " + prodId);
					return true;
		}
		return false;
	}
	
	public void proceedToCheckout (WebDriver driver){
		driver.get("https://www.walmart.com/checkout/");
	}
	
	/*
	 *  Method to select default ship to home and proceed to payment
	 */
	public void shipToHomeAndPayment (WebDriver driver, WebDriverWait wait) {
		wait.until(ExpectedConditions.elementToBeClickable(By.id("COAC1ShpOptContBtn")));
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.findElement(By.id("COAC1ShpOptContBtn")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("COAC2ShpAddrContBtn")));
		driver.findElement(By.id("COAC2ShpAddrContBtn")).click();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("COAC3PayReviewOrderBtn")));
	}
	
	/*
	 * Method to sign in at the payment page
	 */
	public void signInAtCheckout (WebDriver driver, WebDriverWait wait, String username, String password) {
	    wait.until(ExpectedConditions.presenceOfElementLocated(By.id("COAC0WelAccntEmail")));   /*examining the id for a login     
	    box*/
		driver.findElement(By.id("COAC0WelAccntEmail")).sendKeys(username);
		driver.findElement(By.id("COAC0WelAccntPswd")).click();
		driver.findElement(By.id("COAC0WelAccntPswd")).sendKeys(password);
		driver.findElement(By.id("COAC0WelAccntSignInBtn")).click();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	
	}
	
	/*
	 * Method to remove the single item from cart
	 */
	public void removeItemFromCart (WebDriver driver,WebDriverWait wait) {
		driver.get("https://www.walmart.com/cart/");
		driver.findElement(By.id("CartRemItemBtn")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("CartExistAccntSignInBtn")));
	}
	@Test
	public static void main(String[] args) {

		WalmartStore wrunner = new WalmartStore();
		Properties testData =  wrunner.propertiesReader();
		
		//initializing webdriver based on browser type
		String driverType = testData.getProperty("browser");
		try {
	
				WebDriver driver = wrunner.initializeDriver(driverType, testData.getProperty("driverPath"));	
				WebDriverWait wait = new WebDriverWait(driver,30);
				driver.get(testData.getProperty("testUrl"));
				driver.manage().window().maximize();			
				
				// Searching for the item and adding it to the cart
				String prod_id = wrunner.searchItem (driver, wait, testData.getProperty("item"),testData.getProperty("brand"));
				wrunner.addItemToCartAndView(driver);
				String itemCount = wrunner.getItemCountOfCart(driver);
				
				//Asserting the item count and checking the item based on product id
				Assert.assertEquals("1 item.", itemCount);		
				Assert.assertEquals( wrunner.checkItemInCart(driver, prod_id), true);
				System.out.println("Asserts for item count and checking product haved PASSED.");
				wrunner.proceedToCheckout(driver);
			    wrunner.signInAtCheckout(driver,wait, testData.getProperty("username"),testData.getProperty("password"));
			    wrunner.shipToHomeAndPayment(driver, wait);
			    
			    // Asserting that we are on the payment page based on the URL
				String paymentUrl = driver.getCurrentUrl();
				Assert.assertTrue(paymentUrl.contains("payment"));
				wrunner.removeItemFromCart(driver, wait);
				System.out.println("Assert for verifying payment page has PASSED");
				
				//Asserting the item count after removing from cart
				String itemCount1 = wrunner.getItemCountOfCart(driver);
				Assert.assertEquals(itemCount1, "0 items.");
				System.out.println("Assert for verifying empty cart has PASSED ");
				driver.get("https://www.walmart.com/account/logout");
				driver.close();
				System.out.println ("All tests have PASSED successfully.");
			}
		catch (Exception e){
			e.printStackTrace();
		}
	}

}
