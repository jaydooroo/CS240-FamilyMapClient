package dataAccess;

import model.Authtoken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/***
 *  Authtoken Data Access Class,
 *  Access Data Base and insert Authtoken data or retrieve from the DataBase
 */
public class AuthtokenDao {
    private final Connection conn;

    public AuthtokenDao(Connection conn) {
        this.conn = conn;
    }


    /***
     * Insert Authtoken data into the DataBase
     * @param token Authtoken data type variable. Authtoken data which we will insert
     */
    public void insert (Authtoken token) throws DataAccessException {
        String sql = "INSERT INTO Authtoken (authtoken, username) VALUES(?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,token.getAuthtoken());
            stmt.setString(2,token.getUsername());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException ("Error encountered while inserting an authtoken into the database");
        }
    }

    /***
     *
     * @return Authtoken data type variable.
     */
    public Authtoken retrieve (String authtoken) throws DataAccessException {

        Authtoken authtokenReturn;
        ResultSet rs;
        String sql = "SELECT * FROM Authtoken WHERE authtoken = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1,authtoken);
            rs = stmt.executeQuery();

            if(rs.next()) {
              authtokenReturn = new Authtoken(rs.getString("authtoken"), rs.getString("username"));

              return authtokenReturn;
            }
            else {
                // Not sure which one I should use.
                //throw new DataAccessException("No match authtoken");
               return null;
            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while retrieving the Authtoken table");
        }

    }

    /***
     * Clear the Authtoken table in the database.
     */
    public void clear () throws DataAccessException{
        String sql = "DELETE FROM Authtoken";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the Authtoken table");
        }

    }


}
