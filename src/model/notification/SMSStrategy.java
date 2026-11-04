package model.notification;

import model.people.Person;

public class SMSStrategy implements INotificationStrategy {
    private static final int MAX_LENGTH = 160;

    @Override
    public void sendNotification(String message, Person recipient) {
        // Phase 2: send through an SMS gateway
        String text = message.replace("\n", " ");
        if (text.length() > MAX_LENGTH) text = text.substring(0, MAX_LENGTH - 3) + "...";
        System.out.println("  [SMS -> " + recipient.getPhoneNumber() + "] " + text);
    }
}
