package model.lookup;

/**
 * CRUD lookup table: resources given to beneficiaries (e.g. Food Basket, Medicine, School Kit).
 */
public class ResourceType extends LookupEntity {
    public static final String TABLE = "resource_type";

    public ResourceType(int id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public String getTableName() {
        return TABLE;
    }
}
