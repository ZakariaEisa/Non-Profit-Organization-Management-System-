package model.notification;

import model.people.Person;

public class SocialMediaStrategy implements INotificationStrategy {
    @Override
    public void sendNotification(String message, Person recipient) {
        // Phase 2: post through the social media platform API
        String to = recipient == null ? "public page" : "@" + recipient.getFullName().replace(" ", "").toLowerCase();
        System.out.println("  [SOCIAL -> " + to + "] " + message.replace("\n", " | "));
    }
}
