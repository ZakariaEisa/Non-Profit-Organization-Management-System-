package model.event;

import java.time.LocalDate;
import model.people.Person;

/**
 * Link between a Person and an Event. Holds the ticket once the seat is confirmed.
 */
public class EventRegistration {
    private int registrationId;
    private LocalDate registrationDate;
    private String registrationStatus = "PENDING";   // CONFIRMED / WAITLISTED / CANCELLED (CR-05)
    private String attendanceStatus = "PENDING";     // PENDING / ATTENDED / ABSENT
    private Person attendee;
    private Event event;
    private Ticket ticket;

    public EventRegistration(int registrationId, Person attendee, Event event) {
        this.registrationId = registrationId;
        this.attendee = attendee;
        this.event = event;
        this.registrationDate = LocalDate.now();
    }

    /** Confirms the seat and attaches the issued ticket. */
    public void register(Ticket ticket) {
        this.ticket = ticket;
        ticket.issue();
        registrationStatus = "CONFIRMED";
    }

    public void waitlist() {
        registrationStatus = "WAITLISTED";
    }

    public void cancelRegistration() {
        registrationStatus = "CANCELLED";
        if (ticket != null) ticket.cancel();
    }

    /** Scans the ticket at the door. */
    public boolean confirmAttendance() {
        if (ticket != null && ticket.validate()) {
            attendanceStatus = "ATTENDED";
            return true;
        }
        return false;
    }

    public boolean isConfirmed() { return "CONFIRMED".equals(registrationStatus); }
    public boolean isWaitlisted() { return "WAITLISTED".equals(registrationStatus); }

    public int getRegistrationId() { return registrationId; }
    public LocalDate getRegistrationDate() { return registrationDate; }
    public String getRegistrationStatus() { return registrationStatus; }
    public String getAttendanceStatus() { return attendanceStatus; }
    public Person getAttendee() { return attendee; }
    public Event getEvent() { return event; }
    public Ticket getTicket() { return ticket; }
}
