package amber2;

/**
 * Alarm! Surprise!
 */
public class Notifier implements Runnable {

    /* A map of notifications. */
    private PriorityQueue<Notification> notifications;

    public Notifier() {
        // TODO: Implement this function!
    }

    /**
     * Run this notifier; notify all observers in case of a notification becomes
     * active.
     * 
     * Before any regular progress, all the expired notifications will be 
     * broad-casted to the expiring channels.
     *
     * (A notify call is needed here!)
     *
     * Then...
     */
    @Override
    public void run() {
        // TODO: Implement this function!
    }

}
