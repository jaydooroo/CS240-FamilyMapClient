package services;

import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.UserDao;
import model.User;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;
import services.helper.GeneratePerson;
import services.helper.LoginHelper;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.UUID;

/***
 * Creates a new user account (user row in the database)
 * Generates 4 generations of ancestor data for the new user
 * (just like the /fill endpoint if called with a generations value of 4 and this new user’s username as parameters)
 * Logs the user in
 * Returns an authtoken
 */
public class RegisterService{

    private Database db;
    private boolean success = false;
    /***
     * Creates a new user account and generate 4 generations of ancestor data for the new user
     * by accessing database.
     * @param r Registration information including username, password, first and last name, and gender.
     * @return RegisterResult contains authtoken, username, personId, and success if it succeeds.
     * Otherwise, it contains fail message and success information.
     *   this.username = username;
     *         this.password = password;
     *         this.email = email;
     *         this.firstName = firstName;
     *         this.lastName = lastName;
     *         this.gender = gender;
     *         this.personID =personID;
     */
    public RegisterResult register (RegisterRequest r)  {

        try {
            db = new Database();
            Connection conn = db.getConnection();
            String authtoken;

            UserDao userDao = new UserDao(conn);
            UUID personID = UUID.randomUUID();

            // How does it recieve error from the server?
            // How does the sql respond if the same userName exist
            User user = new User(r.getUsername(), r.getPassword(), r.getEmail(), r.getFirstName(),
                    r.getLastName(), r.getGender(), personID.toString());

            userDao.insert(user);

            GeneratePerson creatingEventPersonTree = new GeneratePerson(user,conn);

            LoginRequest loginRequest = new LoginRequest (user.getUsername(), user.getPassword());
            LoginHelper loginHelper = new LoginHelper(conn);
            LoginResult loginResult = loginHelper.login(loginRequest);

            if (loginResult.isSuccess()) {
                authtoken = loginResult.getAuthtoken();
                success = true;
            }
            else {
                throw new DataAccessException("Error: Login Error in RegisterService");
            }

            RegisterResult registerResult = new RegisterResult(authtoken,user.getUsername(),
                    user.getPersonID(),success);

            db.closeConnection(true);

            return registerResult;
        }
        catch (DataAccessException e) {
            success = false;
            e.printStackTrace();

            RegisterResult registerResult = new RegisterResult(e.getMessage(),success);

            db.closeConnection(false);
            return registerResult;
        } catch (FileNotFoundException e) {
            success = false;
            e.printStackTrace();
            RegisterResult registerResult = new RegisterResult(e.getMessage(),success);

            db.closeConnection(false);
            return registerResult;
        }

    }

    public boolean isSuccess() {
        return success;
    }
}
