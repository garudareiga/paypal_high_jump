package amortization;

import java.util.*;

public class AmortizationCalculator {
	///** the type of amortization calculator */
	//private AmortizationCalculatorType calculatorType = AmortizationCalculatorType.MONTHLY;
	/** the amount borrowed in cents */
	private long amountBorrowed = 0;
	/** the annual interest rate */
	private double apr = 0d;
	/** the term, in years, over which the load is repaid */
	private int initialTermMonths = 0;
	
	/** range of the amount borrowed */
	private static final double[] borrowAmountRange = new double[] { 0.01d, 1000000000000d };
	/** range of the annual interest rate */
	private static final double[] aprRange = new double[] { 0.000001d, 100d };
	/** range of the term in years */
	private static final int[] termRange = new int[] { 1, 1000000 };
	
	private final static double MONTHLY_INTEREST_DIVISOR = 12d * 100d;
	private final static int MONTHS_OF_YEAR = 12;
	
	private double monthlyInterest = 0d;
	private long monthlyPaymentAmount = 0;	// in cents
	
	private ArrayList<AmortizationPayment> payments = new ArrayList<AmortizationPayment>();
	
	public AmortizationCalculator(double amount, double interestRate, int years) 
			throws IllegalArgumentException {
		if ((isValidBorrowAmount(amount) == false) ||
			(isValidAPRValue(interestRate) == false) ||
			(isValidTerm(years) == false)) {
			throw new IllegalArgumentException();
		}
		
		amountBorrowed = Math.round(amount * 100);
		apr = interestRate;
		initialTermMonths = years * MONTHS_OF_YEAR;
	}
	
	ArrayList<AmortizationPayment> getPayments() {
		return payments;
	}
	
	public static boolean isValidBorrowAmount(double amount) {
		double range[] = getBorrowAmountRange();
		return ((range[0] <= amount) && (amount <= range[1]));
	}
	
	public static boolean isValidAPRValue(double rate) {
		double range[] = getAPRRange();
		return ((range[0] <= rate) && (rate <= range[1]));
	}
	
	public static boolean isValidTerm(int years) {
		int range[] = getTermRange();
		return ((range[0] <= years) && (years <= range[1]));
	}
	
	public static final double[] getBorrowAmountRange() {
		return borrowAmountRange;
	}
	
	public static final double[] getAPRRange() {
		return aprRange;
	}

	public static final int[] getTermRange() {
		return termRange;
	}
	
	/**
	 * Calculate Monthly Payment as follows:
	 * M = P * (J / (1 - (Math.pow(1/(1 + J), N))));
	 * Where:
	 * P = Principal
	 * I = Interest
	 * J = Monthly Interest in decimal form:  I / (12 * 100)
	 * N = Number of months of loan
	 * M = Monthly Payment Amount
	 */
	private void calculateMonthlyPayment() {
		// calculate J
		monthlyInterest = apr / MONTHLY_INTEREST_DIVISOR;
		
		// this is 1 / (1 + J)
		double tmp = Math.pow(1d + monthlyInterest, -1);
		
		// this is Math.pow(1/(1 + J), N)
		tmp = Math.pow(tmp, initialTermMonths);
		
		// this is 1 / (1 - (Math.pow(1/(1 + J), N))))
		tmp = Math.pow(1d - tmp, -1);
		
		// M = P * (J / (1 - (Math.pow(1/(1 + J), N))));
		double rc = amountBorrowed * monthlyInterest * tmp;
		
		//return Math.round(rc);
		monthlyPaymentAmount = Math.round(rc);
		
		// the following shouldn't happen with the available valid ranges
		// for borrow amount, apr, and term; however, without range validation,
		// monthlyPaymentAmount as calculated by calculateMonthlyPayment()
		// may yield incorrect values with extreme input values
		if (monthlyPaymentAmount > amountBorrowed) {
			throw new IllegalArgumentException();
		}
	}

	public void calculatePayment() {
		calculateMonthlyPayment();
		
		payments.clear();
		
		long balance = (long)amountBorrowed;
		int paymentNumber = 0;
		long totalPayments = 0;
		long totalInterestPaid = 0;
	
		payments.add(new AmortizationPayment(0d, 0d, amountBorrowed/100d,
			totalPayments/100d, totalInterestPaid/100d));
	
		final int maxNumberOfPayments = initialTermMonths + 1;
		while ((balance > 0) && (paymentNumber <= maxNumberOfPayments)) {
			// Calculate H = P x J, this is your current monthly interest
			long curMonthlyInterest = Math.round(((double) balance) * monthlyInterest);

			// the amount required to payoff the loan
			long curPayoffAmount = balance + curMonthlyInterest;
			
			// the amount to payoff the remaining balance may be less than the calculated monthlyPaymentAmount
			long curMonthlyPaymentAmount = Math.min(monthlyPaymentAmount, curPayoffAmount);
			
			// it's possible that the calculated monthlyPaymentAmount is 0,
			// or the monthly payment only covers the interest payment - i.e. no principal
			// so the last payment needs to payoff the loan
			if ((paymentNumber == maxNumberOfPayments) &&
					((curMonthlyPaymentAmount == 0) || (curMonthlyPaymentAmount == curMonthlyInterest))) {
				curMonthlyPaymentAmount = curPayoffAmount;
			}
			
			// Calculate C = M - H, this is your monthly payment minus your monthly interest,
			// so it is the amount of principal you pay for that month
			long curMonthlyPrincipalPaid = curMonthlyPaymentAmount - curMonthlyInterest;
			
			// Calculate Q = P - C, this is the new balance of your principal of your loan.
			long curBalance = balance - curMonthlyPrincipalPaid;
			
			totalPayments += curMonthlyPaymentAmount;
			totalInterestPaid += curMonthlyInterest;
								
			payments.add(new AmortizationPayment(curMonthlyPaymentAmount/100d, 
				curMonthlyInterest/100d, curBalance/100d, totalPayments/100d, totalInterestPaid/100d));
			
			// Set P equal to Q and go back to Step 1: You thusly loop around until the value Q (and hence P) goes to zero.
			balance = curBalance;
		}
	}
}
