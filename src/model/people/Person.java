package model.people;

import model.lookup.NotificationChannel;

/**
 * Abstract parent of Donor, Volunteer and Beneficiary.
 */
public abstract class Person {
    private int personId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    /** CR-04: the channel this person wants to be contacted on. */
    private NotificationChannel preferredChannel;

    protected Person(int personId, String fullName, String email, String phoneNumber, String address) {
        this.personId = personId;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public void updateProfile(String email, String phoneNumber, String address) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getContactInformation() {
        return fullName + " | " + email + " | " + phoneNumber + " | " + address;
    }

    public int getPersonId() { return personId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getAddress() { return address; }
    public NotificationChannel getPreferredChannel() { return preferredChannel; }
    public void setPreferredChannel(NotificationChannel preferredChannel) { this.preferredChannel = preferredChannel; }
}
