package model.observer;

import model.donation.Campaign;
import model.donation.Donation;

/**
 * CR-02: adds every processed donation to its campaign's progress.
 */
public class CampaignProgressObserver implements IDonationObserver {
    @Override
    public void update(Donation donation) {
        Campaign campaign = donation.getCampaign();
        if (campaign == null) return;
        campaign.addContribution(donation.getAmount());
        System.out.printf("  [Campaign] '%s' %.0f / %.0f EGP (%.1f%%)%n", campaign.getTitle(),
                campaign.getRaisedAmount(), campaign.getGoalAmount(), campaign.getProgressPercentage());
    }
}
