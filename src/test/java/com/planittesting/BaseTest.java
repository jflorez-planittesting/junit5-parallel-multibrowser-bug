package com.planittesting;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

@ExtendWith(BrowserParameterResolver.class)
@Execution(ExecutionMode.CONCURRENT)
public abstract class BaseTest {
	protected WebDriver driver;

	@BeforeEach
	public void setupTest(WithBrowsers.Browser browser) throws Exception {
		// browser = dotenv.get("SELENIUM_BROWSER");
		var wait = 3; //Integer.parseInt(dotenv.get("SELENIUM_WAIT"));
		var url = "https://www.google.com"; //dotenv.get("SELENIUM_URL");
		
		this.driver = switch (browser) {
			case CHROME -> new ChromeDriver();
			case FIREFOX -> new FirefoxDriver();
			default -> throw new RuntimeException(browser+" not supported yet");
		};
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(wait));
		driver.manage().window().maximize();
		driver.navigate().to(url);
	}

	
	@AfterEach
	public void shutdownTest(WithBrowsers.Browser browser) {
		driver.quit();
	}
}
