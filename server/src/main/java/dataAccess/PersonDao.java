package dataAccess;

import model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/***
 *  Person Data Access Class,
 *  Access Data Base and insert Person data or retrieve from it the DataBase
 */

public class PersonDao {
    private final Connection conn;

    public PersonDao(Connection conn) {
        this.conn = conn;
    }

    /***
     * Insert Person data into the Data Base.
     * @param person variable which has person data.
     */
    public void insert (Person person) throws DataAccessException {

        String sql = "INSERT INTO Person (personID, associatedUsername, firstName, lastName, gender, " +
                "fatherID, motherID, spouseID) VALUES(?,?,?,?,?,?,?,?)";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            if(person.getFatherID() == null || person.getFatherID().equals("")) {
                stmt.setString(6, null);
            }
            else {
                stmt.setString(6, person.getFatherID());
            }
            if(person.getMotherID() == null || person.getMotherID().equals("")){
                stmt.setString(7, null);
            }
            else {
                stmt.setString(7,person.getMotherID());
            }
            if(person.getSpouseID() == null || person.getSpouseID().equals("")) {
                stmt.setString(8, null);
            }
            else {
                stmt.setString(8, person.getSpouseID());
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting an Person into the database");
        }
    }

    /***
     * Retrieve the Person data from the database using person ID.
     * @param personId variable which ahs person ID
     * @return Person data type which has data related with person.
     */
    public Person retrieve (String personId) throws DataAccessException {
        Person person;
        ResultSet rs;
        String sql = "SELECT * FROM Person WHERE personID = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, personId);
            rs = stmt.executeQuery();
            if(rs.next()) {
                person = new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                        rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"),
                        rs.getString("fatherID"), rs.getString("motherID"),rs.getString("spouseID"));

                return person;
            }
            else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an Person in the database");
        }
    }

    /***
     * Clear the Person Table in the data base.
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Person";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the Person table");
        }

    }

    public void clearAssociatedUsername(String username) throws DataAccessException {

        String sql = "DELETE FROM Person WHERE associatedUsername = ?;";


        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1,username);
            stmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error: Clearing Error");
        }
    }

    public LinkedList <Person> retrieveAllPeople(String username)  throws DataAccessException {
        String sql = "SELECT * FROM Person WHERE associatedUsername = ?;";
        LinkedList <Person>  people = new LinkedList<Person>();
        ResultSet rs;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,username);
            rs = stmt.executeQuery();

            while (rs.next()) {
                people.add(new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                        rs.getString("firstName"), rs.getString("lastName"),rs.getString("gender"),
                        rs.getString("fatherID"),rs.getString("motherID"),rs.getString("spouseID")));
            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error: Retrieving all people error");
        }

    return people;
    }


}
