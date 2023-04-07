package pages;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CustomerList extends StartPage {

	WebDriver driver;

	JavascriptExecutor jse;

	public CustomerList(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
		jse = (JavascriptExecutor) driver;
	}

	@FindBy(xpath = "/html/body/div/div/div[2]/div/div[2]/div/div/table")
	WebElement table;

	@FindBy(css = ".table > thead:nth-child(1) > tr:nth-child(1) > td:nth-child(1) > a:nth-child(1)")
	WebElement firstNameCell;

	@FindBy(xpath = "/html/body/div/div/div[2]/div/div[2]/div/form/div/div/input")
	WebElement seachField;

	public List<Rows> getRows() {
		List<WebElement> rowsList = table.findElements(By.tagName("tr"));
		List<Rows> rowList1 = new ArrayList<>();
		List<WebElement> columnsList = null;
		Rows r = null;
		for (int j = 0; j < rowsList.size(); j++) {
			r = new Rows();
			if (j > 0) {
				columnsList = rowsList.get(j).findElements(By.tagName("td"));
				for (int i = 0; i < columnsList.size(); i++) {
					if (i == 0)
						r.setFirstName(columnsList.get(0).getText());
					if (i == 1)
						r.setLastName(columnsList.get(1).getText());
					if (i == 2)
						r.setZipCode(columnsList.get(2).getText());
					if (i == 4)
						r.setDeleteButton(columnsList.get(4));
				}
				rowList1.add(r);
			}
		}
		return rowList1;
	}

	public void findCustomer(String data) throws InterruptedException {
		// jse.executeScript("document.getElementsByClassName('form-control')[0].value('"+data+"')");
		// jse.executeScript("document.getElementsByClassName('form-control')[0].focus();
		// arguments[0].setAttribute('value', '��Ff123!;')", seachField);
		Actions act = new Actions(driver);
		act.sendKeys(table, Keys.PAGE_UP).build().perform();
		seachField.sendKeys(data);
	}

	public void sortByFirstName() {
		jse.executeScript("arguments[0].click();", firstNameCell);
	}

	public void clearSeachField() {
		seachField.clear();
	}

	public static class Rows {
		String firstName;
		String lastName;
		String zipCode;
		WebElement deleteButton;

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getZipCode() {
			return zipCode;
		}

		public void setZipCode(String zipCode) {
			this.zipCode = zipCode;
		}

		public WebElement getDeleteButton() {
			return deleteButton;
		}

		public void setDeleteButton(WebElement deleteButton) {
			this.deleteButton = deleteButton;
		}

		@Override
		public int hashCode() {
			return Objects.hash(firstName, lastName, zipCode);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Rows other = (Rows) obj;
			return Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName)
					&& Objects.equals(zipCode, other.zipCode);
		}

	}

}
