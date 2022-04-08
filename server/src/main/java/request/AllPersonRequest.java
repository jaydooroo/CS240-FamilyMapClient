package request;

/***
 *
 * Request All family members of the current user.
 * The current user is determined by the provided authtoken.
 *
 */

public class AllPersonRequest {

    String authtoken;

    public AllPersonRequest (String authtoken) {
        this.authtoken = authtoken;

    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

}
