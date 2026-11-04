package model.donation;

/** Goods (clothes, food, equipment...) instead of money. Amount = estimated value. */
public class InKindDonation extends Donation {
    private String itemDescription;
    private double estimatedValue;

    public InKindDonation(int donationId, double estimatedValue) {
        super(donationId, estimatedValue);
        this.estimatedValue = estimatedValue;
    }

    @Override
    public void processDonation() {
        System.out.println("  Items received: " + itemDescription + " (est. " + estimatedValue + " EGP)");
        markCompleted();
    }

    @Override
    public String getMethod() { return "In-Kind"; }

    public String getItemDescription() { return itemDescription; }
    public void setItemDescription(String itemDescription) { this.itemDescription = itemDescription; }
    public double getEstimatedValue() { return estimatedValue; }
    public void setEstimatedValue(double estimatedValue) {
        this.estimatedValue = estimatedValue;
        setAmount(estimatedValue);
    }
}
