package model.receipt;

import model.donation.Donation;

/**
 * DECORATOR pattern: component interface.
 */
public interface IReceipt {
    String generateReceipt();

    Donation getDonation();
}
