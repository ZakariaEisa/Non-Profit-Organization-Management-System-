package model.lookup;

/**
 * CRUD lookup table: kinds of events (e.g. Fundraiser, Workshop, Outreach).
 */
public class EventType extends LookupEntity {
    public static final String TABLE = "event_type";

    public EventType(int id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public String getTableName() {
        return TABLE;
    }
}
