package amortization;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class AmortizationCalculatorTest {
	private static AmortizationCalculator ac = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		double amount = 1000d;
		double apr = 5;
		int years = 1;
		
		ac = new AmortizationCalculator(amount, apr, years);
	}

	@Test
	public void getPaymentNumber() {
		ac.calculatePaymentSchedule();
		ArrayList<AmortizationPayment> payments = ac.getPaymentSchedule();
		assertEquals(ac.getTermMonths() + 1, payments.size());
	}
	
	@Test
	public void getPaymentSchedule() {
		ac.calculatePaymentSchedule();
		ArrayList<AmortizationPayment> payments = ac.getPaymentSchedule();
		// the 0th payment
		assertEquals(ac.getBorrowAmount()*AmortizationCalculator.CENTS_TO_DOLLARS, 
			payments.get(0).getCurrentBalance(), 0.01);
		// the 5th payment
		assertEquals(589.39, payments.get(5).getCurrentBalance(), 0.1);
		// the 10th payment
		assertEquals(170.15, payments.get(10).getCurrentBalance(), 0.1);
	}

}
