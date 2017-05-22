package rozetka;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;
import static org.junit.Assert.*;
import java.util.concurrent.TimeUnit;

public class withSelenium {
	WebDriver driver;
	String baseUrl;
	String bankUrl;

	@BeforeClass
	public void setUp() throws Exception {
		//windows
		System.setProperty("webdriver.chrome.driver", "libs\\chromedriver.exe");
		//unix
		//System.setProperty("webdriver.chrome.driver", "libs/chromedriver");
		
		baseUrl = "http://rozetka.com.ua/";
		bankUrl = "https://bank.gov.ua/control/uk/curmetal/detail/currency?period=daily";
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test(priority = 1)
	public void searchProductByArticle() {
		driver.get(baseUrl);
		searcProduct("5000299223017");
		assertEquals("Ром Captain Morgan Spiced Gold 0.7 л 35% (5000299223017) ", driver.findElement(By.cssSelector(".detail-title")).getText());
	}

	@Test(dependsOnMethods = "searchProductByArticle")
	public void countComments() {
		assertEquals("49", driver.findElement(By.xpath(".//*[@id='tabs']/li[4]/a/span")).getText());
	}

	@Test(dependsOnMethods = "searchProductByArticle")
	public void countRating() {
		assertEquals("width: 100%;", driver.findElement(By.cssSelector(".g-rating-stars-i-medium")).getAttribute("style"));
	}

	@AfterClass
	public void tearDown() throws Exception {
		System.out.println("Price in USD = " + priceUSD(Double.parseDouble(driver.findElement(By.id("price_label")).getText())));
		driver.quit();
	}

	private void searcProduct(String article) {
		driver.findElement(By.name("text")).clear();
		driver.findElement(By.name("text")).sendKeys(article);
		driver.findElement(By.name("rz-search-button")).click();
	}

	private double priceUSD(double priceUAH)
	{
		driver.get(bankUrl);
		return (priceUAH*100) / Double.parseDouble(driver.findElement(By.xpath("html/body/table/tbody/tr/td[2]/table/tbody/tr/td[2]/div[4]/table[4]/tbody/tr[9]/td[5]")).getText());
	}
}
