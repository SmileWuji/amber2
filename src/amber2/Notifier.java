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
            notifications.remove(first);
            if (notifications.isEmpty()) {
                return;
            }
            first = notifications.first();
        }
        expireHandler.close();
        expireHandler = null;
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
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println(System.currentTimeMillis());
        long now = System.currentTimeMillis();
        Date current = new Date(now);
        Date expired1 = new Date(now - 7000);
        Date expired2 = new Date(now - 5000);
        Date todo10sec = new Date(now + 10000);
        Date todo15sec = new Date(now + 15000);
        Notification.Builder builder = new Notification.Builder();
        Notification nt1, nt2, nt3, nt4;
        nt1 = builder.since(current).at(expired1).cast("I'm an angel with a shotgun").build();
        nt2 = builder.since(current).at(expired2).cast("Fighting 'till the wars won").build();
        nt3 = builder.since(current).at(todo10sec).cast("I don't care if haven won't take me back").build();
        nt4 = builder.since(current).at(todo15sec).cast("coda").build();
        
        Notifier app = new Notifier();
        Consumer<Notification> print = n -> System.out.println(n);
        app.onExpire(print);
        app.onNotify(print);
        
        System.out.println("SET");
        app.add(nt1);
        app.add(nt2);
        app.add(nt3);
        app.add(nt4);
        System.out.println("START");
        
        new Thread(app).start();
        Thread.sleep(5000);
        app.add(nt1);
        
    }

}
