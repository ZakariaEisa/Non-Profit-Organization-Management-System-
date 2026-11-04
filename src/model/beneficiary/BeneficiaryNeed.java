package model.beneficiary;

/**
 * Something a beneficiary needs (food, medicine, school fees ...).
 */
public class BeneficiaryNeed {
    private int needId;
    private String description;
    private String priority;   // LOW / MEDIUM / HIGH
    private int quantity;

    public BeneficiaryNeed(int needId, String description, String priority, int quantity) {
        this.needId = needId;
        this.description = description;
        this.priority = priority;
        this.quantity = quantity;
    }

    public void updateNeed(String priority, int quantity) {
        this.priority = priority;
        this.quantity = quantity;
    }

    public String getNeedDetails() {
        return "Need #" + needId + ": " + description + " x" + quantity + " [" + priority + "]";
    }

    public int getNeedId() { return needId; }
    public String getDescription() { return description; }
    public String getPriority() { return priority; }
    public int getQuantity() { return quantity; }
}
