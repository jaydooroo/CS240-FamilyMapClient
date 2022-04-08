package services;

import dataAccess.AuthtokenDao;
import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.EventDao;
import model.Authtoken;
import model.Event;
import request.SingleEventRequest;
import result.SingleEventResult;

import java.sql.Connection;

/***
 *Returns the single Event object with the specified ID (if the event is associated with the current user).
 * The current user is determined by the provided authtoken.
 */
public class SingleEventService {
    private boolean success = false;
    private Database db;

    public SingleEventResult singleEvent (SingleEventRequest singleEventRequest) {

        try {
            db = new Database();
            Connection conn =db.getConnection();

            EventDao eventDao = new EventDao(conn);
            AuthtokenDao authtokenDao = new AuthtokenDao(conn);

            Authtoken authtoken = authtokenDao.retrieve(singleEventRequest.getAuthtoken());
            SingleEventResult singleEventResult;


            if(authtoken == null) {
                success = false;
                throw new DataAccessException("Error: Invalid auth token");
            }


            Event event = eventDao.retrieve(singleEventRequest.getEventID());
            if(event == null) {
                throw new DataAccessException("Error: Wrong EventID");
            }

            if (event.getAssociatedUsername().equals(authtoken.getUsername())) {

                success = true;

                singleEventResult = new SingleEventResult();
                singleEventResult.setEventID(event.getEventID());
                singleEventResult.setAssociatedUsername(event.getAssociatedUsername());
                singleEventResult.setPersonID(event.getPersonID());
                singleEventResult.setLatitude(event.getLatitude());
                singleEventResult.setLongitude(event.getLongitude());
                singleEventResult.setCountry(event.getCountry());
                singleEventResult.setCity(event.getCity());
                singleEventResult.setEventType(event.getEventType());
                singleEventResult.setYear(event.getYear());
                singleEventResult.setSuccess(success);
            }
            else {

                throw new DataAccessException("Error: Not logged in");
            }

            db.closeConnection(true);
            return singleEventResult;

        } catch (DataAccessException e) {
            success = false;
            SingleEventResult singleEventResult = new SingleEventResult(e.getMessage(),success);
            e.printStackTrace();
            db.closeConnection(false);
            return singleEventResult;
        }
    }

    public boolean isSuccess() {
        return success;

    }
}
