package model.receipt;

import model.donation.Donation;

/**
 * DECORATOR pattern: base decorator, wraps another IReceipt and forwards to it.
 */
public abstract class ReceiptDecorator implements IReceipt {
    protected IReceipt wrapped;

    protected ReceiptDecorator(IReceipt wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public String generateReceipt() {
        return wrapped.generateReceipt();
    }

    @Override
    public Donation getDonation() {
        return wrapped.getDonation();
    }
}
