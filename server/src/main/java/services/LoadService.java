package services;

import dataAccess.*;
import request.ClearRequest;
import request.LoadRequest;
import result.LoadResult;

import java.sql.Connection;

/***
 * Clears all data from the database (just like the /clear API)
 * Loads the user, person, and event data from the request body into the database.
 */
public class LoadService {
    boolean success = false;
    private Database db;

    /***
     * Clears all data from the database and Loads the user Person and event data from the request body
     * @param r LoadRequest class contains all the data which is needed to load into database.
     * @return LoadResult contains the information of success.
     */
    public LoadResult load(LoadRequest r) {

        try{

            // Clear the Table
            ClearService clearService = new ClearService();
            clearService.clearDB(new ClearRequest());

            db = new Database();
            Connection conn = db.getConnection();

            UserDao uDao = new UserDao(conn);
            PersonDao pDao = new PersonDao(conn);
            EventDao eDao = new EventDao(conn);

            for(int i = 0; i < r.getUsers().length; i++) {
                uDao.insert(r.getUsers()[i]);
            }

            for (int i = 0; i < r.getPersons().length; i++) {
                pDao.insert(r.getPersons()[i]);
            }
            for (int i = 0; i < r.getEvents().length; i++) {
                eDao.insert(r.getEvents()[i]);
            }
            success = true;

            LoadResult loadResult = new LoadResult("Successfully added " +
                   r.getUsers().length + " users, " +  r.getPersons().length + " persons, and " +
                   r.getEvents().length + " events to the database.", success);

            db.closeConnection(true);

            return loadResult;

        } catch (DataAccessException e) {
            e.printStackTrace();
            success = false;
            LoadResult loadResult = new LoadResult(e.getMessage(), success);
            db.closeConnection(false);
            return loadResult;
        }

    }

    public boolean isSuccess() {
        return success;
    }
}
