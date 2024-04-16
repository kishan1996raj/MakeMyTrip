package demo;

import java.security.Key;
import java.time.Duration;


import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;

import java.util.List;
import java.util.logging.Level;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.WebDriverWait;


public class TestCases {
    ChromeDriver driver;

    public TestCases() {
        System.out.println("Constructor: TestCases");

        WebDriverManager.chromedriver().timeout(30).setup();
        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        // Set log level and type
        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");

        // Set path for log file
        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "chromedriver.log");

        driver = new ChromeDriver(options);

        // Set browser to maximize and wait
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

    }

    public void endTest() {
        System.out.println("End Test: TestCases");
        driver.close();
        driver.quit();

    }

    public boolean selectDateFromCalendarForAirlines(String DD, String MMMYYYY) throws InterruptedException {
        WebElement calenderDropDown = driver.findElement(By.xpath("//span[text()='Departure']"));
        boolean status = false;
        while (!status) {
            WebElement month = driver.findElement(By.xpath("//div[@class='DayPicker-Caption']"));
            String date = month.getText();
            String monthUI = date.substring(0,3);
            String yearUI = date.substring(date.length()-4,date.length());
            String modifiedDate = monthUI+yearUI;
            System.out.println(modifiedDate);
            if (MMMYYYY.equals(modifiedDate)) {
                List<WebElement> dates = driver.findElements(By.xpath("(//div[@class='DayPicker-Month'][1])//div[@aria-disabled='false']/div[@class='dateInnerCell']/p[1]"));
                for (WebElement presentDate : dates){
                    try {
                        if (DD.equalsIgnoreCase(presentDate.getText())) {
                            presentDate.click();
                            status = true;
                           break; // Exit the loop if the date is found and clicked
                        }
                    } catch (StaleElementReferenceException e) {
                        // Handle StaleElementReferenceException
                        System.out.println("Element is stale. Trying again...");
                        if (DD.equalsIgnoreCase(presentDate.getText())) {
                            presentDate.click();
                            status = true;
                             // Exit the loop if the date is found and clicked
                        }
                    }
                }
                break;
            }
            driver.findElement(By.xpath("//span[@aria-label='Next Month']")).click();
        }
        return status;
    }
    public boolean selectDateFromCalendarForTrain(String DD, String MMMYYYY) throws InterruptedException {
        WebElement calenderDropDown = driver.findElement(By.xpath("//span[text()='Travel Date']"));
        JavascriptExecutor js =(JavascriptExecutor) driver;
        boolean status = false;
        while (!status) {
            WebElement month = driver.findElement(By.xpath("//div[@class='DayPicker-Caption']"));
            String date = month.getText();
            String monthUI = date.substring(0,3);
            String yearUI = date.substring(date.length()-4,date.length());
            String modifiedDate = monthUI+yearUI;
            System.out.println(modifiedDate);
            if (MMMYYYY.equals(modifiedDate)) {
                List<WebElement> dates = driver.findElements(By.xpath("//div[@class='DayPicker-Month'][1]/div[@class='DayPicker-Body']//div[@aria-disabled='false']"));
                for (WebElement presentDate : dates){
                    try {
                        if (DD.equalsIgnoreCase(presentDate.getText())) {
                           js.executeScript("arguments[0].click();",presentDate);
                            status = true;
                            break; // Exit the loop if the date is found and clicked
                        }
                    } catch (StaleElementReferenceException e) {
                        // Handle StaleElementReferenceException
                        System.out.println("Element is stale. Trying again...");
                        if (DD.equalsIgnoreCase(presentDate.getText())) {
                            js.executeScript("arguments[0].click();",presentDate);
                            status = true;
                            // Exit the loop if the date is found and clicked
                        }
                    }
                }
                break;
            }
            driver.findElement(By.xpath("//span[@aria-label='Next Month']")).click();
        }
        return status;
    }

    public void testCase01() {
        System.out.println("Start Test case: testCase01");
        driver.get("https://www.makemytrip.com/");
        String url = driver.getCurrentUrl();
        if (url.contains("makemytrip")) {
            System.out.println("Successfully navigated to make my trip");
        } else {
            System.out.println("Testcase 01 failed");
        }
        System.out.println("end Test case: testCase01");

    }

    public void testCases02() throws InterruptedException {
        System.out.println("Start Test case: testCase02");
        driver.get("https://www.makemytrip.com/");
        driver.findElement(By.xpath("//input[@id='fromCity']")).click();
        WebElement fromINPUT = driver.findElement(By.xpath("//input[@placeholder='From']"));
        fromINPUT.sendKeys("blr");
        WebElement suggestion1 = driver.findElement(By.xpath("//ul[@role='listbox']//span[contains(text(),'Bengaluru')]"));
        suggestion1.click();
        driver.findElement(By.xpath("//input[@id='toCity']")).click();
        WebElement toINPUT = driver.findElement(By.xpath("//input[@placeholder='To']"));
        toINPUT.sendKeys("del");
        WebElement suggestion2 = driver.findElement(By.xpath("//ul[@role='listbox']//span[contains(text(),'New Delhi')]"));
        suggestion2.click();
        boolean status = selectDateFromCalendarForAirlines("21", "Dec2024");
        if (status) {
            WebElement searchButton = driver.findElement(By.xpath("//a[text()='Search']"));
            searchButton.click();
            Thread.sleep(5000);
            WebElement firstEntry = driver.findElement(By.xpath("(//div[@class='listingCard  appendBottom5'])[1]"));
            WebElement fromCity = firstEntry.findElement(By.xpath("(//p[@class='blackText'])[1]"));
            String opFromCity = fromCity.getText();
            WebElement toCity = firstEntry.findElement(By.xpath("(//p[@class='blackText'])[2]"));
            String opToCity = toCity.getText();
            if (opFromCity.equalsIgnoreCase("Bengaluru") && opToCity.equalsIgnoreCase("New Delhi")) {
                Thread.sleep(5000);
                driver.findElement(By.xpath("//button[text()='OKAY, GOT IT!']")).click();
                WebElement price = driver.findElement(By.xpath("(//div[contains(@class,'clusterViewPrice')])[1]"));
                String opPrice = price.getText();
                System.out.println("The price for the journey is : " + opPrice);
                System.out.println("Testcase 02 passed");
            }
        }else System.out.println("Testcase 02 failed");


    }

    public void testCase03() throws InterruptedException {
        System.out.println("Starting Test Case 03");
        driver.get("https://www.makemytrip.com/");
        driver.findElement(By.xpath("//a[@href='https://www.makemytrip.com/railways/']")).click();
        driver.findElement(By.xpath("//input[@id='fromCity']")).click();
        WebElement fromINPUT = driver.findElement(By.xpath("//input[@placeholder='From']"));
        fromINPUT.sendKeys("ypr");
        WebElement suggestion1 = driver.findElement(By.xpath("//ul[@role='listbox']//span[contains(text(),'Bengaluru')]"));
        suggestion1.click();
        WebElement toCity = driver.findElement(By.xpath("//input[@id='toCity']"));
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].click();",toCity);
        WebElement toINPUT = driver.findElement(By.xpath("//input[@placeholder='To']"));
        toINPUT.sendKeys("ndls");
        WebElement suggestion2 = driver.findElement(By.xpath("//ul[@role='listbox']//span[contains(text(),'Delhi')]"));
        suggestion2.click();
        boolean status = selectDateFromCalendarForTrain("21", "Jun2024");
        if(status) {
            WebElement travelClass =driver.findElement(By.xpath("//input[@id='travelClass']"));
            js.executeScript("arguments[0].click();",travelClass);
            driver.findElement(By.xpath("//ul[@class='travelForPopup']/li[text()='Third AC']")).click();
            WebElement searchButton = driver.findElement(By.xpath("//a[text()='Search']"));
            searchButton.click();
            Thread.sleep(5000);
            String text = driver.findElement(By.xpath("//div[@class='train-list']/div[1]/div[2]/div[1]//div[contains(@class,'ticket-price')]")).getText();
            System.out.println(text);
            System.out.println("Test case 03 passed");
        }else System.out.println("Test case failed");

    }
    public void testCase04() throws InterruptedException {
        System.out.println("Starting Test Case 03");
        driver.get("https://www.makemytrip.com/");
        driver.findElement(By.xpath("//a[@href='https://www.makemytrip.com/bus-tickets/']")).click();
        driver.findElement(By.xpath("//input[@id='fromCity']")).click();
        WebElement fromINPUT = driver.findElement(By.xpath("//input[@placeholder='From']"));
        fromINPUT.sendKeys("bangl");
        WebElement suggestion1 = driver.findElement(By.xpath("//ul[@role='listbox']//span[contains(text(),'Bangalore')]"));
        suggestion1.click();
        WebElement toCity = driver.findElement(By.xpath("//input[@id='toCity']"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", toCity);
        WebElement toINPUT = driver.findElement(By.xpath("//input[@placeholder='To']"));
        toINPUT.sendKeys("kathmandu");
        WebElement suggestion2 = driver.findElement(By.xpath("//ul[@role='listbox']//span[contains(text(),'Kathmandu')]"));
        suggestion2.click();
        boolean status = selectDateFromCalendarForTrain("29", "May2024");
        System.out.println(status);
        if (status){
            WebElement searchButton = driver.findElement(By.xpath("//button[text()='Search']"));
            searchButton.click();
            Thread.sleep(5000);
            String msg = "No buses found for 29 May";
            status = driver.findElement(By.xpath("//span[contains(text(),'No buses found for 29 May')][1]")).isDisplayed();
            String msgDisp = driver.findElement(By.xpath("//span[contains(text(),'No buses found for 29 May')][1]")).getText();
            if(msg.equals(msgDisp)){
                System.out.println("No Buses found for 29th of next Month");
                System.out.println("TestCase04 passed");
            } else System.out.println("Test case 04 fialed");

        }
    }
}
