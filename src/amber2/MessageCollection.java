package amber2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import util.GenericObservable;

/**
 * A collection of messages.
 */
public class MessageCollection implements Iterable<Message> {
    
    /* Messaging functionalities. */
    private List<Message> records;
    private Iterator<Message> recalls;
    private GenericObservable<Void> emptyRecallHandler;
    private GenericObservable<Message> recallHandler;

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
    public Iterator<Message> iterator() {
        return records.iterator();
    }

    /**
     * Adds a message to the records.
     */
    public void add(Message message) {
        records.add(message);
        if (!masked && recalls!=null) {
            resetRecall();
        }
    }

    /**
     * Recalls the most recent message.
     * Returns the message and notifies the observers.
     * Returns null on failure.
     */
    public Message recall() {
        Message msg;

        if (recalls==null) {
            recalls = iterator();
        }
        if (!recalls.hasNext()) {
            emptyRecallHandler.update(null);
            return null;
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

    public void onRecall(Consumer<Message> action) {
        recallHandler.add(action);
    }

    public void onRecallEmpty(Consumer<Void> action) {
        emptyRecallHandler.add(action);
    }
}
