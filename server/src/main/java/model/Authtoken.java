package model;

import java.util.Objects;

/***
 * Authtoken model class which stores the Authoken data.
 */
public class Authtoken {
    private String username = new String();
    private String authtoken = new String();

    /***
     * Constructor which takes two parameter, token, name  and set this value into the class variables.
     * @param token AuthToken variable.
     * @param name Name variable.
     */
    public Authtoken(String token, String name){
        this.authtoken = token;
        this.username = name;
    }

    /***
     *  No parameter constructor.
     */
    public Authtoken () { }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authtoken authtoken1 = (Authtoken) o;
        return Objects.equals(username, authtoken1.username) && Objects.equals(authtoken, authtoken1.authtoken);
    }

    @Override
    public String toString() {
        return "Authtoken{" +
                "username='" + username + '\'' +
                ", authtoken='" + authtoken + '\'' +
                '}';
    }
}
