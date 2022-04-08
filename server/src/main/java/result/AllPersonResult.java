package result;

import model.Person;

import java.util.LinkedList;

/***
 * Returns ALL family members of the current user.
 * The current user is determined by the provided authtoken.
 */
public class AllPersonResult {

    LinkedList<Person> data;
    String message;
    boolean success;

    public AllPersonResult (LinkedList<Person> people) {
        this.data = people;
    }

    public LinkedList<Person> getData() {
        return data;
    }

    public void setData(LinkedList<Person> data) {
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

    public AllPersonResult(String message, boolean success) {

        this.message = message;
        this.success = success;
    }


}
