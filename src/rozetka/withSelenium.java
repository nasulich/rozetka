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

	@BeforeClass
	public void setUp() throws Exception {
		//windows
		System.setProperty("webdriver.chrome.driver", "libs\\chromedriver.exe");
		//unix
		//System.setProperty("webdriver.chrome.driver", "libs/chromedriver");
		
		baseUrl = "http://rozetka.com.ua/";
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
		driver.quit();
	}

	private void searcProduct(String article) {
		driver.findElement(By.name("text")).clear();
		driver.findElement(By.name("text")).sendKeys(article);
		driver.findElement(By.name("rz-search-button")).click();
	}
}
