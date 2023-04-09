package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Description;
import pages.CreateCustomerPage;
import pages.CustomerList;
import pages.Row;
import pages.StartPage;

class AllCustomerTests {

	static CreateCustomerPage newPage;
	static CustomerList customerList;
	static WebDriver driver;
	static StartPage startPage;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager");
		newPage = new CreateCustomerPage(driver);
		newPage.clickToAddCustomer();
		newPage.addCustomer("AaGg", "AaGg", "AaGg");
		newPage.addCustomer("Jack", "Daniels", "E946");
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		driver.get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager");
		customerList = new CustomerList(driver);
		customerList.clickToCustomers();
		customerList.deleteCustomer("AaGg");
		customerList.deleteCustomer("Daniels");
		customerList.deleteCustomer("AaFf123!;");
		driver.close();
	}
	
	@Test
	@Description("создание нового уникального пользователя")
	@ResourceLock(value = "newForm")
	@ResourceLock(value = "driver")
	void createNewCustomerTest() throws InterruptedException {
		newPage = new CreateCustomerPage(driver);
		String successResult = newPage.addCustomer("AaFf123!;", "AaFf123!;", "AaFf123!;");
		assertTrue(successResult.contains("Customer added successfully with customer id"));
	}

	@ParameterizedTest
	@ValueSource(strings = {"AaGg"})
	@EmptySource
	@Description("создание пользователя с пустыми полями")
	@ResourceLock(value = "newForm")
	@ResourceLock(value = "driver")
	void createEmptyUserTest(String value) throws InterruptedException {
		newPage = new CreateCustomerPage(driver);
		String result = newPage.addCustomer(value, value, value);
		if(value != "") assertEquals(result, "Please check the details. Customer may be duplicate.");
		else assertNull(result);
	}

	@Test
	@Description("сортировка списка пользователей")
	@ResourceLock(value = "newForm")
	@ResourceLock(value = "driver")
	void sortCustomersByFirstNameTest() throws InterruptedException {
		customerList = new CustomerList(driver);
		customerList.clickToCustomers();
		List<Row> list = customerList.getRows();
		list = list.stream().sorted(Comparator.comparing(Row::getFirstName).reversed()).collect(Collectors.toList());
		customerList.sortByFirstName();
		List<Row> list1 = customerList.getRows();
		assertTrue(list.equals(list1));
	}

	@ParameterizedTest(name = "{index}")
	@ValueSource(strings = {"AaGg", "Daniels", "E946"})
	@Description("поиск пользователя по всем трем его параметрам")
	@ResourceLock(value = "customerList")
	@ResourceLock(value = "driver")
	void findCustomerTest(String input, TestInfo info) throws InterruptedException {
		String count = info.getDisplayName();
		String parameter = null;
		List<Row> table = null;
		customerList = new CustomerList(driver);
		customerList.clickToCustomers();
		customerList.clearSeachField();
		customerList.findCustomer(input);
		table = customerList.getRows();
		switch (count) {
		case "1":
			parameter = table.get(0).getFirstName();
			break;
		case "2":
			parameter = table.get(0).getLastName();
			break;
		case "3":
			parameter = table.get(0).getZipCode();
		}
		assertEquals(parameter, input);
	}
}
