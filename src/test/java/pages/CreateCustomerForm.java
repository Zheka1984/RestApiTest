package pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CreateCustomerForm extends StartPage {

	WebDriver driver;

	public CreateCustomerForm(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(css = "div.form-group:nth-child(1) > input:nth-child(2)")
	WebElement firstName;

	@FindBy(css = "div.form-group:nth-child(2) > input:nth-child(2)")
	WebElement lastName;

	@FindBy(css = "div.form-group:nth-child(3) > input:nth-child(2)")
	WebElement zipCode;

	@FindBy(css = "button.btn:nth-child(4)")
	WebElement addCustomerAtTheBottom;

	public void fillCustomerData(String firstName, String lastName, String postCode) {
		this.firstName.sendKeys(firstName);
		this.lastName.sendKeys(lastName);
		this.zipCode.sendKeys(postCode);
	}

	public String clickAddCustomerAtTheBottomAndGetAlertText() throws InterruptedException {
		addCustomerAtTheBottom.click();
		String s = null;
		try {
			Alert alert = driver.switchTo().alert();
			s = alert.getText();
			alert.accept();
		} catch (NoAlertPresentException e) {

		}
		return s;
	}

}
