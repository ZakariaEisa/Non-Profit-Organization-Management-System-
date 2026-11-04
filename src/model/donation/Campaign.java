package model.donation;

import java.time.LocalDate;

/**
 * CR-02: a fundraising campaign with a target amount.
 * raisedAmount is updated automatically by CampaignProgressObserver.
 */
public class Campaign {
    private int campaignId;
    private String title;
    private double goalAmount;
    private double raisedAmount;
    private LocalDate startDate;
    private LocalDate endDate;

    public Campaign(int campaignId, String title, double goalAmount, LocalDate startDate, LocalDate endDate) {
        this.campaignId = campaignId;
        this.title = title;
        this.goalAmount = goalAmount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void addContribution(double amount) {
        raisedAmount += amount;
    }

    public double getProgressPercentage() {
        return goalAmount == 0 ? 0 : Math.min(100.0, raisedAmount * 100.0 / goalAmount);
    }

    public boolean isGoalReached() {
        return raisedAmount >= goalAmount;
    }

    public int getCampaignId() { return campaignId; }
    public String getTitle() { return title; }
    public double getGoalAmount() { return goalAmount; }
    public double getRaisedAmount() { return raisedAmount; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
}
