package com.taskmanager.util;

import java.time.LocalDateTime;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.taskmanager.exception.InvalidDateException;

public class DateValidatorTest {

	private DateValidator dateValidator;

	@BeforeMethod
	public void setUp() {
		dateValidator = new DateValidator(); // Initialize before each test method
	}

	@Test
	public void testValidDate() throws InvalidDateException {
		String dateString = "2012-12-22 10:30";
		LocalDateTime date = DateValidator.convertToDate(dateString);
		Assert.assertEquals(date.getYear(), 2012);
		Assert.assertEquals(date.getMonthValue(), 12);
		Assert.assertEquals(date.getDayOfMonth(), 22);
		Assert.assertEquals(date.getHour(), 10);
		Assert.assertEquals(date.getMinute(), 30);
	}

	@DataProvider(name = "validDates")
	public Object[][] validDates() {
		return new Object[][] { { "2000-12-31 11:30" }, { "1979-12-31" }, { "31/12/2012 23:30" }, { "12/12/2012" },
				{ "22-10-2024 20:30" } };
	}

	@Test(dataProvider = "validDates")
	public void testDateFormats(String dateString) throws InvalidDateException {
		LocalDateTime date = DateValidator.convertToDate(dateString);
		Assert.assertNotNull(date);
	}

	@DataProvider(name = "invalidDates")
	public Object[][] invalidDates() {
		return new Object[][] { { "2024-12-32" }, { "in-valid-date" } };
	}

	@Test(dataProvider = "invalidDates", expectedExceptions = InvalidDateException.class)
	public void testInvalidDateFormats(String dateString) throws InvalidDateException {
		LocalDateTime date = DateValidator.convertToDate(dateString);
		DateValidator.convertToDate(dateString);
		Assert.assertNotNull(date);
	}

	@Test(expectedExceptions = InvalidDateException.class)
	public void testNullDate() throws InvalidDateException {
		DateValidator.convertToDate(null);
	}

	@Test(expectedExceptions = InvalidDateException.class)
	public void testEmptyDate() throws InvalidDateException {
		DateValidator.convertToDate("");
	}

	@Test
	public void testValidFutureDate() throws InvalidDateException {
		LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
		dateValidator.validateFutureDate(futureDate);

	}

	@Test(expectedExceptions = InvalidDateException.class)
	public void testPastDate() throws InvalidDateException {
		LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
		dateValidator.validateFutureDate(pastDate);
	}

	@Test
	public void testLeapYearDate() throws InvalidDateException {
		// Test valid leap year date (February 29)
		String leapYearDate = "2004-02-29 11:45";
		LocalDateTime date = DateValidator.convertToDate(leapYearDate);
		Assert.assertEquals(date.getYear(), 2004);
		Assert.assertEquals(date.getMonthValue(), 2);
		Assert.assertEquals(date.getDayOfMonth(), 29);
	}

	@DataProvider(name = "leapYearDates")
	public Object[][] leapYearDates() {
		return new Object[][] { { "2024-02-28 11:30", true }, // Valid leap year
				{ "2000-02-29", true }, { "2016-02-29", true } };
	}

	@Test(dataProvider = "leapYearDates")
	public void testLeapYearValidation(String dateString, boolean shouldBeValid) {
		boolean isValid;

		try {

			DateValidator.convertToDate(dateString);
			isValid = true;
		} catch (InvalidDateException e) {
			System.out.println("Validation failed for " + dateString + ": " + e.getMessage());
			isValid = false;
		}

		Assert.assertEquals(isValid, shouldBeValid, "Date validity mismatch for: " + dateString);
	}
}
