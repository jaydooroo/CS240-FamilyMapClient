package services;

import dataAccess.*;
import request.ClearRequest;
import result.ClearResult;

import java.sql.Connection;

/***
 * Delete All data from the database,
 * inclusing user, authtoken, person, and event data
 */
public class ClearService {

    private Database db;
    boolean success = false;


    /***
     * Clear all the data by accessing using dao classes.
     * @param r ClearRequest class.
     * @return  ClearResult class which contains the information of success.
     */
    public ClearResult clearDB (ClearRequest r) {

        try {
            db = new Database();

            Connection conn = db.getConnection();

            UserDao uDao = new UserDao(conn);
            PersonDao pDao = new PersonDao(conn);
            EventDao eDao = new EventDao(conn);
            AuthtokenDao aDao = new AuthtokenDao(conn);

            uDao.clear();
            pDao.clear();
            eDao.clear();
            aDao.clear();

            success = true;

            ClearResult clearResult = new ClearResult("Clear succeeded.",true);

            db.closeConnection(true);

            return clearResult;

        } catch (DataAccessException e) {
            success = false;

            //TODO: should be only internal server error
            ClearResult clearResult = new ClearResult(e.getMessage(),success);

            db.closeConnection(false);
            e.printStackTrace();

            return clearResult;
        }
    }

    public boolean isSuccess() {
        return success;
    }

}
