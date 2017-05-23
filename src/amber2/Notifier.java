package amber2;

import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Consumer;

import util.GenericObservable;

/**
 * Alarm! Surprise!
 */
public class Notifier implements Runnable {

    /* A set of notifications. */
    private SortedSet<Notification> notifications;
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
        synchronized (notifications) {
            notifications.add(task);
            editHandler.update(task);
        }
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
                    try {
                        notifications.wait(timeElapsed);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        return;
                    }
                    nearest = getTheNearetFuture();
                    timeElapsed = getTimeElapsed(nearest);
                }

                /* At this point, the nearest notification is activated. */
                pollNotification(nearest);
            }
        }
        
    }

    private void pollExpiredOnes() {
        Notification first;
        Date current = new Date(System.currentTimeMillis());
        
        if (notifications.isEmpty()) {
            return;
        }
        first = notifications.first();
        while (first.at().compareTo(current) <= 0) {
            expireHandler.update(first);
            if (notifications.isEmpty()) {
                return;
            }
            first = notifications.first();
        }
        
    }

    private Notification getTheNearetFuture() {
        if (notifications.isEmpty()) {
            return null;
        }
        return notifications.first();
    }

    /**
     * Returns the time from now to the task gets activated, in milliseconds.
     * Returns 0 in case the task is null.
     */
    public long getTimeElapsed(Notification task) {
        if (task == null) {
            return 0;
        }
        return task.at().getTime() - System.currentTimeMillis();
    }

    /**
     * Assuming that the notification task is valid... proceed!
     */
    private void pollNotification(Notification task) {
        notifyHandler.update(task);
        notifications.remove(task);
    }

/* The methods below are for the generic observers. */

    public void onNotify(Consumer<Notification> action) {
        notifyHandler.bind(action);
    }

    public void onEdit(Consumer<Notification> action) {
        editHandler.bind(action);
    }

    /**
     * The handler series will only be updated for a continuous round in a row.
     * After that the handlers will not be executed again.
     */
    public void onExpire(Consumer<Notification> action) {
        expireHandler.bind(action);
    }
    
    /**
     * For testing purpose...
     */
    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
    }

}
