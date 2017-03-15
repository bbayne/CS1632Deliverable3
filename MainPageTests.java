import static org.junit.Assert.*;

import java.util.logging.*;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.logging.*;

import org.openqa.selenium.remote.*;

/* 
   Tests the CS1632 D3 Webapp Main Page. 
   As users, we want to be able to access all of the app's pages
   without difficulty, meaning we want a working navigational menu
   as listed in the requirements. We want to see the 'Welcome' 
   message when we access the page, and we want to know that this
   is the CS1632 QA class's webapp. 
*/

public class MainPageTests 
{
	static WebDriver driver;
	
	// Set up our driver and turn off logging so that we don't see useless information logs 
	// You can change this if you feel the need, but these logs are not necessary for normal
	// usage. 
	@BeforeClass
    public static void setUpDriver() 
	{
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		driver = new HtmlUnitDriver(); 
	}
	
	// Start at the home page, since that's what we're testing here.
	@Before
	public void setUp() throws Exception
	{
		driver.get("https://cs1632ex.herokuapp.com");
	}

	// Each page should have a title, and that title should be CS1632 D3. This isn't 
	// explicitly a requirement, but it is important for a page to have a title. 
	@Test
	public void testShowsCorrectTitle() 
	{	
		String title = driver.getTitle();
		assertEquals(title, "CS1632 D3");
	}
	
	// We need to test that the proper welcome message is displayed according to requirement 1. 
	// Get the text in the body of the page and check if it contains the message.
	// We first need to cut out any white space characters (carriage returns or other nuisances) 
	// since the requirement doesn't state that the message needs to be formatted in any given way.
	// We use a regex to eliminate the unnecessary characters and then just check to make sure that 
	// the body of the page contains the messages we want. 
	
	// Testing Requirement 1
	@Test
	public void testHomePageTextDisplay()
	{
		String body = driver.findElement(By.tagName("body")).getText();
		String trimmed = body.replaceAll("[\\t\\n\\x0B\\f\\r]", " ");
		assertTrue(trimmed.contains("Welcome, friend, to a land of pure calculation") && trimmed.contains("Used for CS1632 Software Quality Assurance, taught by Bill Laboon"));
	}
	
	// Each page should have a navigation menu with 5 different links.
	// CS1632 D3 Home , Factorial , Fibonacci , Hello , and Cathedral Pics
	// So, when we view the header, we should see that it contains these links
	// We have this test because we want to actually make sure that all of the
	// pages have the right links. This separates that requirement from the ones below 
	// which test whether the links navigate properly, and should allow the user
	// to see immediately if a link is missing on a given page. 
	
	// Testing requirement 2 
	@Test
	public void testHasCorrectHeaderLinksHomePage() 
	{
		try 
		{
			driver.findElement(By.linkText("CS1632 D3 Home"));
			driver.findElement(By.linkText("Factorial"));
			driver.findElement(By.linkText("Fibonacci"));
			driver.findElement(By.linkText("Hello"));
			driver.findElement(By.linkText("Cathedral Pics"));
		} 
		catch (NoSuchElementException nseex) 
		{
			fail("The home page is missing one or more link(s) in its navigational menu!");
		}
	}
	
	/*
		Now we should check to make sure the header links actually go to the right places. 
		These are not the most localized tests - we still have to 'findElement' the links.
		However, since we have the test above checking that the links actually exist,
		a user will be able to tell, if a test fails, whether the problem is that the
		link didn't exist or that the problem is that they went to the wrong location.
		
		If this test fails but the one above does not, the user will know that 
		the problem was that the link went somewhere it shouldn't have. The user can
		also check the failure text of these tests to determine why, exactly, the failure occurred. 
	*/
	
	// Test that the home link navigates to the home page 
	@Test
	public void testHomeLinkHomePage() 
	{
		try 
		{
			String home = driver.findElement(By.linkText("CS1632 D3 Home")).getAttribute("href");
			assertEquals(home, "https://cs1632ex.herokuapp.com/");
		} 
		catch (NoSuchElementException nseex) 
		{
			fail("This test could not succeed because the home link is missing on the home page!");
		}
	}
	
	// Test that the factorial link navigates to the factorial page 
	@Test
	public void testFactorialLink() 
	{
		try 
		{
			String fact = driver.findElement(By.linkText("Factorial")).getAttribute("href");
			assertEquals(fact, "https://cs1632ex.herokuapp.com/fact");
		} 
		catch (NoSuchElementException nseex) 
		{
			fail("This test could not succeed because the factorial link is missing on the home page!");
		}
	}
	
	// Test that the fibonacci link navigates to the fibonacci page 
	@Test
	public void testFibonacciLink() 
	{
		try 
		{
			String fib = driver.findElement(By.linkText("Fibonacci")).getAttribute("href");
			assertEquals(fib, "https://cs1632ex.herokuapp.com/fib");
		} 
		catch (NoSuchElementException nseex) 
		{
			fail("This test could not succeed because the fibonacci link is missing on the home page!");
		}
	}
	
	// Test that the hello link navigates to the hello page 
	@Test
	public void testHelloLink() 
	{
		try 
		{
			String hello = driver.findElement(By.linkText("Hello")).getAttribute("href");
			assertEquals(hello, "https://cs1632ex.herokuapp.com/hello");
		} 
		catch (NoSuchElementException nseex) 
		{
			fail("This test could not succeed because the hello link is missing on the home page!");
		}
	}
	
	// Test that the cathy link navigates to the cathedral page 
	@Test
	public void testCathyLink() 
	{
		try 
		{
			String cathy = driver.findElement(By.linkText("Cathedral Pics")).getAttribute("href");
			assertEquals(cathy, "https://cs1632ex.herokuapp.com/cathy");
		} 
		catch (NoSuchElementException nseex) 
		{
			fail("This test could not succeed because the cathy link is missing on the home page!");
		}
	}
}
