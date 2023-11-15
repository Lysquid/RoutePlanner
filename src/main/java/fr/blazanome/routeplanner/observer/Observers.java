package fr.blazanome.routeplanner.observer;

/**
 * Observers
 */
public class Observers {

    /**
     * Creates a {@link TypeFilteredObserver} given a class
     *
     * @param type     the class of the type to filter
     * @param observer the typed observer
     * @param <T>      an Observable
     * @return the filtered observer that receives only messages from T
     */
    public static <T extends Observable> Observer typed(Class<T> type, TypedObserver<T> observer) {
        return new TypeFilteredObserver<>(type, observer);
    }

}
