package services.helper;

import dataAccess.AuthtokenDao;
import dataAccess.DataAccessException;
import dataAccess.UserDao;
import model.Authtoken;
import model.User;
import request.LoginRequest;
import result.LoginResult;

import java.sql.Connection;
import java.util.UUID;

public class LoginHelper {

    private Connection conn;

    public LoginHelper (Connection conn) {
        this.conn = conn;
    }


    /***
     * Logs the user in by using information in the parameter "LoginRequest"
     * Also use dao to access the database.
     * @param loginRequest has information of username and password
     * @return LoginResult class has authtoken and success information.
     */
    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {

            User retrieve;
            UserDao accessUserDB = new UserDao(conn);

            retrieve = accessUserDB.retrieve(loginRequest.getUsername());

            if (retrieve != null) {

                if (retrieve.getPassword().equals(loginRequest.getPassword())){

                    // build authtoken dao and insert into data

                    String generatedAuthToken = UUID.randomUUID().toString();
                    Authtoken authtoken = new Authtoken(generatedAuthToken, loginRequest.getUsername());

                    AuthtokenDao authtokenDao = new AuthtokenDao(conn);
                    authtokenDao.insert(authtoken);

                    // Create respond body
                    LoginResult successLoginResult = new LoginResult(generatedAuthToken, loginRequest.getUsername(),
                            retrieve.getPersonID(), true);

                    return successLoginResult;


                } else {
                    throw new DataAccessException("Error: password does not match");
                }
            } else {
                throw new DataAccessException("Error: Does not have the user");
            }

    }

}

