package model.donation;

/** Cash or check handed to a cashier at the office. */
public class CashDonation extends Donation {
    private String cashierName;

    public CashDonation(int donationId, double amount) {
        super(donationId, amount);
    }

    @Override
    public void processDonation() {
        System.out.println("  Cash received by " + (cashierName == null ? "front desk" : cashierName));
        markCompleted();
    }

    @Override
    public String getMethod() { return "Cash"; }

    public String getCashierName() { return cashierName; }
    public void setCashierName(String cashierName) { this.cashierName = cashierName; }
}
