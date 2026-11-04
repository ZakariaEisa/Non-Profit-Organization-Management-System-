package database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.lookup.LookupEntity;

/**
 * SINGLETON pattern: exactly one shared database connection for the whole system.
 * The constructor is private, so the only way to get the object is getInstance().
 *
 * Phase 1: lookup tables are kept in memory to demonstrate CRUD.
 * Phase 2: the same methods will run real SQL (see schema.sql) through JDBC.
 */
public class DatabaseManager {
    private static DatabaseManager instance;

    private boolean connected;
    /** table name -> (id -> row) */
    private final Map<String, Map<Integer, LookupEntity>> lookupTables = new HashMap<>();

    private DatabaseManager() {
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void connect() {
        connected = true;
        System.out.println("[DB] Connected.");
    }

    public void disconnect() {
        connected = false;
        System.out.println("[DB] Disconnected.");
    }

    public boolean isConnected() {
        return connected;
    }

    public void executeQuery(String sql) {
        // TODO (Phase 2): run the statement through JDBC
        System.out.println("[DB] " + sql);
    }

    // ---------------- CRUD for lookup tables ----------------

    /** CREATE */
    public void insertLookup(LookupEntity item) {
        Map<Integer, LookupEntity> table = tableOf(item.getTableName());
        if (table.containsKey(item.getId())) {
            throw new IllegalArgumentException(item.getTableName() + " already has id " + item.getId());
        }
        table.put(item.getId(), item);
    }

    /** READ one */
    public LookupEntity findLookup(String tableName, int id) {
        return tableOf(tableName).get(id);
    }

    /** READ all */
    public List<LookupEntity> findAllLookups(String tableName) {
        return new ArrayList<>(tableOf(tableName).values());
    }

    /** UPDATE */
    public boolean updateLookup(LookupEntity item) {
        Map<Integer, LookupEntity> table = tableOf(item.getTableName());
        if (!table.containsKey(item.getId())) {
            return false;
        }
        table.put(item.getId(), item);
        return true;
    }

    /** DELETE */
    public boolean deleteLookup(String tableName, int id) {
        return tableOf(tableName).remove(id) != null;
    }

    private Map<Integer, LookupEntity> tableOf(String tableName) {
        return lookupTables.computeIfAbsent(tableName, t -> new LinkedHashMap<>());
    }
}
