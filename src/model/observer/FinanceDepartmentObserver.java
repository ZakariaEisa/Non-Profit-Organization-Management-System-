package model.observer;

import model.donation.Donation;

/**
 * Keeps the finance department's running total up to date.
 */
public class FinanceDepartmentObserver implements IDonationObserver {
    private double totalReceived;

    @Override
    public void update(Donation donation) {
        totalReceived += donation.getAmount();
        System.out.println("  [Finance] recorded " + donation.getAmount() + " EGP, total = " + totalReceived);
    }

    public double getTotalReceived() {
        return totalReceived;
    }
}
