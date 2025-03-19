package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Random;

public class RegisterUserStepDefinitions {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = "https://membership.basketballengland.co.uk/NewSupporterAccount";
    private final String EMAIL_PREFIX = "kallekula";
    private final String EMAIL_DOMAIN = "@testmail.se";

    // Setup WebDriver och väntetider innan varje test
    @Before
    public void setup() {
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Stäng WebDriver efter varje test
    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Given("att jag är på registreringssidan för nya användare")
    public void attJagÄrPåRegistreringssidanFörNyaAnvändare() {
        driver.get(BASE_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@id='signup_form']")));
    }

    // Test 1: Lyckad registrering - alla fält korrekt ifyllda
    @When("jag fyller i alla nödvändiga fält korrekt")
    public void jagFyllerIAllaNödvändigaFältKorrekt() {
        String randomEmail = generateRandomEmail();

        driver.findElement(By.id("member_firstname")).sendKeys("Kalle");
        driver.findElement(By.id("member_lastname")).sendKeys("Kula"); // Första testet, efternamn ifyllt
        driver.findElement(By.id("member_emailaddress")).sendKeys(randomEmail);
        driver.findElement(By.id("member_confirmemailaddress")).sendKeys(randomEmail);
        driver.findElement(By.id("signupunlicenced_password")).sendKeys("Losenord123!");
        driver.findElement(By.id("signupunlicenced_confirmpassword")).sendKeys("Losenord123!");
        driver.findElement(By.id("dp")).sendKeys("05/03/1978");
    }

    // Test 2: Försök att registrera en användare utan efternamn
    @When("jag fyller i alla nödvändiga fält korrekt, men lämnar efternamn tomt")
    public void jagFyllerIAllaNödvändigaFältKorrektMenLämnarEfternamnTomt() {
        String randomEmail = generateRandomEmail();

        driver.findElement(By.id("member_firstname")).sendKeys("Kalle");
        driver.findElement(By.id("member_lastname")).clear(); // Lämnar efternamnet tomt för Test 2
        driver.findElement(By.id("member_emailaddress")).sendKeys(randomEmail);
        driver.findElement(By.id("member_confirmemailaddress")).sendKeys(randomEmail);
        driver.findElement(By.id("signupunlicenced_password")).sendKeys("Losenord123!");
        driver.findElement(By.id("signupunlicenced_confirmpassword")).sendKeys("Losenord123!");
        driver.findElement(By.id("dp")).sendKeys("05/03/1978");
    }

    // Test 3: Försök att registrera en användare med olika lösenord
    @When("jag fyller i alla nödvändiga fält korrekt, men lösenorden matchar inte")
    public void jagFyllerIAllaNödvändigaFältKorrektMenLosenordenMatcharInte() {
        String randomEmail = generateRandomEmail();

        driver.findElement(By.id("member_firstname")).sendKeys("Kalle");
        driver.findElement(By.id("member_lastname")).sendKeys("Kula");
        driver.findElement(By.id("member_emailaddress")).sendKeys(randomEmail);
        driver.findElement(By.id("member_confirmemailaddress")).sendKeys(randomEmail);
        driver.findElement(By.id("signupunlicenced_password")).sendKeys("Losenord123!");
        driver.findElement(By.id("signupunlicenced_confirmpassword")).sendKeys("FelaktigtLosenord!"); // För Test 3
        driver.findElement(By.id("dp")).sendKeys("05/03/1978");
    }

    // Test 4: Försök att registrera en användare utan att acceptera Terms and Conditions
    @When("jag fyller i alla nödvändiga fält korrekt men terms and conditions är inte ibockad")
    public void jagFyllerIAllaNödvändigaFältKorrektMenTermsAndConditionsArInteIbockad() {
        String randomEmail = generateRandomEmail();

        driver.findElement(By.id("member_firstname")).sendKeys("Kalle");
        driver.findElement(By.id("member_lastname")).sendKeys("Kula");
        driver.findElement(By.id("member_emailaddress")).sendKeys(randomEmail);
        driver.findElement(By.id("member_confirmemailaddress")).sendKeys(randomEmail);
        driver.findElement(By.id("signupunlicenced_password")).sendKeys("Losenord123!");
        driver.findElement(By.id("signupunlicenced_confirmpassword")).sendKeys("Losenord123!");
        driver.findElement(By.id("dp")).sendKeys("05/03/1978");
        // Don't click the terms checkbox for Test 4
    }

    // Klicka på checkboxar för godkännande av användarvillkor och andra alternativ
    @And("jag accepterar användarvillkoren och samtycker till alla nödvändiga checkboxar")
    public void jagAccepterarAnvändarvillkoren() {
        WebElement termsCheckbox = driver.findElement(By.cssSelector(".md-checkbox > .md-checkbox:nth-child(1) .box"));
        clickElement(termsCheckbox);

        WebElement ageCheckbox = driver.findElement(By.cssSelector(".md-checkbox:nth-child(7) .box"));
        clickElement(ageCheckbox);

        WebElement ethicsCheckbox = driver.findElement(By.cssSelector(".md-checkbox:nth-child(2) > label > .box"));
        clickElement(ethicsCheckbox);
    }

    @And("jag klickar på registreringsknappen")
    public void jagKlickarPåRegistreringsknappen() {
        WebElement confirmButton = driver.findElement(By.name("join"));
        confirmButton.click();
    }

    // Kontrollera att ett felmeddelande visas
    @Then("ska jag se ett felmeddelande")
    public void skaJagSeEttFelmeddelande() {
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='error-message']")));
        assert errorMessage.isDisplayed();
    }

    @Then("ska jag se ett felmeddelande om att lösenorden inte matchar")
    public void skaJagSeEttFelmeddelandeOmAttLosenordenInteMatchar() {
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Passwords do not match')]")));
        assert errorMessage.isDisplayed();
    }

    @Then("ska jag se ett felmeddelande om att användarvillkoren inte är accepterade")
    public void skaJagSeEttFelmeddelandeOmAttAnvandarvillkorenInteArAccepterade() {
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Please accept the terms and conditions')]")));
        assert errorMessage.isDisplayed();
    }

    private void clickElement(WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }

    private String generateRandomEmail() {
        Random random = new Random();
        int randomNum = random.nextInt(10000);
        return EMAIL_PREFIX + randomNum + EMAIL_DOMAIN;
    }
}
