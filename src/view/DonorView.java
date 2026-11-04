package view;

import controller.DonationController;
import model.donation.Donation;
import model.people.Donor;

/**
 * MVC View: donor pages.
 */
public class DonorView {
    private final DonationController donationController;

    public DonorView(DonationController donationController) {
        this.donationController = donationController;
    }

    public void showDonationForm() {
        System.out.println("=== DONATE ===  method: [Cash | Online | In-Kind | Recurring]  amount: ____  "
                + "campaign: ____  [ ] donate anonymously");
    }

    public void showHistory(Donor donor) {
        System.out.println("  Donation history of " + donor.getFullName() + " (total " + donor.getTotalDonations() + " EGP):");
        for (Donation d : donor.viewDonationHistory()) {
            System.out.println("    - " + d.getDonationDetails());
        }
    }

    public void showReceipt(int donationId) {
        System.out.println(donationController.generateReceipt(donationId));
    }
}
