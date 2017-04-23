package util;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class GenericObservable<T> {
    Set< Consumer<T> > observers;
    
    public GenericObservable() {
        observers = new HashSet<>(1);
    }
    
    /**
     * Broadcast the update to all observers.
     */
    public void update(T argument) {
        observers.forEach( a -> a.accept(argument) );
    }
    
    /**
     * Binds an action in case of any updates.
     */
    public void add(Consumer<T> action) {
        observers.add(action);
    }
    
    /**
     * Removes an action.
     */
    public void remove(Consumer<T> action) {
        observers.remove(action);
    }

    public void bind(Consumer<T> action) {
    	add(action);
    }
    
    public void subscribe(Consumer<T> action) {
        add(action);
    }
    
    public void unsubscribe(Consumer<T> action) {
        remove(action);
    }
}
