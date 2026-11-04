package model.notification;

import model.people.Person;

/**
 * STRATEGY pattern: strategy interface. Each channel sends messages its own way.
 */
public interface INotificationStrategy {
    void sendNotification(String message, Person recipient);
}
