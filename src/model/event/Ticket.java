package model.event;

import java.time.LocalDate;

/**
 * Event ticket issued to a confirmed registration (ticketing requirement).
 */
public class Ticket {
    private int ticketId;
    private String ticketCode;
    private double price;
    private LocalDate issueDate;
    private String status = "NEW";   // VALID / USED / CANCELLED

    public Ticket(int ticketId, int eventId, double price) {
        this.ticketId = ticketId;
        this.price = price;
        this.ticketCode = "EVT" + eventId + "-T" + ticketId;
    }

    public void issue() {
        issueDate = LocalDate.now();
        status = "VALID";
    }

    /** A valid ticket can be used once at the entrance. */
    public boolean validate() {
        if (!"VALID".equals(status)) return false;
        status = "USED";
        return true;
    }

    public void cancel() {
        status = "CANCELLED";
    }

    public int getTicketId() { return ticketId; }
    public String getTicketCode() { return ticketCode; }
    public double getPrice() { return price; }
    public LocalDate getIssueDate() { return issueDate; }
    public String getStatus() { return status; }
}
