package model.donation;

/** Card / wallet payment through an online payment gateway. */
public class OnlineDonation extends Donation {
    private String transactionId;
    private String paymentGateway = "Paymob";

    public OnlineDonation(int donationId, double amount) {
        super(donationId, amount);
    }

    @Override
    public void processDonation() {
        transactionId = "TX-" + getDonationId() + "-" + (System.currentTimeMillis() % 100000);
        System.out.println("  Charged online via " + paymentGateway + " (" + transactionId + ")");
        markCompleted();
    }

    @Override
    public String getMethod() { return "Online"; }

    public String getTransactionId() { return transactionId; }
    public String getPaymentGateway() { return paymentGateway; }
    public void setPaymentGateway(String paymentGateway) { this.paymentGateway = paymentGateway; }
}
