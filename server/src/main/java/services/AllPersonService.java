package services;

import dataAccess.AuthtokenDao;
import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.PersonDao;
import model.Authtoken;
import model.Person;
import request.AllPersonRequest;
import result.AllPersonResult;

import java.sql.Connection;
import java.util.LinkedList;

/***
 * Returns All family members of the current user.
 * The current user is determined by the provided authtoken.
 *
 */
public class AllPersonService {

    private Database db;
    private boolean success = false;
    /***
     * Returns all family members of the current user by accessing
     * the Data Base.
     * @param personRequest AllPersonRequest class which contains the request information.
     * @return AllPersonResult class which contains Person data of all related family members.
     */
    public AllPersonResult person(AllPersonRequest personRequest) {

        try{
            db = new Database();
            Connection conn = db.getConnection();

            AuthtokenDao authtokenDao = new AuthtokenDao(conn);
            PersonDao personDao = new PersonDao(conn);

            Authtoken authtoken = authtokenDao.retrieve(personRequest.getAuthtoken());
            if (authtoken == null) {
                success = false;
                throw new DataAccessException("Error: Invalid auth token");
            }

            LinkedList<Person> people = personDao.retrieveAllPeople(authtoken.getUsername());

            AllPersonResult allPersonResult = new AllPersonResult(people);

            success = true;
            allPersonResult.setSuccess(success);
            db.closeConnection(true);
            return allPersonResult;



        } catch (DataAccessException e) {
            e.printStackTrace();
            success = false;
            AllPersonResult allPersonResult = new AllPersonResult(e.getMessage(),success);
            db.closeConnection(false);
            return allPersonResult;
        }

    }

    public boolean isSuccess() {
        return success;
    }
}
