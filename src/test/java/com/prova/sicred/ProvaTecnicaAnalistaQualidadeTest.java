package com.prova.sicred;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.util.concurrent.Uninterruptibles;

public class ProvaTecnicaAnalistaQualidadeTest {

	private static final String MESSAGE_SUCCESSFULLY_DELETED_FROM_THE_DATABASE = "Your data has been successfully deleted from the database.";
	private static final String MESSAGE_CONFIRM_DELETE_ITEM = "Are you sure that you want to delete this 1 item?";
	private static final String MESSAGE_SUCCESSFULLY_STORED_INTO_THE_DATABASE = "Your data has been successfully stored into the database.";
	private static String driverPath = System.getProperty("user.dir") + "/src/test/resources/chromedriver/";
	private WebDriver driver;
	private WebDriverWait wait;
	private Map<String, Integer> fromEmployeerMap;

	@BeforeMethod
	private void setUp() {
		System.setProperty("webdriver.chrome.driver", driverPath + "chromedriver");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		wait = new WebDriverWait(driver, 5000);
		driver.navigate().to("https://www.grocerycrud.com/demo/bootstrap_theme");
		registerFromEmployeerMap();
	}

	@Test
	private void desafio1() {
		insertRegistryAndValidate("Bootstrap V4 Theme", "Teste Sicredi", "Teste", "Guilherme", "51 9999-9999", "Av Assis Brasil, 3970", "Torre D", "Porto Alegre", "RS",
				"91000-000", "Brasil", "Fixter", "200");
		removeRegistry("Teste Sicredi");
	}

	@Test
	private void desafio2() {
		insertRegistryAndValidate("Bootstrap V4 Theme", "Teste Sicredi", "Teste", "Guilherme", "51 9999-9999", "Av Assis Brasil, 3970", "Torre D", "Porto Alegre", "RS",
				"91000-000", "Brasil", "Fixter", "200");
		removeRegistryAndValidate("Teste Sicredi");
	}

	private void insertRegistryAndValidate(String version,String name, String lastName, String contactFirstName, String phone, String addressLine1, String addressLine2,
			String city, String state, String postalCode, String country, String fromEmployeer, String creditLimit) {
		//given
		selectCustomerFieldById("switch-version-select", version);
		clickCustomerFieldByLinkText("Add Customer");
		fillCustomerField("field-customerName", name);
		fillCustomerField("field-contactLastName", lastName);
		fillCustomerField("field-contactFirstName", contactFirstName);
		fillCustomerField("field-phone", phone);
		fillCustomerField("field-addressLine1", addressLine1);
		fillCustomerField("field-addressLine2", addressLine2);
		fillCustomerField("field-city", city);
		fillCustomerField("field-state", state);
		fillCustomerField("field-postalCode", postalCode);
		fillCustomerField("field-country", country);
		selectCustomerFieldByXpath("(.//*[normalize-space(text()) and normalize-space(.)='Select from Employeer'])[1]/following::b[1]",
				"(.//*[normalize-space(text()) and normalize-space(.)='" + fromEmployeer + "'])/following::li["
						+ fromEmployeerMap.get(fromEmployeer).toString() + "]");
		fillCustomerField("field-creditLimit", creditLimit);
		clickCustomerFieldById("form-button-save");
		waitCustomerFieldById("report-success");
		//when
		String currentMessageSuccessfullyStoredIntoTheDatabase = searchCustomerFieldById("report-success");
		//then
		assertTrue(currentMessageSuccessfullyStoredIntoTheDatabase.contains(MESSAGE_SUCCESSFULLY_STORED_INTO_THE_DATABASE));
		clickCustomerFieldByLinkText("Go back to list");
	}

	private void removeRegistryAndValidate(String name) {
		//given
		searchRegistryByName(name);
		waitCustomerFieldByXpath("(.//*[normalize-space(text()) and normalize-space(.)='CreditLimit'])[1]/following::span[1]");
		clickCustomerFieldByXpath("(.//*[normalize-space(text()) and normalize-space(.)='CreditLimit'])[1]/following::span[1]");
		waitCustomerFieldByXpath("(.//*[normalize-space(text()) and normalize-space(.)='Cancel'])[2]/following::button[1]");
		//when
		String currentMessageConfirmDeleteItem = searchCustomerFieldByXpath("(.//*[normalize-space(text()) and normalize-space(.)='Delete'])[6]/following::p[2]");
		//then
		assertEquals(currentMessageConfirmDeleteItem, MESSAGE_CONFIRM_DELETE_ITEM);

		//given
		clickCustomerFieldByXpath("(.//*[normalize-space(text()) and normalize-space(.)='Cancel'])[2]/following::button[1]");
		waitCustomerFieldByXpath("(.//*[normalize-space(text()) and normalize-space(.)='Close'])[1]/following::p[1]");
		//when
		String currentMessageSuccessfullyDeletedFromTheDatabase = searchCustomerFieldByXpath("(.//*[normalize-space(text()) and normalize-space(.)='Close'])[1]/following::p[1]");
		//then
		assertEquals(currentMessageSuccessfullyDeletedFromTheDatabase, MESSAGE_SUCCESSFULLY_DELETED_FROM_THE_DATABASE);
	}

