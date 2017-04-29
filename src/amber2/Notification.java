package amber2;

import java.util.Date;

public class Notification implements Comparable<Notification> {
    private Date issue;
    private Date activation;
    private String message;

    private Notification() {
    }

    /**
     * Copy constructor.
     */
    public Notification(Notification another) {
        issue = new Date(another.issue.getTime());
        activation = new Date(another.activation.getTime());
        message = another.message;
    }

    public static class Builder {
        private Notification instance;

        public Builder() {
            instance = new Notification();
        }

        public Builder since(Date issue) {
            instance.issue = issue;
            return this;
        }

        public Builder at(Date activation) {
            instance.activation = activation;
            return this;
        }

        public Builder cast(String message) {
            instance.message = message;
            return this;
        }

        public Notification build() {
            if (instance.issue == null) {
                throw new IllegalArgumentException("Missing issue date.");
            } else if (instance.activation == null) {
                throw new IllegalArgumentException("Missing activation date.");
            } else if (instance.message == null) {
                throw new IllegalArgumentException("Missing message.");
            }
            return new Notification(instance);
        }
    }

    @Override
    public int compareTo(Notification another) {
        return activation.compareTo(another.activation);
    }

    public Date since() {
        return issue;
    }

    public Date at() {
        return activation;
    }

    public String cast() {
        return message;
    }

    @Override
    public boolean equals(Object another) {
        return (another != null) 
            && (another instanceof Notification) 
            && (compareTo((Notification) another) == 0)
            && (message.equals(((Notification) another).message));
    } 

    @Override
    public String toString() {
        return message;
    }


}
