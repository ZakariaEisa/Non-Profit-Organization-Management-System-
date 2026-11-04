package view;

import controller.BeneficiaryController;
import model.beneficiary.BeneficiaryNeed;
import model.beneficiary.ResourceDistribution;
import model.people.Beneficiary;

/**
 * MVC View: beneficiary pages.
 */
public class BeneficiaryView {
    private final BeneficiaryController beneficiaryController;

    public BeneficiaryView(BeneficiaryController beneficiaryController) {
        this.beneficiaryController = beneficiaryController;
    }

    public void showNeeds(Beneficiary beneficiary) {
        System.out.println("  Needs of " + beneficiary.getFullName() + " (eligible: "
                + beneficiaryController.checkEligibility(beneficiary) + "):");
        for (BeneficiaryNeed n : beneficiary.getNeeds()) {
            System.out.println("    - " + n.getNeedDetails());
        }
    }

    public void showDistributions(Beneficiary beneficiary) {
        System.out.println("  Distributions to " + beneficiary.getFullName() + ":");
        for (ResourceDistribution d : beneficiary.viewDistributionHistory()) {
            System.out.println("    - " + d.getDistributionDetails());
        }
    }
}
