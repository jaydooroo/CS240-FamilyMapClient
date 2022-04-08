package result;

/***
 * Shows the result of the request "Load"
 * Returns message of if the loading the data into database succeeds or not.
 */
public class LoadResult {

    String message;
    boolean success;

    public LoadResult() {}

    public LoadResult(String message, boolean success) {
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
