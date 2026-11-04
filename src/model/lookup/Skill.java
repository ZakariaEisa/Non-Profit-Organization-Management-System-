package model.lookup;

/**
 * CRUD lookup table: volunteer skills (e.g. First Aid, Teaching, Driving).
 */
public class Skill extends LookupEntity {
    public static final String TABLE = "skill";

    public Skill(int id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public String getTableName() {
        return TABLE;
    }
}
