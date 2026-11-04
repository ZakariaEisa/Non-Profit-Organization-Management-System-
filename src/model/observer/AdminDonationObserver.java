package model.observer;

import model.donation.Donation;

/**
 * Shows every new donation on the admin dashboard.
 */
public class AdminDonationObserver implements IDonationObserver {
    @Override
    public void update(Donation donation) {
        System.out.println("  [Admin dashboard] " + donation.getDonationDetails());
    }
}
