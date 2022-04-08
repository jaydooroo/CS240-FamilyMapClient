package services;

import dataAccess.AuthtokenDao;
import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.EventDao;
import model.Authtoken;
import model.Event;
import request.AllEventRequest;
import result.AllEventResult;

import java.sql.Connection;
import java.util.LinkedList;

/***
 * Returns All events for All family members of the current user.
 * The current user is determined from the provided auth token.
 */
public class AllEventService {

    private Database db;
    private boolean success = false;

    /***
     * Access DB through Dao class and retrieve all the events for all family members.
     * @param allEventRequest AllEventRequest class which contains data for retrieving All the events data.
     * @return AllEventResult class which contains all the events for all family members.
     */
    public AllEventResult event (AllEventRequest allEventRequest) {

        try {

            db = new Database();
            Connection conn = db.getConnection();

            AuthtokenDao authtokenDao = new AuthtokenDao(conn);
            EventDao eventDao = new EventDao(conn);

            Authtoken authtoken = authtokenDao.retrieve(allEventRequest.getAuthtoken());

            if(authtoken == null) {
                success = false;
                throw new DataAccessException("Error: Invalid auth token");
            }

            LinkedList<Event> events  = eventDao.retrieveAllEvent(authtoken.getUsername());

            AllEventResult allEventResult = new AllEventResult(events);

            success = true;
            allEventResult.setSuccess(success);
            db.closeConnection(true);
            return allEventResult;


        } catch (DataAccessException e) {
            e.printStackTrace();
            success = false;
            AllEventResult allEventResult = new AllEventResult(e.getMessage(),success);
            db.closeConnection(false);
            return allEventResult;
        }
    }

    public boolean isSuccess() {
        return success;
    }
}
