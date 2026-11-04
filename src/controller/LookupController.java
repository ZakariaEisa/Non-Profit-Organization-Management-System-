package controller;

import java.util.List;
import model.lookup.LookupEntity;

/**
 * Full CRUD for every lookup table (CR-07). Used by the admin page.
 */
public class LookupController extends BaseController {

    /** CREATE */
    public void add(LookupEntity item) {
        validate(item);
        db.insertLookup(item);
    }

    /** READ one */
    public LookupEntity getById(String tableName, int id) {
        return db.findLookup(tableName, id);
    }

    /** READ all */
    public List<LookupEntity> list(String tableName) {
        return db.findAllLookups(tableName);
    }

    /** UPDATE */
    public boolean edit(LookupEntity item) {
        validate(item);
        return db.updateLookup(item);
    }

    /** DELETE */
    public boolean delete(String tableName, int id) {
        return db.deleteLookup(tableName, id);
    }

    private void validate(LookupEntity item) {
        if (item.getName() == null || item.getName().isBlank()) {
            throw new IllegalArgumentException("Lookup name is required");
        }
    }
}
