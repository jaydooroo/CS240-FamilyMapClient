package request;

/**
 * request to populate the server's database with generated data for the
 * specified username.
 */
public class FillRequest {

    String username;
    int generations;

    public FillRequest () {}

    public FillRequest(String username, int generations) {

        this.username = username;
        this.generations = generations;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGeneration() {
        return generations;
    }

    public void setGeneration(int generations) {
        this.generations = generations;
    }

}
