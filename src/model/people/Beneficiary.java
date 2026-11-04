package model.people;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.beneficiary.BeneficiaryNeed;
import model.beneficiary.ResourceDistribution;
import model.lookup.BeneficiaryCategory;

public class Beneficiary extends Person {
    private LocalDate registrationDate;
    private String eligibilityStatus = "PENDING";
    private BeneficiaryCategory category;
    private final List<BeneficiaryNeed> needs = new ArrayList<>();
    private final List<ResourceDistribution> distributions = new ArrayList<>();

    public Beneficiary(int personId, String fullName, String email, String phoneNumber, String address,
                       BeneficiaryCategory category) {
        super(personId, fullName, email, phoneNumber, address);
        this.category = category;
        this.registrationDate = LocalDate.now();
    }

    public void updateNeeds(BeneficiaryNeed need) {
        needs.add(need);
    }

    public void receive(ResourceDistribution distribution) {
        distributions.add(distribution);
    }

    public List<ResourceDistribution> viewDistributionHistory() {
        return Collections.unmodifiableList(distributions);
    }

    /** Eligible when the beneficiary belongs to an active category and has at least one need. */
    public boolean checkEligibility() {
        boolean eligible = category != null && category.isActive() && !needs.isEmpty();
        eligibilityStatus = eligible ? "ELIGIBLE" : "NOT_ELIGIBLE";
        return eligible;
    }

    public LocalDate getRegistrationDate() { return registrationDate; }
    public String getEligibilityStatus() { return eligibilityStatus; }
    public BeneficiaryCategory getCategory() { return category; }
    public List<BeneficiaryNeed> getNeeds() { return Collections.unmodifiableList(needs); }
}
