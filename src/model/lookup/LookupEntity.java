package model.lookup;

import java.util.Objects;

/**
 * Base class of every CRUD lookup table (DonationType, EventType, Skill, ...).
 * Each subclass is one small table in the database (see database/schema.sql)
 * and is managed by the admin through LookupController (Create, Read, Update, Delete).
 */
public abstract class LookupEntity {
    private int id;
    private String name;
    private String description;
    private boolean active = true;

    protected LookupEntity(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /** Name of the database table this lookup value is stored in. */
    public abstract String getTableName();

    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LookupEntity)) return false;
        LookupEntity other = (LookupEntity) o;
        return id == other.id && getTableName().equals(other.getTableName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTableName(), id);
    }

    @Override
    public String toString() {
        return getTableName() + "#" + id + " " + name + (active ? "" : " (inactive)");
    }
}
