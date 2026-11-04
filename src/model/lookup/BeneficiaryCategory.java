package model.lookup;

/**
 * CRUD lookup table: groups of beneficiaries (e.g. Orphans, Elderly, Refugees).
 */
public class BeneficiaryCategory extends LookupEntity {
    public static final String TABLE = "beneficiary_category";

    public BeneficiaryCategory(int id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public String getTableName() {
        return TABLE;
    }
}
