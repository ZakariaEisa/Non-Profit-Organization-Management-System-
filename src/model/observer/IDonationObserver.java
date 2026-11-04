package model.observer;

import model.donation.Donation;

/**
 * OBSERVER pattern: observer interface for donations.
 */
public interface IDonationObserver {
    void update(Donation donation);
}