	private void removeRegistry(String name) {
		searchRegistryByName(name);
		if (driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='CreditLimit'])[1]/following::span[1]")) != null) {

			clickCustomerFieldByXpath("(.//*[normalize-space(text()) and normalize-space(.)='CreditLimit'])[1]/following::span[1]");
			waitCustomerFieldByXpath("(.//*[normalize-space(text()) and normalize-space(.)='Cancel'])[2]/following::button[1]");
			clickCustomerFieldByXpath("(.//*[normalize-space(text()) and normalize-space(.)='Cancel'])[2]/following::button[1]");
			waitCustomerFieldByXpath("(.//*[normalize-space(text()) and normalize-space(.)='Close'])[1]/following::p[1]");
		}
	}

	private void searchRegistryByName(String name) {
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Print'])[1]/following::a[1]")));
		driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Print'])[1]/following::a[1]")).click();
		driver.findElement(By.name("search")).clear();
		driver.findElement(By.name("search")).sendKeys(name);
		driver.findElement(By.xpath("//body")).click();
		Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
		driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='CreditLimit'])[1]/following::input[1]")).click();
	}

	private void registerFromEmployeerMap() {
		fromEmployeerMap = new HashMap<String, Integer>();
		fromEmployeerMap.put("Bondur", 1);
		fromEmployeerMap.put("Bott", 3);
		fromEmployeerMap.put("Bow", 4);
		fromEmployeerMap.put("Castillo", 5);
		fromEmployeerMap.put("Firrelli", 6);
		fromEmployeerMap.put("Fixter", 8);
		fromEmployeerMap.put("Gerard", 9);
		fromEmployeerMap.put("Hernandez", 10);
		fromEmployeerMap.put("Jennings", 11);
		fromEmployeerMap.put("Jones", 12);
		fromEmployeerMap.put("Kato", 13);
		fromEmployeerMap.put("King", 14);
		fromEmployeerMap.put("March", 15);
		fromEmployeerMap.put("Murphy", 16);
		fromEmployeerMap.put("Nishi", 17);
		fromEmployeerMap.put("Patterson", 18);
		fromEmployeerMap.put("Thompson", 21);
		fromEmployeerMap.put("Tseng", 22);
		fromEmployeerMap.put("Vanauf", 23);
	}

	private void clickCustomerFieldById(String fieldName) {
		driver.findElement(By.id(fieldName)).click();
	}

	private void clickCustomerFieldByLinkText(String linkText) {
		driver.findElement(By.linkText(linkText)).click();
	}

	private void clickCustomerFieldByXpath(String xpath) {
		driver.findElement(By.xpath(xpath)).click();
	}

	private String searchCustomerFieldById(String fieldName) {
		return driver.findElement(By.id(fieldName)).getText();
	}

	private String searchCustomerFieldByXpath(String xpath) {
		return driver.findElement(By.xpath(xpath)).getText();
	}

	private void fillCustomerField(String fieldName, String fieldValue) {
		driver.findElement(By.id(fieldName)).click();
		driver.findElement(By.id(fieldName)).clear();
		driver.findElement(By.id(fieldName)).sendKeys(fieldValue);
	}

	private void selectCustomerFieldById(String fieldName, String fieldValue) {
		clickCustomerFieldById(fieldName);
		new Select(driver.findElement(By.id(fieldName))).selectByVisibleText(fieldValue);
	}

	private void selectCustomerFieldByXpath(String fiedXpathName, String fieldXpathValue) {
		clickCustomerFieldByXpath(fiedXpathName);
		clickCustomerFieldByXpath(fieldXpathValue);
	}

	private void waitCustomerFieldById(String fieldName) {
		wait.until(ExpectedConditions.elementToBeClickable(By.id(fieldName)));
	}

	private void waitCustomerFieldByXpath(String xpath) {
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
	}

	@AfterMethod
	private void tearDown() {
		if(driver!=null) {
			driver.quit();
		}
	}
}
