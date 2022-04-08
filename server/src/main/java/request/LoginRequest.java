package request;

/***
 * Request to log the user in.
 * takes username and password to log the user in.
 */
public class LoginRequest {
    String username;
    String password;

    public LoginRequest (String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
