package model.receipt;

import model.donation.Donation;

/**
 * DECORATOR pattern: concrete component - the plain receipt.
 */
public class BasicReceipt implements IReceipt {
    private final Donation donation;

    public BasicReceipt(Donation donation) {
        this.donation = donation;
    }

    @Override
    public String generateReceipt() {
        return "RECEIPT #" + donation.getDonationId()
                + "\nDonor: " + donation.getDonorDisplayName()
                + "\nMethod: " + donation.getMethod()
                + "\nAmount: " + donation.getAmount() + " EGP"
                + "\nDate: " + donation.getDonationDate();
    }

    @Override
    public Donation getDonation() {
        return donation;
    }
}
