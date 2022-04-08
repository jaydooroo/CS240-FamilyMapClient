package dataAccess;

import model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/***
 *  Event Data Access Class,
 *  Event Data Base and insert Event related data or retrieve the data from the DataBase
 */

public class EventDao {

    private final Connection conn;

    public EventDao(Connection conn) {
        this.conn = conn;
    }


    /***
     * insert Event related Data into the DataBase.
     * @param event variable.
     */
    public void insert (Event event) throws DataAccessException {
        String sql = "INSERT INTO Event (eventID, associatedUsername, personID, latitude, longitude, country," +
                "city, eventType, year) VALUES(?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,event.getEventID());
            stmt.setString(2,event.getAssociatedUsername());
            stmt.setString(3,event.getPersonID());
            stmt.setFloat(4,event.getLatitude());
            stmt.setFloat(5,event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8,event.getEventType());
            stmt.setInt(9,event.getYear());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting an Event into the database");
        }
    }

    /***
     * Search the data which has event ID and retrieve it from the DataBase
     * and returns the data.
     * @param eventId variable which contains event ID.
     * @return Event class which has event data related with the event ID.
     */
    public Event retrieve (String eventId) throws DataAccessException {
        Event event;
        ResultSet rs;
        String sql = "SELECT * FROM Event WHERE eventID = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1,eventId);
            rs = stmt.executeQuery();
            if(rs.next()) {
                event = new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                        rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                        rs.getString("country"),rs.getString("city"),rs.getString("eventType"),
                        rs.getInt("year"));

                return event;
            }
            else {

                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while retrieving the Event table ");

        }
    }
    //update, clear

    /***
     * Clear the Event table in the DataBase.
     */
    public void clear () throws DataAccessException{
        String sql = "DELETE FROM EVENT";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the Event table");
        }


    }
    public void clearAssociatedUsername(String username) throws DataAccessException {

        String sql = "DELETE FROM Event WHERE associatedUsername = ?;";


        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1,username);
            stmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error: Clearing Error");
        }
    }

    public LinkedList<Event> retrieveAllEvent(String username)  throws DataAccessException {
        String sql = "SELECT * FROM Event WHERE associatedUsername = ?;";
        LinkedList <Event>  events = new LinkedList<Event>();
        ResultSet rs;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,username);
            rs = stmt.executeQuery();

            while (rs.next()) {
                events.add(new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                        rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude")
                ,rs.getString("country"),rs.getString("city"),rs.getString("eventType"),
                        rs.getInt("year")));

            }
            return events;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error: Retrieving all events error");
        }


    }


}
