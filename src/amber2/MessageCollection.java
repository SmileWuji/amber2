package amber2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import util.GenericObservable;

/**
 * A collection of messages.
 */
public class MessageCollection implements Iterable<String> {
    
    /* Messaging functionalities. */
    private List<String> records;
    private Iterator<String> recalls;
    private GenericObservable<Void> emptyRecallHandler;
    private GenericObservable<String> recallHandler;

    /* Configuration of the recalls. */
    private boolean masked;

    public MessageCollection() {
        records = new LinkedList<>();
        recalls = null;
        masked = false;
    }

    /**
     * Returns an iterator of all the messages in this collection.
     */
    @Override
    public Iterator<String> iterator() {
        return records.iterator();
    }

    /**
     * Adds a message to the records.
     */
    public void add(String message) {
        records.add(message);
        if (!masked && recalls!=null) {
            resetRecall();
        }
    }

    /**
     * Recalls the most recent message.
     * Returns the message and notifies the observers.
     */
    public String recall() {
        String msg;

        if (recalls==null) {
            recalls = iterator();
        }
        if (!recalls.hasNext()) {
            emptyRecallHandler.update(null);
        }
        msg = recalls.next();
        recallHandler.update(msg);
        return msg;
    }

    /**
     * Reset the recalling procedure.
     */
    public void resetRecall() {
        recalls = null;
    }

    public void onRecall(Consumer<String> action) {
        recallHandler.add(action);
    }

    public void onRecallEmpty(Consumer<Void> action) {
        emptyRecallHandler.add(action);
    }
}
