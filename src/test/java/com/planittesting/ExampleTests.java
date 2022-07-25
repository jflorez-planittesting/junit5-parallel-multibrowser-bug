package com.planittesting;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

public class ExampleTests extends BaseTest {
    
    @ParameterizedTest(name = "{0} - exampleMultiBrowser")
	@ArgumentsSource(BrowsersArgumentsProvider.class)
	@WithBrowsers({WithBrowsers.Browser.CHROME, WithBrowsers.Browser.FIREFOX})
    void exampleMultiBrowser(WithBrowsers.Browser browser) {
        assertTrue(driver.getClass().getSimpleName().toLowerCase().contains(browser.toString().toLowerCase()));
    }

}
