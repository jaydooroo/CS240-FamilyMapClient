package result;

import model.Event;

import java.util.LinkedList;

/***
 * Shows the result of the request "Event"
 * Returns ALL events for ALL family members of the current user.
 * The current user is determined from the provided auth token.
 */

public class AllEventResult {

    LinkedList<Event> data;
    String message;
    boolean success;

    public AllEventResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
    public AllEventResult(LinkedList<Event> data) {
        this.data = data;
    }

    public LinkedList<Event> getData() {
        return data;
    }

    public void setData(LinkedList<Event> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


}
