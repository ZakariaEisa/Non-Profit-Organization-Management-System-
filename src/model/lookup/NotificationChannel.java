package model.lookup;

/**
 * CRUD lookup table: communication channels a person can prefer (Email, SMS, Social).
 */
public class NotificationChannel extends LookupEntity {
    public static final String TABLE = "notification_channel";

    public NotificationChannel(int id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public String getTableName() {
        return TABLE;
    }
}
