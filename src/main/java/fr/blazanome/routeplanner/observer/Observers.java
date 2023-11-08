package fr.blazanome.routeplanner.observer;

/**
 * Observers
 */
public class Observers {

    public static <T extends Observable> Observer typed(Class<T> type, TypedObserver<T> observer) {
        return new TypeFilteredObserver<>(type, observer);
    }
    
}
