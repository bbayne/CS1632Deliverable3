import static org.junit.Assert.*;

import java.util.List;
import java.util.logging.*;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.logging.*;

import org.openqa.selenium.remote.*;

/* 
   Tests the CS1632 D3 Webapp Cathedral Page. 
   As users, we want to be able to access all of the app's pages
   without difficulty, meaning we want a working navigational menu
   as listed in the requirements. We expect on this page to see three images of the 
   Cathedral of Learning in a numbered list.  
*/

public class CathyPageTests 
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
	
	// Start at the Cathedral page, since that's what we're testing here
	@Before
	public void setUp() throws Exception
	{
		driver.get("https://cs1632ex.herokuapp.com/cathy");
	}

	// Each page should have a title, and that title should be CS1632 D3
	@Test
	public void testShowsCorrectTitle() 
	{	
		String title = driver.getTitle();
		assertEquals(title, "CS1632 D3");
	}
	
	// Each page should have a navigation menu with 5 different links.
	// CS1632 D3 Home , Fibonacci , Fibonacci , Hello , and Cathedral Pics
	// So, when we view the header, we should see that it contains these links
	// We have this test because we want to actually make sure that all of the
	// pages have the right links. This separates that requirement from the ones below 
	// which test whether the links navigate properly, and should allow the user
	// to see immediately if a link is missing on a given page. 
	
	// Testing requirement 2 
	@Test
	public void testHasCorrectHeaderLinks() 
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
			fail("The cathedral page is missing one or more link(s) in its navigational menu!");
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
	public void testHomeLink() 
	{
		try 
		{
			String home = driver.findElement(By.linkText("CS1632 D3 Home")).getAttribute("href");
			assertEquals(home, "https://cs1632ex.herokuapp.com/");
		} 
		catch (NoSuchElementException nseex) 
		{
			fail("This test could not succeed because the home link is missing on the cathy page!");
		}
	}
	
	// Test that the Fibonacci link navigates to the Fibonacci page 
	@Test
	public void testFactorialLink() 
	{
		try 
		{
			String Fib = driver.findElement(By.linkText("Factorial")).getAttribute("href");
			assertEquals(Fib, "https://cs1632ex.herokuapp.com/fact");
		} 
		catch (NoSuchElementException nseex) 
		{
			fail("This test could not succeed because the factorial link is missing on the cathy page!");
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
			fail("This test could not succeed because the fibonacci link is missing on the cathy page!");
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
			fail("This test could not succeed because the hello link is missing on the cathy page!");
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
			fail("This test could not succeed because the cathy link is missing on the cathy page!");
		}
	}
	
	// First thing to do is check if we have images at all, and specifically three at that. 
	// So, we can access all image objects on the page and see if we have three. 
	@Test
	public void testCathyImageNumber()
	{
		try
		{
			List<WebElement> images = driver.findElements(By.tagName("img"));
			assertEquals(images.size(), 3);
		}
		catch (NoSuchElementException nseex)
		{
			fail("No images found!");
		}
	}
	
	// We don't exactly have computer vision to help us test things here, so there's no way 
	// for us to be absolutely certain that the images we're seeing are images of the cathedral.
	// However, what we can do is check their alt attribute to see if they are at least being listed
	// as images of the cathedral. After all, the alt attributes for each image should indicate 
	// what they are images of - that is the purpose of the alt attribute. 
	@Test
	public void testCathyImageSubject()
	{
		try
		{
			List<WebElement> images = driver.findElements(By.tagName("img"));
			for (WebElement e : images)
			{
				String altTag = e.getAttribute("alt");
				String lowered = altTag.toLowerCase();
				assertTrue(lowered.contains("cathedral"));
			}
		}
		catch (NoSuchElementException nseex)
		{
			fail("No images found!");
		}
	}
	
	// We need everything to be in a numbered list. The simplest way to check for this is just
	// to check if we have a numbered list element (the <ol> tag). But we should also check to see 
	// if the images are actually in that numbered list. The simplest way to do THAT would be to 
	// gather up all of the <li> tags and make sure that they are all images. 
	// So, make sure we have an <ol> tag, and then make sure we have <li> tags,
	// and that those <li> tags contain images. That will tell us that we have
	// a numbered list of images. The test above makes sure those images are of cathy, and the one above
	// that makes sure that there is no more than 3.
	@Test 
	public void testForNumberedList()
	{
		try
		{
			WebElement list = driver.findElement(By.tagName("ol"));
			List<WebElement> listItems = list.findElements(By.tagName("li"));
			
			try
			{
				for (WebElement e : listItems)
					e.findElement(By.tagName("img"));
			}
			catch (NoSuchElementException nseex)
			{
				fail("We have a numbered list, but it doesn't contain just images!");
			}
		}
		catch (NoSuchElementException nseex)
		{
			fail("No numbered list element found on the page!");
		}
	}
}
