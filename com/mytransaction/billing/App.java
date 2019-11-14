package com.mytransaction.billing;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.mytransaction.billing.exception.BillingException;
import com.mytransaction.billing.utils.CsvReaderTransConverter;
import com.mytransaction.billing.utils.DateConverter;

@SpringBootApplication
@ComponentScan(basePackages = { "com.mytransaction.billing", "com.mytransaction.billing.*" })
public class App implements CommandLineRunner {

	@Autowired
	TransactionDetailReporting reporting;

	@Autowired
	DateConverter dateConvert;

	@Autowired
	CsvReaderTransConverter csvReader;

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);

	}

	@Override
	public void run(String... args) {

		// load records into the list
		csvReader.readFromCSV();

		String continueLoop;

		try (Scanner scan = new Scanner(System.in)) {

			do {
				System.out.println("\nfrom Date:");
				String fromDate = scan.nextLine();
				System.out.println("\nTo Date:");
				String toDate = scan.nextLine();
				System.out.println("\nMerchant:");
				String merchant = scan.nextLine();

				reporting.getCountAndAverage(dateConvert.convertStringToDate(fromDate),
						dateConvert.convertStringToDate(toDate), merchant);

				System.out.println("\n \n Continue: press y, otherwise press anykey");
				continueLoop = scan.nextLine();
			} while (continueLoop.equalsIgnoreCase("Y"));

		} catch (BillingException ex) {
			System.out.println(ex.getMessage());

		}

	}
}
