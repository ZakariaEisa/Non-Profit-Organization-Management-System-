package controller;

import database.DatabaseManager;

/**
 * MVC Controller base class. Every controller reaches the same Singleton DatabaseManager.
 */
public abstract class BaseController {
    protected final DatabaseManager db = DatabaseManager.getInstance();
}
