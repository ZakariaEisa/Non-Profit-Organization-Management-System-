package model.donation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.lookup.DonationType;
import model.observer.IDonationObserver;
import model.people.Donor;

/**
 * Abstract parent of all donations. Also the SUBJECT of the Observer pattern:
 * when a donation is processed every attached observer is notified.
 */
public abstract class Donation {
    private int donationId;
    private double amount;
    private LocalDate donationDate;
    private String donationStatus = "PENDING";
    private DonationType donationType;
    private Donor donor;
    private Campaign campaign;        // CR-02 (optional)
    private boolean anonymous;        // CR-03
    private final List<IDonationObserver> observers = new ArrayList<>();

    protected Donation(int donationId, double amount) {
        this.donationId = donationId;
        this.amount = amount;
        this.donationDate = LocalDate.now();
    }

    /** Each donation method processes the payment in its own way. */
    public abstract void processDonation();

    /** Short name of the donation method, e.g. "Cash". */
    public abstract String getMethod();

    /** Called by subclasses once their own processing succeeded. */
    protected void markCompleted() {
        donationStatus = "COMPLETED";
        notifyObservers();
    }

    public String getDonationDetails() {
        return "Donation #" + donationId + " [" + getMethod() + "] " + amount + " EGP by "
                + getDonorDisplayName() + " on " + donationDate + " - " + donationStatus;
    }

    /** CR-03: anonymous donors are never shown by name. */
    public String getDonorDisplayName() {
        if (anonymous || donor == null) return "Anonymous donor";
        return donor.getFullName();
    }

    // ---------------- Observer (subject side) ----------------
    public void attach(IDonationObserver observer) {
        if (!observers.contains(observer)) observers.add(observer);
    }

    public void detach(IDonationObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (IDonationObserver observer : observers) {
            observer.update(this);
        }
    }

    // ---------------- getters / setters ----------------
    public int getDonationId() { return donationId; }
    public double getAmount() { return amount; }
    protected void setAmount(double amount) { this.amount = amount; }
    public LocalDate getDonationDate() { return donationDate; }
    public String getDonationStatus() { return donationStatus; }
    public DonationType getDonationType() { return donationType; }
    public void setDonationType(DonationType donationType) { this.donationType = donationType; }
    public Donor getDonor() { return donor; }
    public void setDonor(Donor donor) { this.donor = donor; }
    public Campaign getCampaign() { return campaign; }
    public void setCampaign(Campaign campaign) { this.campaign = campaign; }
    public boolean isAnonymous() { return anonymous; }
    public void setAnonymous(boolean anonymous) { this.anonymous = anonymous; }
}
