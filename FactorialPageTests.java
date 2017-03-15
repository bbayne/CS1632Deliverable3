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
   Tests the CS1632 D3 Webapp Factorial Page. 
   As users, we want to be able to access all of the app's pages
   without difficulty, meaning we want a working navigational menu
   as listed in the requirements. We want to be able to calculate factorials 
   for values between 1 and 100 inclusive, and we don't want the website to 
   crash if we input an incorrect value. 
*/

public class FactorialPageTests 
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
	
	// Start at the Factorial page, since that's what we're testing here
	@Before
	public void setUp() throws Exception
	{
		driver.get("https://cs1632ex.herokuapp.com/fact");
	}

	// Each page should have a title, and that title should be CS1632 D3
	@Test
	public void testShowsCorrectTitle() 
	{	
		String title = driver.getTitle();
		assertEquals(title, "CS1632 D3");
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
			fail("The Factorial page is missing one or more link(s) in its navigational menu!");
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
			fail("This test could not succeed because the home link is missing on the factorial page!");
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
			fail("This test could not succeed because the factorial link is missing on the factorial page!");
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
			fail("This test could not succeed because the fibonacci link is missing on the factorial page!");
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
			fail("This test could not succeed because the hello link is missing on the factorial page!");
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
			fail("This test could not succeed because the cathy link is missing on the factorial page!");
		}
	}
	
	// Now to test the basic functionality of the factorial page. We'll start with the example 
	// provided in the requirements (5). This is a basic check to ensure that the functionality 
	// is there. It will also fail, however, if the textbox or form do not exist on the page. 
	// This is an implicit requirement. If either does not exist, the test will notify the user
	// in the failure message. 
	
	// Testing requirement 3
	@Test
	public void testBasicFact()
	{
		try
		{
			WebElement factorialForm = driver.findElement(By.tagName("form"));
			WebElement textBox = factorialForm.findElement(By.name("value"));
			
			textBox.sendKeys("5");
			factorialForm.submit();
			
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			String body = driver.findElement(By.tagName("body")).getText();
			String trimmed = body.replaceAll("[\\t\\n\\x0B\\f\\r]", " ");
			assertTrue(trimmed.contains("Factorial of 5 is 120!"));
		}
		catch (NoSuchElementException nseex)
		{
			fail("Either there was no form, or the form that was on the page did not contain the 'value' textbox!");
		}
	}
	
	// Now it's time to get a little more interesting. We've made sure that the basic functionality is 
	// there for the factorial requirement, but now we're going to try some edge cases. 
	// Obviously, since JUnit won't run these in any particular order, the same failure case applies here 
	// for when/if the text box or form does not exist, and the same message will be returned in that case. 
	
	// Testing requirement 3 
	@Test
	public void testEdgeFactLower()
	{
		try
		{
			WebElement factorialForm = driver.findElement(By.tagName("form"));
			WebElement textBox = factorialForm.findElement(By.name("value"));
			
			textBox.sendKeys("1");
			factorialForm.submit();
			
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			String body = driver.findElement(By.tagName("body")).getText();
			String trimmed = body.replaceAll("[\\t\\n\\x0B\\f\\r]", " ");
			assertTrue(trimmed.contains("Factorial of 1 is 1!"));
		}
		catch (NoSuchElementException nseex)
		{
			fail("Either there was no form, or the form that was on the page did not contain the 'value' textbox!");
		}
	}
	
	// Another edge case, this time testing the upper limit for our factorial input (100)
	
	// Testing requirement 3 
	@Test
	public void testEdgeFactUpper()
	{
		try
		{
			WebElement factorialForm = driver.findElement(By.tagName("form"));
			WebElement textBox = factorialForm.findElement(By.name("value"));
			
			textBox.sendKeys("100");
			factorialForm.submit();
			
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			String body = driver.findElement(By.tagName("body")).getText();
			String trimmed = body.replaceAll("[\\t\\n\\x0B\\f\\r]", " ");
			assertTrue(trimmed.contains("Factorial of 100 is 93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000!"));
		}
		catch (NoSuchElementException nseex)
		{
			fail("Either there was no form, or the form that was on the page did not contain the 'value' textbox!");
		}
	}
	
	// Now we need to test how the factorial page handles incorrect inputs. 
	// We'll start with the lower edge case (0)
	
	// Testing requirement 5
	@Test
	public void testEdgeIncorrectLower()
	{
		try
		{
			WebElement factorialForm = driver.findElement(By.tagName("form"));
			WebElement textBox = factorialForm.findElement(By.name("value"));
			
			textBox.sendKeys("0");
			factorialForm.submit();
			
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			String body = driver.findElement(By.tagName("body")).getText();
			String trimmed = body.replaceAll("[\\t\\n\\x0B\\f\\r]", " ");
			assertTrue(trimmed.contains("Factorial of 0 is 1!"));
		}
		catch (NoSuchElementException nseex)
		{
			fail("Either there was no form, or the form that was on the page did not contain the 'value' textbox!");
		}
	}
	
	// Now we'll try the upper edge case (101) for incorrect inputs. 
	
	// Testing requirement 5
	@Test
	public void testEdgeIncorrectUpper()
	{
		try
		{
			WebElement factorialForm = driver.findElement(By.tagName("form"));
			WebElement textBox = factorialForm.findElement(By.name("value"));
			
			textBox.sendKeys("101");
			factorialForm.submit();
			
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			String body = driver.findElement(By.tagName("body")).getText();
			String trimmed = body.replaceAll("[\\t\\n\\x0B\\f\\r]", " ");
			assertTrue(trimmed.contains("Factorial of 101 is 1!"));
		}
		catch (NoSuchElementException nseex)
		{
			fail("Either there was no form, or the form that was on the page did not contain the 'value' textbox!");
		}
	}
	
	// With the edge cases out of the way, we should test a corner case or two as well.
	// We'll start with a negative value. 
	
	// Testing requirement 5
	@Test
	public void testCornerFactLower()
	{
		try
		{
			WebElement factorialForm = driver.findElement(By.tagName("form"));
			WebElement textBox = factorialForm.findElement(By.name("value"));
			
			textBox.sendKeys("-234");
			factorialForm.submit();
			
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			String body = driver.findElement(By.tagName("body")).getText();
			String trimmed = body.replaceAll("[\\t\\n\\x0B\\f\\r]", " ");
			assertTrue(trimmed.contains("Factorial of -234 is 1!"));
		}
		catch (NoSuchElementException nseex)
		{
			fail("Either there was no form, or the form that was on the page did not contain the 'value' textbox!");
		}
	}
	
	// Finally, we can wrap this up with one more corner case. This time, a massive number.
	// We'll use the factorial of 100, way beyond the max integer value for any language and far 
	// more numbers than anyone would ever try to put into a textbox, making it a good corner. 
	
	// Testing requirement 5
	@Test
	public void testCornerFactUpper()
	{
		try
		{
			WebElement factorialForm = driver.findElement(By.tagName("form"));
			WebElement textBox = factorialForm.findElement(By.name("value"));
			
			textBox.sendKeys("93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000");
			factorialForm.submit();
			
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			String body = driver.findElement(By.tagName("body")).getText();
			String trimmed = body.replaceAll("[\\t\\n\\x0B\\f\\r]", " ");
			assertTrue(trimmed.contains("Factorial of 93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000 is 1!"));
		}
		catch (NoSuchElementException nseex)
		{
			fail("Either there was no form, or the form that was on the page did not contain the 'value' textbox!");
		}
	}
	
	// Now we'll test some even weirder corner cases. Requirement number 5 states that 'if a user enters an invalid value
	// OF ANY KIND, they shall be informed that the value is 1.' We're going to take note that ANY invalid value should 
	// return an answer of 1. That means that every type of input, not just numbers, should return that value. So, we'll
	// test a non-numeric value - a letter.
	
	// Testing requirement 5
	@Test
	public void testCornerFactLetters()
	{
		try
		{
			WebElement factorialForm = driver.findElement(By.tagName("form"));
			WebElement textBox = factorialForm.findElement(By.name("value"));
			
			textBox.sendKeys("a");
			factorialForm.submit();
			
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			String body = driver.findElement(By.tagName("body")).getText();
			String trimmed = body.replaceAll("[\\t\\n\\x0B\\f\\r]", " ");
			assertTrue(trimmed.contains("Factorial of a is 1!"));
		}
		catch (NoSuchElementException nseex)
		{
			fail("Either there was no form, or the form that was on the page did not contain the 'value' textbox!");
		}
	}
	
	// The last of our corner cases, this time we're going to test what happens when the user doesn't input anything
	// at all. This is actually a really common corner case, as users can easily hit the 'enter' key accidentally
	// when using a textbox. What we would expect here is just like everything else - the app should return a message
	// that "the factorial of  is 1" by our formatting (this is, for readability's sake, equivalent to 
	// "the factorial of "" is 1", where "" just represents an empty string).
	
	// Testing requirement 5
	@Test
	public void testCornerFactEmpty()
	{
		try
		{
			WebElement factorialForm = driver.findElement(By.tagName("form"));
			factorialForm.submit();
			
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			String body = driver.findElement(By.tagName("body")).getText();
			String trimmed = body.replaceAll("[\\t\\n\\x0B\\f\\r]", " ");
			assertTrue(trimmed.contains("Factorial of  is 1!"));
		}
		catch (NoSuchElementException nseex)
		{
			fail("Either there was no form, or the form that was on the page did not contain the 'value' textbox!");
		}
	}
}
