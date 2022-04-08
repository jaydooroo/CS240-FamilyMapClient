package dataAccess;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/***
 *  UserDao Data Access Class,
 *  Access Data Base and insert User data or retrieve from the DataBase
 */
public class UserDao {
    private final Connection conn;

    public UserDao(Connection conn) {
        this.conn = conn;
    }
    /***
     * Insert User data into the DataBase
     * @param user User data type variable. User data which we will insert
     */
    public void insert(User user) throws DataAccessException {
        String sql = "INSERT INTO User (username, password, email, firstName, lastName, gender, personID) " +
                "VALUES(?,?,?,?,?,?,?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,user.getUsername());
            stmt.setString(2,user.getPassword());
            stmt.setString(3,user.getEmail());
            stmt.setString(4,user.getFirstName());
            stmt.setString(5,user.getLastName());
            stmt.setString(6,user.getGender());
            stmt.setString(7,user.getPersonID());

            stmt.executeUpdate();
        }catch(SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting an User into the database");
        }
    }

    /***
     * search the data which has username and retrieve it from the DataBase.
     * @param username variable which contains username.
     * @return User class which has User data related with the username.
     */
    public User retrieve(String username) throws DataAccessException {

        User user;
        ResultSet rs;
        String sql = "SELECT * FROM User WHERE username = ?;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1,username);
            rs = stmt.executeQuery();
            if(rs.next()) {
                user = new User(rs.getString("username"), rs.getString("password")
                        ,rs.getString("email"), rs.getString("firstName"),
                        rs.getString("lastName"), rs.getString("gender"),
                        rs.getString("PersonID") );
                return user;
            }
            else {
                return null;
            }

        }catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an User in the database");
        }
    }

    /***
     * to see if the username and password is valid by looking at the data base.
     * @param username username parameter.
     * @param password password parameter.
     * @return boolean which represent if the username and password is vaild or not.
     */
    boolean validate(String username, String password) throws DataAccessException {
        ResultSet rs;
        String sql = "SELECT * FROM User WHERE username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,username);
            rs = stmt.executeQuery();
            if(rs.next()) {
                if(password == rs.getString("password")){
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while checking validation of an User in the database");
        }
    }

    /***
     * clear the user table in the database.
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM User";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the event table");
        }

    }

}
