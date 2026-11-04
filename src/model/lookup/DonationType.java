package model.lookup;

/**
 * CRUD lookup table: kinds of donations (e.g. General, Zakat, Education Fund).
 */
public class DonationType extends LookupEntity {
    public static final String TABLE = "donation_type";

    public DonationType(int id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public String getTableName() {
        return TABLE;
    }
}
