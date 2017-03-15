import static org.junit.Assert.*;

import java.util.logging.*;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.logging.*;

import org.openqa.selenium.remote.*;

/* 
   Tests the CS1632 D3 Webapp Hello Page. 
   As users, we want to be able to access all of the app's pages
   without difficulty, meaning we want a working navigational menu
   as listed in the requirements. We want to be able to access this page with
   trailing values to see hello from the trailing value, or with no trailing values
   to see hello from prof. Laboon. 
*/

public class HelloPageTests 
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
	
	// Start at the Hello page, since that's what we're testing here
	@Before
	public void setUp() throws Exception
	{
		driver.get("https://cs1632ex.herokuapp.com/hello");
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
			fail("The hello page is missing one or more link(s) in its navigational menu!");
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
			fail("This test could not succeed because the home link is missing on the hello page!");
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
			fail("This test could not succeed because the factorial link is missing on the hello page!");
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
			fail("This test could not succeed because the fibonacci link is missing on the hello page!");
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
			fail("This test could not succeed because the hello link is missing on the hello page!");
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
			fail("This test could not succeed because the cathy link is missing on the hello page!");
		}
	}
	
	// This one is fairly simple. According to requirement 6, if we access the hello page with no trailing values
	// in the URL, we should see a message that reads "Hello CS1632, from Prof. Laboon!". So, we test that
	// this is indeed the case. The @Before method, which runs before every class, starts us off by accessing
	// the hello page with no trailing values. The rest is easy - just check the body for the appropriate message. 
	// It's important to note that we're still going to use the .contains method for our check, rather than using
	// .equals. This is because the requirements specifically state that we simply should see the message. It doesn't
	// tell us that we shouldn't see anything else. 
	
	// Testing requirement 6
	@Test
	public void testHelloNoTrail()
	{
		String body = driver.findElement(By.tagName("body")).getText();
		String trimmed = body.replaceAll("[\\t\\n\\x0B\\f\\r]", " ");
		assertTrue(trimmed.contains("Hello CS1632, from Prof. Laboon!"));
	}
	
	// Now we'll try this with a regular usage case: one trailing value. We'll go ahead and use the 
	// example provided in requirement 7 and access the hello page with the trailing value "/jazzy".
	// Once again, we're not worried about what else is on the page. The requirements specifically tell
	// us that we just need to see the message, not that we shouldn't see anything else. So, again, we 
	// use the .contains() method. 
	
	// Testing requirement 7
	@Test
	public void testHelloOneTrail()
	{
		driver.get("https://cs1632ex.herokuapp.com/hello/jazzy");
		
		String body = driver.findElement(By.tagName("body")).getText();
		String trimmed = body.replaceAll("[\\t\\n\\x0B\\f\\r]", " ");
		assertTrue(trimmed.contains("Hello CS1632, from jazzy!"));
	}
	
	// Now we're going to try out an edge case. Requirement 7 states that 'this shall work for ALL input values.'
	// We won't bother trying things like numbers since that would work the same way as a single string anyway, since
	// URLs basically interpret everything as strings. However, there is something we can try. ALL input 
	// values means it should work for absolutely everything. That includes /'s, which would normally be interpreted as
	// other pages. So, since this is the only thing that really might break this requirement, we'll try it. We 
	// can call this an edge case since this is, in a way, the only border value we have, as it is the only thing
	// which poses a threat to the requirement. 
	//
	// NOTE: We expect everything after the /hello to be considered a single string. So, if there's a slash, that 
	// would be included just like anything else. So, if we access .../hello/jazz/hands, we'd expect a message 
	// "Hello CS1632, from jazz/hands!"
	
	// Testing requirement 7
	@Test
	public void testHelloMultiTrail()
	{
		driver.get("https://cs1632ex.herokuapp.com/hello/jazz/hands");
		
		String body = driver.findElement(By.tagName("body")).getText();
		String trimmed = body.replaceAll("[\\t\\n\\x0B\\f\\r]", " ");
		assertTrue(trimmed.contains("Hello CS1632, from jazz/hands!"));
	}
}
