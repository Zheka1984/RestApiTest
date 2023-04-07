package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Description;
import pages.CreateCustomerForm;
import pages.CustomerList;
import pages.CustomerList.Rows;
import pages.StartPage;

class AllCustomerTests {

	StartPage spage;
	static CreateCustomerForm ccf;
	static CustomerList cl;
	static WebDriver driver;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		WebDriverManager.chromedriver().setup();
		;
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager");
		driver.get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager");
		driver.findElement(By.cssSelector("button.btn-lg:nth-child(1)")).click();
		ccf = new CreateCustomerForm(driver);
		ccf.fillCustomerData("AaGg", "AaGg", "AaGg");
		ccf.clickAddCustomerAtTheBottomAndGetAlertText();
//		ccf.fillCustomerData("Abc", "Cdf", "E123");
//		ccf.clickAddCustomerAtTheBottomAndGetAlertText();
		ccf.fillCustomerData("Jack", "Daniels", "E946");
		ccf.clickAddCustomerAtTheBottomAndGetAlertText();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		driver.get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager");
		driver.findElement(By.cssSelector("button.btn-lg:nth-child(3)")).click();
		cl = new CustomerList(driver);
		List<Rows> list = null;
		cl.findCustomer("AaGg");
		list = cl.getRows();
		list.get(0).getDeleteButton().click();
		cl.clearSeachField();
		cl.findCustomer("Daniels");
		list = cl.getRows();
		list.get(0).getDeleteButton().click();
		cl.clearSeachField();
		driver.close();
	}

	@Test
	@Description("создание нового уникального пользователя")
	@ResourceLock(value = "ccf")
	@ResourceLock(value = "driver")
	void createNewCustomer() throws InterruptedException {
		System.out.println("start 1");
		driver.get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager");
		driver.findElement(By.cssSelector("button.btn-lg:nth-child(1)")).click();
		ccf = new CreateCustomerForm(driver);
		ccf.fillCustomerData("AaFf123!;", "AaFf123!;", "AaFf123!;");
		String successResult = ccf.clickAddCustomerAtTheBottomAndGetAlertText();
		assertTrue(successResult.contains("Customer added successfully with customer id"));
		System.out.println("end 1");
	}

	@Test
	@Description("создание дублирующего пользователя, добавленного перед выпольнением тестов")
	@ResourceLock(value = "ccf")
	@ResourceLock(value = "driver")
	void createDuplicateCustomer() throws InterruptedException {
		System.out.println("start 2");
		driver.get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager");
		driver.findElement(By.cssSelector("button.btn-lg:nth-child(1)")).click();
		ccf.fillCustomerData("AaGg", "AaGg", "AaGg");
		ccf.clickAddCustomerAtTheBottomAndGetAlertText();
		String successResult = ccf.clickAddCustomerAtTheBottomAndGetAlertText();
		assertEquals(successResult, "Please check the details. Customer may be duplicate.");
		System.out.println("end 2");
	}

	@Test
	@Description("создание пользователя с пустыми полями")
	@ResourceLock(value = "ccf")
	@ResourceLock(value = "driver")
	void createEmptyUser() throws InterruptedException {
		System.out.println("start 3");
		driver.get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager");
		driver.findElement(By.cssSelector("button.btn-lg:nth-child(1)")).click();
		ccf = new CreateCustomerForm(driver);
		ccf.fillCustomerData("", "", "");
		String result = ccf.clickAddCustomerAtTheBottomAndGetAlertText();
		assertNull(result);
		System.out.println("end 3");
	}

	@Test
	@Description("сортировка списка пользователей")
	@ResourceLock(value = "ccf")
	@ResourceLock(value = "cl")
	@ResourceLock(value = "driver")
	void sortCustomersByFirstName() throws InterruptedException {
		System.out.println("start 4");
		driver.findElement(By.cssSelector("button.btn-lg:nth-child(1)")).click();
		ccf = new CreateCustomerForm(driver);
		ccf.clickToCustomers();
		cl = new CustomerList(driver);
		List<Rows> list = cl.getRows();
		list = list.stream().sorted(Comparator.comparing(Rows::getFirstName)).collect(Collectors.toList());
		cl.sortByFirstName();
		cl.sortByFirstName();
		List<Rows> list1 = cl.getRows();
		assertTrue(list.equals(list1));
		System.out.println("end 4");
	}

	@Test
	@Description("поиск пользователя по всем трем его параметрам")
	@ResourceLock(value = "cl")
	@ResourceLock(value = "driver")
	void findCustomer() throws InterruptedException {
		System.out.println("start 5");
		List<CustomerList.Rows> list = null;
		driver.get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager");
		driver.findElement(By.cssSelector("button.btn-lg:nth-child(3)")).click();
		cl = new CustomerList(driver);
		cl.findCustomer("AaGg");
		list = cl.getRows();
		assertEquals(list.get(0).getFirstName(), "AaGg");
		cl.clearSeachField();
		cl.findCustomer("Daniels");
		list = cl.getRows();
		list.get(0).getDeleteButton().click();
		assertEquals(list.get(0).getLastName(), "Daniels");
		cl.clearSeachField();
		cl.findCustomer("E946");
		list = cl.getRows();
		assertEquals(list.get(0).getZipCode(), "E946");
		System.out.println("end 5");
	}
}
