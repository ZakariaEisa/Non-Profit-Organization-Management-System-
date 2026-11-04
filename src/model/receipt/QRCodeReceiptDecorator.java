package model.receipt;

/**
 * Adds a QR verification code to the receipt.
 */
public class QRCodeReceiptDecorator extends ReceiptDecorator {
    public QRCodeReceiptDecorator(IReceipt wrapped) {
        super(wrapped);
    }

    @Override
    public String generateReceipt() {
        return super.generateReceipt() + "\nQR verify code: NPO-RCPT-" + getDonation().getDonationId();
    }
}
