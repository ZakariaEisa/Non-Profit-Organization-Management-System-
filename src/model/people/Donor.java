package model.people;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.donation.Donation;

public class Donor extends Person {
    private String donorType;          // Individual / Corporate
    private double totalDonations;
    private final List<Donation> donations = new ArrayList<>();

    public Donor(int personId, String fullName, String email, String phoneNumber, String address, String donorType) {
        super(personId, fullName, email, phoneNumber, address);
        this.donorType = donorType;
    }

    public void makeDonation(Donation donation) {
        donation.setDonor(this);
        donations.add(donation);
        totalDonations += donation.getAmount();
    }

    public List<Donation> viewDonationHistory() {
        return Collections.unmodifiableList(donations);
    }

    public String getDonorType() { return donorType; }
    public double getTotalDonations() { return totalDonations; }
}
