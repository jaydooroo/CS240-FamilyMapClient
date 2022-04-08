package serviceTest;


import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.PersonDao;
import dataAccess.UserDao;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.ClearRequest;
import request.RegisterRequest;
import result.RegisterResult;
import services.ClearService;
import services.RegisterService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RegisterServiceTest {
    private RegisterRequest bestRequest;
    private RegisterService regiService;


    @BeforeEach
    public void setUp() throws DataAccessException {


        bestRequest = new RegisterRequest("aaaftyfytse66","asdada","asdasdsdsa","susan","Ellis","f");

        regiService = new RegisterService();



    }

    @AfterEach
    public void Clean() {


        ClearRequest emptyRequest = new ClearRequest();
        ClearService clear = new ClearService();
        clear.clearDB(emptyRequest);

    }

    @Test
    public void registerPass () throws DataAccessException {
        RegisterResult regiResult;
        regiResult = regiService.register(bestRequest);

        Database db = new Database();
        Connection conn = db.getConnection();

        PersonDao personDao = new PersonDao(conn);
        UserDao userDao = new UserDao(conn);


        User dbUser = userDao.retrieve(bestRequest.getUsername());
        Person dbPerson =  personDao.retrieve(dbUser.getPersonID());

        db.closeConnection(true);

        assertNotNull(regiResult);
        assertEquals (bestRequest.getUsername(),regiResult.getUsername());
        assertEquals(true,regiResult.isSuccess());
        assertEquals(bestRequest.getFirstName(),dbUser.getFirstName());
        assertEquals(bestRequest.getLastName(),dbUser.getLastName());
        assertEquals(regiResult.getPersonID(),dbPerson.getPersonID());
    }

    @Test
    public void registerFail () {
        RegisterResult regiResult;

        bestRequest.setGender("asd");

        regiResult = regiService.register(bestRequest);

        assertNotNull(regiResult);
        assertNotNull(regiResult.getMessage());
        assertEquals(false,regiResult.isSuccess());




    }





}
