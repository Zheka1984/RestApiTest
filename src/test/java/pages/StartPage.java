package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class StartPage {

	WebDriver driver;

	public StartPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(css = "button.btn-lg:nth-child(1)")
	WebElement addCustomer;

	@FindBy(css = "button.btn:nth-child(2)")
	WebElement openAccount;

	@FindBy(css = "button.btn-lg:nth-child(3)")
	WebElement customers;

	public void clickToAddCustomer() {
		addCustomer.click();
	}

	public void clickToOpenAccount() {
		openAccount.click();
	}

	public void clickToCustomers() {
		customers.click();
	}

}
