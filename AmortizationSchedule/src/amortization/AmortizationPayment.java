package amortization;

/**
 * The amortization payment is the scheduled monthly payment with detailed information as follows:<br>
 * paymentAmount: the amount of current month payment<br>
 * paymentInterest: the amount of current month interest<br>
 * currentBalace: the amount of current month balance<br>
 * totalPaymets: the total amount paid<br>
 * totalInterestPaid: the total interest amount paid<br>
 * 
 * @author ray
 *
 */
public class AmortizationPayment {
	private int paymentId;
	private double paymentAmount;
	private double paymentInterest;
	private double currentBalance;
	private double totalPayments;
	private double totalInterestPaid;

	public AmortizationPayment(int paymentId, double paymentAmount, double paymentInterest, double currentBalance,
		double totalPayments, double totalInterestPaid) {
		this.paymentId = paymentId;
		this.paymentAmount = paymentAmount;
		this.paymentInterest = paymentInterest;
		this.currentBalance = currentBalance;
		this.totalPayments = totalPayments;
		this.totalInterestPaid = totalInterestPaid;
	}
	
	public int getPaymentId() {
		return paymentId;
	}
	
	public double getPaymentAmount() {
		return paymentAmount;
	}
	
	public double getPaymentInterest() {
		return paymentInterest;
	}
	
	public double getCurrentBalance() {
		return currentBalance;
	}
	
	public double getTotalPayments() {
		return totalPayments;
	}
	
	public double getTotalInterestPaid() {
		return totalInterestPaid;
	}
}
