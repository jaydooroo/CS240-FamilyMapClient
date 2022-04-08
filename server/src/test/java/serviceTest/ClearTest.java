package serviceTest;

import dataAccess.*;
import model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.ClearRequest;
import request.RegisterRequest;
import result.RegisterResult;
import services.ClearService;
import services.RegisterService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertNull;

public class ClearTest {
    private RegisterService registerService;
    private ClearService clearService;
    private ClearRequest clearRequest;
    private RegisterResult registerResult;

    @BeforeEach
    public void setUp() throws DataAccessException {
         clearRequest = new ClearRequest();
        ClearService clearService = new ClearService();
        clearService.clearDB(clearRequest);

        RegisterRequest registerRequest =  new RegisterRequest("aaa","asdada","asdasdsdsa","susan","Ellis","f");

        registerService = new RegisterService();
        registerResult = registerService.register(registerRequest);

    }

    @Test
    public void clearPass() throws DataAccessException {

        Database db = new Database();
        Connection conn2 = db.getConnection();
        EventDao insertEventDao = new EventDao(conn2);
        Event event = new Event("Sheila_Birth","sheila","Sheila_Parker",-36.1833f,144.9667f,"Australia",
                "Melbourne","birth",1970);

        insertEventDao.insert(event);
        db.closeConnection(true);


        clearRequest = new ClearRequest();
        ClearService clearService = new ClearService();
        clearService.clearDB(clearRequest);

        String expected = "";

        Connection conn = db.openConnection();
        UserDao userDao= new UserDao(conn);
        PersonDao personDao = new PersonDao(conn);
        EventDao eventDao = new EventDao(conn);
        AuthtokenDao authtokenDao = new AuthtokenDao(conn);



        assertNull( personDao.retrieve(registerResult.getPersonID()));
        assertNull(eventDao.retrieve(event.getEventID()));
        assertNull(authtokenDao.retrieve(registerResult.getAuthtoken()));
        assertNull(userDao.retrieve("aaa"));
        db.closeConnection(true);

    }

}
