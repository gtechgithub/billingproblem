package com.mytransaction.billing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.mytransaction.billing.constants.PaymentTypes;
import com.mytransaction.billing.models.BillingTransactionDetails;
import com.mytransaction.billing.utils.CsvReaderTransConverter;

@Service
public class TransactionDetailReporting {

	@Autowired
	CsvReaderTransConverter csvReader;

	public void getCountAndAverage(LocalDateTime startDateTime, LocalDateTime endDateTime, String merchantName) {

		List<BigDecimal> transExtractDetails = csvReader.getTransDetails().stream().filter(Objects::nonNull)
				.filter(tranDetail -> tranDetail.getTransactionDate().compareTo(startDateTime) >= 0
						&& tranDetail.getTransactionDate().compareTo(endDateTime) <= 0)
				.filter(tranDetail -> tranDetail.getMerchantName().equalsIgnoreCase(merchantName))
				.filter(tranDetail -> tranDetail.getPaymentType().equals(PaymentTypes.PAYMENT))
				.map(BillingTransactionDetails::getAmount).collect(Collectors.toList());

		int count = transExtractDetails.size();

		if (count > 0) {
			BigDecimal SumTotalResult = transExtractDetails.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal average = SumTotalResult.divide(new BigDecimal(count), 2, RoundingMode.HALF_UP);

			System.out.println("Number of transactions = " + count);
			System.out.println("Average Transaction Value  = " + average);
		} else {
			System.out.println("no records to display");
		}
	}
}
