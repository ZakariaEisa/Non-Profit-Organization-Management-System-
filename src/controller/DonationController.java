package controller;

import java.util.HashMap;
import java.util.Map;
import factory.DonationFactory;
import model.donation.Campaign;
import model.donation.Donation;
import model.lookup.DonationType;
import model.notification.NotificationService;
import model.observer.AdminDonationObserver;
import model.observer.CampaignProgressObserver;
import model.observer.DonorReceiptObserver;
import model.observer.FinanceDepartmentObserver;
import model.observer.IDonationObserver;
import model.people.Donor;
import model.receipt.BasicReceipt;
import model.receipt.IReceipt;
import model.receipt.QRCodeReceiptDecorator;
import model.receipt.TaxInfoReceiptDecorator;

public class DonationController extends BaseController {
    private final DonationFactory donationFactory = new DonationFactory();
    private final Map<Integer, Donation> donations = new HashMap<>();
    private final IDonationObserver[] observers;

    public DonationController(NotificationService notificationService) {
        observers = new IDonationObserver[] {
                new AdminDonationObserver(),
                new FinanceDepartmentObserver(),
                new DonorReceiptObserver(notificationService),
                new CampaignProgressObserver()
        };
    }

    /** Factory creates the object, then every observer is attached to it. */
    public Donation createDonation(String type, int donationId, double amount, Donor donor,
                                   DonationType donationType, Campaign campaign, boolean anonymous) {
        Donation donation = donationFactory.createDonation(type, donationId, amount);
        donation.setDonationType(donationType);
        donation.setCampaign(campaign);
        donation.setAnonymous(anonymous);
        if (donor != null) donor.makeDonation(donation);
        for (IDonationObserver observer : observers) donation.attach(observer);
        donations.put(donationId, donation);
        return donation;
    }

    public void processDonation(int donationId) {
        find(donationId).processDonation();
    }

    /** Decorator: plain receipt + tax info + QR code. */
    public String generateReceipt(int donationId) {
        IReceipt receipt = new QRCodeReceiptDecorator(new TaxInfoReceiptDecorator(new BasicReceipt(find(donationId))));
        return receipt.generateReceipt();
    }

    public Donation find(int donationId) {
        Donation donation = donations.get(donationId);
        if (donation == null) throw new IllegalArgumentException("No donation with id " + donationId);
        return donation;
    }
}
