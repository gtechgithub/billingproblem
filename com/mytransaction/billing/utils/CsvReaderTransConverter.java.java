package com.mytransaction.billing.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.mytransaction.billing.constants.Constants;
import com.mytransaction.billing.constants.PaymentTypes;
import com.mytransaction.billing.models.BillingTransactionDetails;

@Component
public class CsvReaderTransConverter {

	@Autowired
	private TransactionPropertiesLoader properties;

	@Autowired
	DateConverter dateConverter;
	
	


	private List<BillingTransactionDetails> transDetails;
	
	public void readFromCSV() {

		List<BillingTransactionDetails> billingTransList = new ArrayList();

		try (Stream<String> stream = Files.lines(Paths.get("C:\\Softwares\\projects\\transactiondetails.csv"))) {
			stream.forEach(line -> populateTransctionDetails(billingTransList, line));
		} catch (IOException e) {
			e.printStackTrace();
		}

		transDetails =  billingTransList;
	}

	private void populateTransctionDetails(List<BillingTransactionDetails> billingTransList, String line) {
		// BillingTransactionDetails billingTrans = new BillingTransactionDetails ();
		String[] billingTrans = line.split(Constants.DELIM);
		BillingTransactionDetails billingDet = null;
		
		Stream<String> stringStream = Arrays.stream(billingTrans).map(billingtran -> billingtran.trim());
		billingTrans = stringStream.toArray(size -> new String[size]);
		
		// check if it is empty or invalid entries
		if (null == billingTrans || billingTrans.length < Constants.NO_OF_FIELDS_PAYMENTS) {
			return;
		}

		// check if it is a header row, checking with few header columns
		if (billingTrans[0].equalsIgnoreCase(Constants.ID) && billingTrans[1].equalsIgnoreCase(Constants.Date)) {
			return;
		}

		billingDet = new BillingTransactionDetails(billingTrans[0], dateConverter.convertStringToDate(billingTrans[1]),
				new BigDecimal(billingTrans[2]), billingTrans[3], PaymentTypes.getByName(billingTrans[4]));

		billingTransList.add(billingDet);

		if (billingTrans.length == Constants.NO_OF_FIELDS_REVERSAL) {
			billingDet.setReversalTransactionId(billingTrans[5]);
		}
	}
	
	public List<BillingTransactionDetails> getTransDetails() {
		return transDetails;
	}

}
