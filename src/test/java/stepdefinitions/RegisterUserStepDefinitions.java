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
import static org.junit.Assert.*;

import java.time.Duration;
import java.util.Random;

public class RegisterUserStepDefinitions {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = "https://membership.basketballengland.co.uk/NewSupporterAccount";
    private final String EMAIL_PREFIX = "kallekula";
    private final String EMAIL_DOMAIN = "@testmail.se";
    private boolean isTermsAccepted = true;

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
        isTermsAccepted = true;
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
        isTermsAccepted = true;
    }

    // Test 3: Försök att registrera en användare med olika lösenord
    @When("jag fyller i alla nödvändiga fält korrekt, men lösenorden matchar inte")
    public void jagFyllerIAllaNödvändigaFältKorrektMenLösenordenMatcharInte() {
        String randomEmail = generateRandomEmail();

        driver.findElement(By.id("member_firstname")).sendKeys("Kalle");
        driver.findElement(By.id("member_lastname")).sendKeys("Kula");
        driver.findElement(By.id("member_emailaddress")).sendKeys(randomEmail);
        driver.findElement(By.id("member_confirmemailaddress")).sendKeys(randomEmail);
        driver.findElement(By.id("signupunlicenced_password")).sendKeys("Losenord123!");
        driver.findElement(By.id("signupunlicenced_confirmpassword")).sendKeys("FelaktigtLosenord!"); // För Test 3
        driver.findElement(By.id("dp")).sendKeys("05/03/1978");
        isTermsAccepted = true;
    }

    // Test 4: Försök att registrera en användare utan att acceptera Terms and Conditions
    @When("jag fyller i alla nödvändiga fält korrekt men terms and conditions är inte ibockad")
    public void jagFyllerIAllaNödvändigaFältKorrektMenTermsAndConditionsÄrInteIbockad() {
        String randomEmail = generateRandomEmail();

        driver.findElement(By.id("member_firstname")).sendKeys("Kalle");
        driver.findElement(By.id("member_lastname")).sendKeys("Kula");
        driver.findElement(By.id("member_emailaddress")).sendKeys(randomEmail);
        driver.findElement(By.id("member_confirmemailaddress")).sendKeys(randomEmail);
        driver.findElement(By.id("signupunlicenced_password")).sendKeys("Losenord123!");
        driver.findElement(By.id("signupunlicenced_confirmpassword")).sendKeys("Losenord123!");
        driver.findElement(By.id("dp")).sendKeys("05/03/1978");
        isTermsAccepted = false; // Markera att terms inte ska accepteras
    }

    // Klicka på checkboxar för godkännande av användarvillkor och andra alternativ
    @And("jag accepterar användarvillkoren och samtycker till alla nödvändiga checkboxar")
    public void jagAccepterarAnvändarvillkoren() {
        if (isTermsAccepted) {
            WebElement termsCheckbox = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector(".md-checkbox > .md-checkbox:nth-child(1) .box")));
            clickElement(termsCheckbox);

            WebElement ageCheckbox = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector(".md-checkbox:nth-child(7) .box")));
            clickElement(ageCheckbox);

            WebElement ethicsCheckbox = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector(".md-checkbox:nth-child(2) > label > .box")));
            clickElement(ethicsCheckbox);
        }
    }

    @And("jag klickar på registreringsknappen")
    public void jagKlickarPåRegistreringsknappen() {
        WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(By.name("join")));
        clickElement(confirmButton);
        try {
            Thread.sleep(2000); // Kort paus för att säkerställa att sidan hinner reagera
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Then("ska en bekräftelse på lyckad registrering visas")
    public void skaEnBekräftelsePåLyckadRegistreringVisas() {
        try {
            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("h2.bold.gray.text-center.margin-bottom-40")));
            assertTrue("Bekräftelsemeddelande visas inte", successMessage.isDisplayed());
        } catch (Exception e) {
            assertTrue("Lyckad registrering kunde inte verifieras", driver.getPageSource().contains("Thank you"));
        }
    }

    @Then("ska jag se ett felmeddelande")
    public void skaJagSeEttFelmeddelande() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error-message")));
            assertTrue("Felmeddelande visas inte", driver.getPageSource().contains("required"));
        } catch (Exception e) {
            fail("Kunde inte hitta felmeddelande: " + e.getMessage());
        }
    }

    @Then("ska jag se ett felmeddelande om att lösenorden inte matchar")
    public void skaJagSeEttFelmeddelandeOmAttLösenordenInteMatchar() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Passwords do not match')]")));
            assertTrue("Felmeddelande om lösenord inte matchar visas inte", driver.getPageSource().contains("Passwords do not match"));
        } catch (Exception e) {
            fail("Kunde inte hitta felmeddelande om lösenord: " + e.getMessage());
        }
    }

    @Then("ska jag se ett felmeddelande om att användarvillkoren inte är accepterade")
    public void skaJagSeEttFelmeddelandeOmAttAnvändarvillkorenInteÄrAccepterade() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'terms') and contains(text(), 'conditions')]")));
            assertTrue("Felmeddelande om användarvillkor visas inte", driver.getPageSource().contains("terms and conditions"));
        } catch (Exception e) {
            fail("Kunde inte hitta felmeddelande om användarvillkor: " + e.getMessage());
        }
    }

    private void clickElement(WebElement element) {
        try {
            element.click();
        } catch (Exception e) {
            try {
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeScript("arguments[0].click();", element);
            } catch (Exception jsException) {
                System.out.println("Kunde inte klicka på element via JavaScript heller: " + jsException.getMessage());
            }
        }
    }

    private String generateRandomEmail() {
        Random random = new Random();
        int randomNum = random.nextInt(10000);
        return EMAIL_PREFIX + randomNum + EMAIL_DOMAIN;
    }
}
