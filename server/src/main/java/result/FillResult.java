package result;

/***
 * shows the result of the Fill result.
 * Returns message which tells if the procedure succeeds or not.
 */
public class FillResult {

    String message;
    boolean success;

    public FillResult (String message, boolean success) {
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
