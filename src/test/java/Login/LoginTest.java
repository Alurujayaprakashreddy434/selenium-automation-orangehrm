package Login;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import org.apache.commons.io.FileUtils;

public class LoginTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.manage().window().maximize();
    }

    @Test
    public void loginAndVerifyAdminPage() throws IOException, InterruptedException {
        driver.get("https://opensource-demo.orangehrmlive.com/");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys("Admin");
        driver.findElement(By.name("password")).sendKeys("admin123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        System.out.println("Logged in and Dashboard visible.");
        takeScreenshot("after_login.png");

        WebElement adminTab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Admin']")));
        adminTab.click();
        Thread.sleep(1000); // optional delay for animation

        takeScreenshot("after_admin_click.png");

        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.oxd-table-filter-header-title > h5")));
        Assert.assertEquals(header.getText().trim(), "System Users", "'System Users' header is not displayed.");

        System.out.println("'System Users' header is visible. Test Passed.");
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public void takeScreenshot(String fileName) throws IOException {
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String path = System.getProperty("user.dir") + File.separator + "screenshots";
        File screenshotsDir = new File(path);
        if (!screenshotsDir.exists()) {
            screenshotsDir.mkdir();
        }
        FileUtils.copyFile(srcFile, new File(screenshotsDir, fileName));
        System.out.println("Screenshot saved: " + fileName);
    }
}
