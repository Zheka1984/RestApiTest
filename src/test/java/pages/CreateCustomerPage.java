package pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CreateCustomerPage extends StartPage {

	WebDriver driver;

	public CreateCustomerPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		driver.get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager/addCust");
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

	private void fillCustomerData(String firstName, String lastName, String postCode) {
		this.firstName.sendKeys(firstName);
		this.lastName.sendKeys(lastName);
		this.zipCode.sendKeys(postCode);
	}

	private String clickAddCustomerAtTheBottomAndGetAlertText() throws InterruptedException {
		addCustomerAtTheBottom.click();
		String alertMessage = null;
		try {
			Alert alert = driver.switchTo().alert();
			alertMessage = alert.getText();
			alert.accept();
		} catch (NoAlertPresentException e) {
			System.out.println("добавление пустого пользователя");
		}
		return alertMessage;
	}
	public String addCustomer(String firstName, String lastName, String postCode) throws InterruptedException {
		fillCustomerData(firstName, lastName, postCode);
		return clickAddCustomerAtTheBottomAndGetAlertText();
	}
}