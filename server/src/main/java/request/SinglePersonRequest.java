package request;

/***
 * Request a Person data with the specified ID.
 */
public class SinglePersonRequest {

    String personID;
    String authtoken;

    public SinglePersonRequest (String personID){
        this.personID = personID;
    }

    public SinglePersonRequest (String personID, String authtoken) {
        this.personID = personID;
        this.authtoken = authtoken;
    }


    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }
}
