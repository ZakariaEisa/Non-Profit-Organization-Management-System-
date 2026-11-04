package model.donation;

import java.time.LocalDate;

/**
 * CR-01: a donation that is charged automatically every period (monthly by default).
 */
public class RecurringDonation extends Donation {
    private String frequency = "MONTHLY";
    private LocalDate nextChargeDate;
    private boolean activeSubscription = true;

    public RecurringDonation(int donationId, double amount) {
        super(donationId, amount);
    }

    @Override
    public void processDonation() {
        if (!activeSubscription) {
            System.out.println("  Recurring donation #" + getDonationId() + " is cancelled, nothing charged.");
            return;
        }
        nextChargeDate = "WEEKLY".equals(frequency) ? LocalDate.now().plusWeeks(1) : LocalDate.now().plusMonths(1);
        System.out.println("  Charged " + frequency.toLowerCase() + " payment, next charge on " + nextChargeDate);
        markCompleted();
    }

    @Override
    public String getMethod() { return "Recurring"; }

    public void cancelSubscription() { activeSubscription = false; }

    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public LocalDate getNextChargeDate() { return nextChargeDate; }
    public boolean isActiveSubscription() { return activeSubscription; }
}
