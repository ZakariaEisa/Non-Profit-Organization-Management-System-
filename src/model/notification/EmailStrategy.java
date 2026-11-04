package model.notification;

import model.people.Person;

public class EmailStrategy implements INotificationStrategy {
    @Override
    public void sendNotification(String message, Person recipient) {
        // Phase 2: send through an SMTP / mail API
        System.out.println("  [EMAIL -> " + recipient.getEmail() + "] " + message.replace("\n", " | "));
    }
}
