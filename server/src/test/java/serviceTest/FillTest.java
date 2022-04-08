package serviceTest;

import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.PersonDao;
import dataAccess.UserDao;
import model.Person;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.ClearRequest;
import request.FillRequest;
import request.RegisterRequest;
import result.FillResult;
import services.ClearService;
import services.FillService;
import services.RegisterService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FillTest {
    private FillRequest bestRequest;
    private FillService fillService;
    private RegisterService registerService;

    @BeforeEach
    public void setUp() throws DataAccessException {
        ClearRequest clearRequest = new ClearRequest();
        ClearService clearService = new ClearService();
        clearService.clearDB(clearRequest);

        RegisterRequest registerRequest =  new RegisterRequest("aaa","asdada","asdasdsdsa","susan","Ellis","f");

        registerService = new RegisterService();
        registerService.register(registerRequest);

        bestRequest = new FillRequest("aaa",4);

        fillService = new FillService();
    }

    @Test
    public void fillPass() throws DataAccessException {
        FillResult fillResult;

        fillResult = fillService.fill(bestRequest);

        assertNotNull(fillResult);
        assertEquals(true,fillResult.isSuccess());

        Database db = new Database();
        Connection conn = db.getConnection();

        PersonDao personDao =new PersonDao(conn);
        UserDao userDao = new UserDao(conn);

        User user = userDao.retrieve(bestRequest.getUsername());
        Person person =  personDao.retrieve(user.getPersonID());

        db.closeConnection(true);

        assertNotNull(person);
        assertEquals(user.getFirstName(), person.getFirstName());
        assertEquals(user.getLastName(),person.getLastName());
    }

    @Test
    public void fillFail() {
        FillResult fillResult;

        bestRequest.setGeneration(-1);

        fillResult = fillService.fill(bestRequest);

        assertNotNull(fillResult);
        assertEquals(false,fillResult.isSuccess());


    }



}
