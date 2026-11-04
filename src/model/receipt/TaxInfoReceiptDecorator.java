package model.receipt;

/**
 * Adds the tax-deduction information (tax-related document requirement).
 */
public class TaxInfoReceiptDecorator extends ReceiptDecorator {
    public TaxInfoReceiptDecorator(IReceipt wrapped) {
        super(wrapped);
    }

    @Override
    public String generateReceipt() {
        return super.generateReceipt() + "\nTax-deductible amount: " + getDonation().getAmount()
                + " EGP (keep this receipt for your tax records)";
    }
}
