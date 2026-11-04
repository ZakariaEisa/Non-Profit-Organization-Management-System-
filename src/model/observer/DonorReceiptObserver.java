package model.observer;

import model.donation.Donation;
import model.notification.NotificationService;
import model.receipt.BasicReceipt;
import model.receipt.IReceipt;
import model.receipt.TaxInfoReceiptDecorator;

/**
 * Sends the donor a receipt (built with the Decorator pattern) on their preferred channel.
 */
public class DonorReceiptObserver implements IDonationObserver {
    private final NotificationService notificationService;

    public DonorReceiptObserver(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void update(Donation donation) {
        if (donation.getDonor() == null) return;
        IReceipt receipt = new TaxInfoReceiptDecorator(new BasicReceipt(donation));
        notificationService.sendUsingPreferredChannel(receipt.generateReceipt(), donation.getDonor());
    }
}
