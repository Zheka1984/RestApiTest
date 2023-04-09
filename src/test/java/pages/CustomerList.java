package pages;

import java.util.List;
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

	JavascriptExecutor jsExecutor;

	public CustomerList(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
		jsExecutor = (JavascriptExecutor) driver;
	}

	@FindBy(xpath = "/html/body/div/div/div[2]/div/div[2]/div/div/table")
	WebElement table;

	@FindBy(css = ".table > thead:nth-child(1) > tr:nth-child(1) > td:nth-child(1) > a:nth-child(1)")
	WebElement firstNameCell;

	@FindBy(xpath = "/html/body/div/div/div[2]/div/div[2]/div/form/div/div/input")
	WebElement seachField;
	
	By elementOfTable = By.tagName("tr");

	public List<Row> getRows() {
		List<WebElement> listOfAllRows = table.findElements(elementOfTable);
		List<Row> rowList = new ArrayList<>();
		List<WebElement> columnsList = null;
		Row row = null;
		for (int j = 0; j < listOfAllRows.size(); j++) {
			if (j > 0) {
				columnsList = listOfAllRows.get(j).findElements(By.tagName("td"));
				row = new Row(columnsList);
				rowList.add(row);
			}
		}
		return rowList;
	}

	public void findCustomer(String data) throws InterruptedException {
		Actions act = new Actions(driver);
		act.sendKeys(table, Keys.PAGE_UP).build().perform();
		seachField.sendKeys(data);
	}

	public void sortByFirstName() {
		jsExecutor.executeScript("arguments[0].click();", firstNameCell);
	}

	public void clearSeachField() {
		seachField.clear();
	}
	public void deleteCustomer(String name) throws InterruptedException {
		List<Row> list = null;
		findCustomer("AaGg");
		list = this.getRows();
		list.get(0).getDeleteButton().click();
		clearSeachField();
	}
}