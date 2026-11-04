package controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import model.event.Event;
import model.event.EventRegistration;
import model.notification.NotificationService;
import model.observer.EventReminderObserver;
import model.people.Person;

public class EventController extends BaseController {
    private final Map<Integer, Event> events = new HashMap<>();
    private final EventReminderObserver reminderObserver;

    public EventController(NotificationService notificationService) {
        reminderObserver = new EventReminderObserver(notificationService);
    }

    public void createEvent(Event event) {
        event.createEvent();
        event.attach(reminderObserver);
        events.put(event.getEventId(), event);
    }

    public EventRegistration registerAttendee(int eventId, Person attendee) {
        return find(eventId).registerAttendee(attendee);
    }

    public EventRegistration cancelRegistration(int eventId, EventRegistration registration) {
        return find(eventId).cancelRegistration(registration);
    }

    public void updateEvent(int eventId, String location, LocalDate date) {
        find(eventId).updateEvent(location, date);
    }

    public void cancelEvent(int eventId) {
        find(eventId).cancelEvent();
    }

    public Event find(int eventId) {
        Event event = events.get(eventId);
        if (event == null) throw new IllegalArgumentException("No event with id " + eventId);
        return event;
    }
}
