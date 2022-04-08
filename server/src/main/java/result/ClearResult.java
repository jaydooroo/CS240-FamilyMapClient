package result;

/***
 * Shows the result of the request "clear".
 * return message, and boolean type variable which tells if the request succeeds or not.
 */

public class ClearResult {

    String message;
    boolean success;

    public ClearResult (String message, boolean success) {

        this.message = message;
        this.success = success;
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
