package services;

import dataAccess.AuthtokenDao;
import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.PersonDao;
import model.Authtoken;
import model.Person;
import request.SinglePersonRequest;
import result.SinglePersonResult;

import java.sql.Connection;

/***
 * Returns the single Person object with the specified ID (if the person is associated with the current user).
 * The current user is determined by the provided authtoken.
 */

public class SinglePersonService {

    private Database db;
    boolean success = false;
    /***
     * Find a single Person object related with specified ID by accessing Database.
     * @param personRequest SinglePersonRequest class
     * @return SinglePersonResult class contains all the Person object information if succeed
     * Otherwise, contains the message of failure and success information.
     */
    public SinglePersonResult singlePerson (SinglePersonRequest personRequest) {

        try{
            db = new Database();
            Connection conn = db.getConnection();

            PersonDao personDao= new PersonDao(conn);
            AuthtokenDao authtokenDao = new AuthtokenDao(conn);

            Authtoken authtoken =  authtokenDao.retrieve(personRequest.getAuthtoken());
            SinglePersonResult singlePersonResult;



            if (authtoken == null) {
                success = false;
                throw new DataAccessException("Error: Invalid auth token");
            }

            Person person =  personDao.retrieve(personRequest.getPersonID());

            if(person == null) {
                throw new DataAccessException("Error: wrong personID ");
            }

            if(person.getAssociatedUsername().equals(authtoken.getUsername())) {

                success = true;

                singlePersonResult = new SinglePersonResult();
                singlePersonResult.setPersonID(person.getPersonID());
                singlePersonResult.setAssociatedUsername(person.getAssociatedUsername());
                singlePersonResult.setFirstName(person.getFirstName());
                singlePersonResult.setLastName(person.getLastName());
                singlePersonResult.setGender(person.getGender());
                singlePersonResult.setFatherID(person.getFatherID());
                singlePersonResult.setMotherID(person.getMotherID());
                singlePersonResult.setSpouseID(person.getSpouseID());
                singlePersonResult.setSuccess(success);


            }
            else {

                throw new DataAccessException("Error: Not logged in");

            }
            db.closeConnection(true);
            return singlePersonResult;

        } catch (DataAccessException e) {


            success =false;
           SinglePersonResult singlePersonResult = new SinglePersonResult(e.getMessage(),success);
            e.printStackTrace();
            db.closeConnection(false);
            return singlePersonResult;
        }
    }

    public boolean isSuccess() {
        return success;
    }
}
