package helpers.webbrowser;

import org.openqa.selenium.support.events.EventFiringWebDriver;

public class WebBrowserDriver extends EventFiringWebDriver {

    public WebBrowserDriver() {
        super(WebDriverFactory.webDriver());
    }
}