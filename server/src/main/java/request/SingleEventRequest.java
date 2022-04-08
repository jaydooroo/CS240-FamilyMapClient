package request;

/***
 * Request a event related with the specified ID.
 */
public class SingleEventRequest {
    String authtoken;
    String eventID;

    public SingleEventRequest(String eventID) {
        this.eventID = eventID;

    }

    public SingleEventRequest(String eventID, String authtoken) {
        this.eventID = eventID;
        this.authtoken = authtoken;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }


}
