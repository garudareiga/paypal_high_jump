package amortization;

public class AmortizationPayment {
	static int paymentNumber = 0;
	private int paymentId;
	private double paymentAmount;
	private double paymentInterest;
	private double currentBalance;
	private double totalPayments;
	private double totalInterestPaid;

	public AmortizationPayment(double paymentAmount, double paymentInterest, double currentBalance,
		double totalPayments, double totalInterestPaid) {
		this.paymentId = ++paymentNumber;
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
