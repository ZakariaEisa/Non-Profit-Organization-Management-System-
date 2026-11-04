package factory;

import model.donation.CashDonation;
import model.donation.Donation;
import model.donation.InKindDonation;
import model.donation.OnlineDonation;
import model.donation.RecurringDonation;

/**
 * FACTORY pattern: creates the right Donation subclass from the method the user picked.
 * Adding a new donation method only needs a new subclass and one new case here.
 */
public class DonationFactory {
    public Donation createDonation(String type, int donationId, double amount) {
        switch (type.trim().toLowerCase()) {
            case "cash":
            case "check":
                return new CashDonation(donationId, amount);
            case "online":
                return new OnlineDonation(donationId, amount);
            case "inkind":
            case "in-kind":
                return new InKindDonation(donationId, amount);
            case "recurring":
                return new RecurringDonation(donationId, amount);
            default:
                throw new IllegalArgumentException("Unknown donation type: " + type);
        }
    }
}
