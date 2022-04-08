package services;

import dataAccess.DataAccessException;
import dataAccess.Database;
import request.LoginRequest;
import result.LoginResult;
import services.helper.LoginHelper;

import java.sql.Connection;

/***
 * Logs the user in and returns an authtoken.
 */
public class LoginService {

    private Database db;
    private boolean success = false;




    /***
     * Logs the user in by using information in the parameter "LoginRequest"
     * Also use dao to access the database.
     * @param loginRequest has information of username and password
     * @return LoginResult class has authtoken and success information.
     */
    public LoginResult login(LoginRequest loginRequest){
         try {
             db = new Database();
             Connection conn = db.getConnection();

             LoginHelper loginHelper = new LoginHelper(conn);

             LoginResult successLogin = loginHelper.login(loginRequest);


             db.closeConnection(true);
             success = true;
             return successLogin;

         }catch (DataAccessException e) {
            success = false;
            e.printStackTrace();
            LoginResult failLoginResult = new LoginResult(e.getMessage(),success);

            db.closeConnection(false);
            return failLoginResult;
        }

    }

    public boolean isSuccess () {
        return success;
    }
}
