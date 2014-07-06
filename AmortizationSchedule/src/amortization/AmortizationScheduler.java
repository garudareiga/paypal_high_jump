package amortization;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.IllegalFormatException;

/**
 * The amortization scheduler takes three input values:<br>
 * 		the amount he or she is borrowing,<br>
 * 		the annual percentage rate used to repay the loan,<br>
 *		the term, in years, over which the loan is repaid.<br>
 * and then outputs the payment schedule.
 * @author ray
 *
 */
public class AmortizationScheduler {
	/** console for scheduler's input/ouput interface */
	private static Console console = System.console();
	/** amortization calculator */
	private AmortizationCalculator calculator = null;
	
	public AmortizationScheduler(AmortizationCalculator calculator) {
		this.calculator = calculator;
	}
	
	public AmortizationCalculator getCalculator() {
		return calculator;
	}
	
	/** Output amortization payment schedule */
	public void outputAmortizationSchedule() {
		String formatString = "%1$-20s%2$-20s%3$-20s%4$s,%5$s,%6$s\n";
		printf(formatString,
				"PaymentNumber", "PaymentAmount", "PaymentInterest",
				"CurrentBalance", "TotalPayments", "TotalInterestPaid");
				
		// output is in dollars
		formatString = "%1$-20d%2$-20.2f%3$-20.2f%4$.2f,%5$.2f,%6$.2f\n";
		calculator.calculatePaymentSchedule();
		for (AmortizationPayment payment : calculator.getPaymentSchedule())
			printPayment(formatString, payment);
	}

	private static String readLine(String userPrompt) throws IOException {
		String line = "";
		
		if (console != null) {
			line = console.readLine(userPrompt);
		} else {
			// print("console is null\n");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

			print(userPrompt);
			line = bufferedReader.readLine();
		}
		line.trim();
		return line;
	}
	
	private static void print(String s) {
		printf("%s", s);
	}
	
	private static void printf(String formatString, Object... args) {
		
		try {
			if (console != null) {
				console.printf(formatString, args);
			} else {
				System.out.print(String.format(formatString, args));
			}
		} catch (IllegalFormatException e) {
			System.err.print("Error printing...\n");
		}
	}
	
	private void printPayment(String formatString, AmortizationPayment payment) {
		printf(formatString, payment.getPaymentId(), payment.getPaymentAmount(),
			payment.getPaymentInterest(), payment.getCurrentBalance(), 
			payment.getTotalPayments(), payment.getTotalInterestPaid());
	}
	
	public static void main(String [] args) {
		
		String[] userPrompts = {
				"Please enter the amount you would like to borrow: ",
				"Please enter the annual percentage rate used to repay the loan: ",
				"Please enter the term, in years, over which the loan is repaid: "
		};
		
		String line = "";
		long amount = 0;
		double apr = 0;
		int years = 0;
		
		for (int i = 0; i< userPrompts.length; ) {
			String userPrompt = userPrompts[i];
			try {
				line = readLine(userPrompt);
			} catch (IOException e) {
				print("An IOException was encountered. Terminating program.\n");
				return;
			}
			
			boolean isValidValue = true;
			try {
				switch (i) {
				case 0:
					amount = Long.parseLong(line);
					if (AmortizationCalculator.isValidBorrowAmount(amount) == false) {
						isValidValue = false;
						double range[] = AmortizationCalculator.getBorrowAmountRange();
						print("Please enter a positive value between " + range[0] + " and " + range[1] + ". ");
					}
					break;
				case 1:
					apr = Double.parseDouble(line);
					if (AmortizationCalculator.isValidAPRValue(apr) == false) {
						isValidValue = false;
						double range[] = AmortizationCalculator.getAPRRange();
						print("Please enter a positive value between " + range[0] + " and " + range[1] + ". ");
					}
					break;
				case 2:
					years = Integer.parseInt(line);
					if (AmortizationCalculator.isValidTerm(years) == false) {
						isValidValue = false;
						int range[] = AmortizationCalculator.getTermRange();
						print("Please enter a positive integer value between " + range[0] + " and " + range[1] + ". ");
					}
					break;
				}
			} catch (NumberFormatException e) {
				isValidValue = false;
			}
			if (isValidValue) {
				i++;
			} else {
				print("An invalid value was entered.\n");
			}
		}
		
		try {
			AmortizationCalculator calculator = new AmortizationCalculator(amount, apr, years);
			AmortizationScheduler as = new AmortizationScheduler(calculator);
			as.outputAmortizationSchedule();
		} catch (IllegalArgumentException e) {
			print("Unable to process the values entered. Terminating program.\n");
		}
	}
}
