package amber2;

public class Notification implements Comparable<Notification> {
    private Date issue;
    private Date activation;
    private String message;

    private Notification();

    public static class Builder {
        private Notification instance;

        public Builder() {
            instance = new Notification();
        }

        public void since(Date issue) {
            instance.issue = issue;
        }

        public void at(Date activation) {
            instance.activation = activation;
        }

        public void cast(String message) {
            instance.message = message;
        }

        public Notification build() {
            if (instance.issue == null) {
                throw new IllegalArgumentException("Missing issue date.");
            } else if (instance.activation == null) {
                throw new IllegalArgumentException("Missing activation date.");
            } else if (instance.message == null) {
                throw new IllegalArgumentException("Missing message.");
            }
            return instance;
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


}
