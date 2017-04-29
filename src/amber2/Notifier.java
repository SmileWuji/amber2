package amber2;

import java.util.TreeSet;

/**
 * Alarm! Surprise!
 */
public class Notifier implements Runnable {

    /* A set of notifications. */
    private Set<Notification> notifications;
    private GenericObservable<Notification> notifyHandler; 
    private GenericObservable<Notification> editHandler;
    private GenericObservable<Notification> expireHandler;

    public Notifier() {
        notifications = new TreeSet<>();
        notifyHandler = new GenericObservable<>();
        editHandler = new GenericObservable<>();
        expireHandler = new GenericObservable<>();
    }

    /**
     * Add a notification to this notifier.
     */
    public void add(Notification task) {
        // TODO: Implement this method!
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
        Notification nearest;
        long timeElapsed;

        pollExpiredOnes();
        while (true) {
            synchronized (notifications) {
                nearest = getTheNearetFuture();
                timeElapsed = getTimeElapsed(nearest);
                while (timeElapsed > 0 || nearest == null) {
                    this.wait(timeElapsed);
                    nearest = getTheNearetFuture();
                    timeElapsed = getTimeElapsed(nearest);
                }

                // At this point, the nearest notification is activated.
                pollNotification(nearest);
            }
        }
        
    }

    private void pollExpiredOnes() {
        // TODO: Implement this method!
    }

    private Notification getTheNearetFuture() {
        // TODO: Implement this method!
    }

    /**
     * Returns the time from now to the task gets activated, in milliseconds.
     * Returns 0 in case the task is null.
     */
    public long getTimeElapsed(Notification task) {
        // TODO: Implement this method!
    }

    /**
     * Assuming that the notification task is valid... proceed!
     */
    private void pollNotification(Notification task) {
        // TODO: Implement this method!
    }

/* The methods below are for the generic observers. */

    public void onNotify(Consumer<Notification> action) {
        // TODO: Implement this method!
    }

    public void onEdit(Consumer<Notification> action) {
        // TODO: Implement this method!
    }

    /**
     * The handler series will only be updated for a continuous round in a row.
     * After that the handlers will not be executed again.
     */
    public void onExpire(Consumer<Notification> action) {
        // TODO: Implement this method!
    }

}
