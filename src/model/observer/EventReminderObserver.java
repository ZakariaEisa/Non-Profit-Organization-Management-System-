package model.observer;

import model.event.Event;
import model.event.EventRegistration;
import model.notification.NotificationService;

/**
 * Informs every registered attendee when an event changes or is cancelled.
 */
public class EventReminderObserver implements IEventObserver {
    private final NotificationService notificationService;

    public EventReminderObserver(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void update(Event event) {
        String message = "Event '" + event.getEventName() + "' is now " + event.getStatus()
                + " - " + event.getLocation() + " on " + event.getEventDate();
        for (EventRegistration registration : event.getRegistrations()) {
            if (registration.isConfirmed() || registration.isWaitlisted()) {
                notificationService.sendUsingPreferredChannel(message, registration.getAttendee());
            }
        }
    }
}
