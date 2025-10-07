package com.taskmanager.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.Arrays;
import java.util.List;

import com.taskmanager.exception.InvalidDateException;

public class DateValidator {

	private static final List<String> DATE_FORMATS = Arrays.asList("yyyy-MM-dd HH:mm", "yyyy-MM-dd", "yyyy/MM/dd HH:mm",
			"yyyy/MM/dd", "dd/MM/yyyy HH:mm", "dd/MM/yyyy", "dd-MM-yyyy HH:mm", "dd-MM-yyyy");

	public static LocalDateTime convertToDate(String dateString) throws InvalidDateException {
		if (dateString == null || dateString.trim().isEmpty()) {
			throw new InvalidDateException("Please provide a date");
		}

		for (String format : DATE_FORMATS) {
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
				LocalDateTime dateTime;

				if (format.contains("HH")) {
					dateTime = LocalDateTime.parse(dateString, formatter);
				} else {
					LocalDate date = LocalDate.parse(dateString, formatter);
					dateTime = date.atStartOfDay();
				}

				validateFebruary(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth());
				return dateTime;

			} catch (DateTimeParseException e) {
				continue;
			}
		}
		throw new InvalidDateException(
				"Invalid date format. Supported formats: yyyy-MM-dd, dd/MM/yyyy, dd-MM-yyyy, yyyy-MM-dd HH:mm");
	}

	private static void validateFebruary(int year, int month, int day) throws InvalidDateException {
		if (month == 2) {
			if (day > 29) {
				throw new InvalidDateException("Invalid date: February cannot have more than 29 days");
			}
			if (day == 29 && !Year.of(year).isLeap()) {
				throw new InvalidDateException("Invalid date: February 29 is only valid in leap years");
			}
		}
	}

	public static boolean isValidDueDate(LocalDateTime dueDate) {
		return (dueDate != null && dueDate.isAfter(LocalDateTime.now()));
	}

	public void validateFutureDate(LocalDateTime date) throws InvalidDateException {
		if (date == null) {
			throw new InvalidDateException("Date cannot be null");
		}

		LocalDateTime now = LocalDateTime.now();
		if (date.isBefore(now)) {
			throw new InvalidDateException("Due date must be in the future");
		}
	}
}