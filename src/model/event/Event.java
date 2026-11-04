package model.event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.lookup.EventType;
import model.observer.IEventObserver;
import model.people.Person;

/**
 * An event (fundraiser, workshop, outreach...). Also a SUBJECT of the Observer pattern:
 * registered attendees are reminded/informed whenever the event changes.
 */
public class Event {
    private int eventId;
    private String eventName;
    private String description;
    private String location;
    private LocalDate eventDate;
    private int capacity;
    private double ticketPrice;
    private String status = "DRAFT";
    private EventType eventType;
    private final List<EventRegistration> registrations = new ArrayList<>();
    private final List<Task> tasks = new ArrayList<>();
    private final List<IEventObserver> observers = new ArrayList<>();
    private int nextRegistrationId = 1;

    public Event(int eventId, String eventName, String description, String location,
                 LocalDate eventDate, int capacity, double ticketPrice, EventType eventType) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.description = description;
        this.location = location;
        this.eventDate = eventDate;
        this.capacity = capacity;
        this.ticketPrice = ticketPrice;
        this.eventType = eventType;
    }

    public void createEvent() {
        status = "SCHEDULED";
    }

    public void updateEvent(String newLocation, LocalDate newDate) {
        this.location = newLocation;
        this.eventDate = newDate;
        status = "UPDATED";
        notifyObservers();
    }

    public void cancelEvent() {
        status = "CANCELLED";
        notifyObservers();
    }

    /**
     * CR-05: confirmed (with a ticket) while seats are free, otherwise put on the waiting list.
     */
    public EventRegistration registerAttendee(Person attendee) {
        EventRegistration registration = new EventRegistration(nextRegistrationId++, attendee, this);
        if (getConfirmedCount() < capacity) {
            registration.register(new Ticket(registration.getRegistrationId(), eventId, ticketPrice));
        } else {
            registration.waitlist();
        }
        registrations.add(registration);
        return registration;
    }

    /** CR-05: cancelling frees a seat, so the first waitlisted person is promoted automatically. */
    public EventRegistration cancelRegistration(EventRegistration registration) {
        boolean wasConfirmed = registration.isConfirmed();
        registration.cancelRegistration();
        if (!wasConfirmed) return null;
        for (EventRegistration r : registrations) {
            if (r.isWaitlisted()) {
                r.register(new Ticket(r.getRegistrationId(), eventId, ticketPrice));
                return r;
            }
        }
        return null;
    }

    /** Number of people who actually attended. */
    public int trackAttendance() {
        int attended = 0;
        for (EventRegistration r : registrations) {
            if ("ATTENDED".equals(r.getAttendanceStatus())) attended++;
        }
        return attended;
    }

    public int getConfirmedCount() {
        int count = 0;
        for (EventRegistration r : registrations) if (r.isConfirmed()) count++;
        return count;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    // ---------------- Observer (subject side) ----------------
    public void attach(IEventObserver observer) {
        if (!observers.contains(observer)) observers.add(observer);
    }

    public void detach(IEventObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (IEventObserver observer : observers) {
            observer.update(this);
        }
    }

    public int getEventId() { return eventId; }
    public String getEventName() { return eventName; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public LocalDate getEventDate() { return eventDate; }
    public int getCapacity() { return capacity; }
    public double getTicketPrice() { return ticketPrice; }
    public String getStatus() { return status; }
    public EventType getEventType() { return eventType; }
    public List<EventRegistration> getRegistrations() { return Collections.unmodifiableList(registrations); }
    public List<Task> getTasks() { return Collections.unmodifiableList(tasks); }
}
