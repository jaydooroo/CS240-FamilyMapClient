package request;

/***
 *Request All Events for All family members of the current user
 */
public class AllEventRequest {

    String authtoken;

    public AllEventRequest(String authtoken) {
        this.authtoken = authtoken;
    }

    public AllEventRequest() {

    }
    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

}
