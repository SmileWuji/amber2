package amber2;

import java.util.Date;

public class Message {
    private String message;
    private Date issued;

    private Message() {
    }

    public Message(Message another) {
        message = another.message;
        issued = new Date(another.issued.getTime());
    }

    public static class Builder {
        private Message instance;

        public Builder() {
            instance = new Message();
        }

        public Builder cast(String message) {
            instance.message = message;
            return this;
        }

        public Builder at(Date issued) {
            instance.issued = issued;
            return this;
        }

        public Message build() {
            return new Message(instance);
        }
    }

    public String getMessage() {
        return message;
    }

    public Date getIssued() {
        return new Date(issued.getTime());
    }

    public String toString() {
        return message;
    }

}
