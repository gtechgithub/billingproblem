package com.mytransaction.billing.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mytransaction.billing.constants.Constants;
import com.mytransaction.billing.exception.BillingException;

@Component
public class DateConverter {

	public LocalDateTime convertStringToDate(String dateString) {
		// convert string to date time
		LocalDateTime dateTime = null;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ddmmyyydateFormat);
			dateTime = LocalDateTime.parse(dateString, formatter);

		} catch (DateTimeParseException ex) {

			throw new BillingException("enter the proper date in dd/mm/yyyy hh:mm:ss format");
		} catch (BillingException ex) {
			throw new BillingException("Recevied excepion during date parser " + ex.getMessage());
		}
		return dateTime;

	}
}
