package model.observer;

import model.event.Event;

/**
 * OBSERVER pattern: observer interface for events.
 */
public interface IEventObserver {
    void update(Event event);
}
