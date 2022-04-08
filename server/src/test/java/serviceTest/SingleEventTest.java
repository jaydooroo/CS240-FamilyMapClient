package serviceTest;

import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.EventDao;
import model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.ClearRequest;
import request.RegisterRequest;
import request.SingleEventRequest;
import result.RegisterResult;
import result.SingleEventResult;
import services.ClearService;
import services.RegisterService;
import services.SingleEventService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SingleEventTest {

    private SingleEventService singleEventService;
    private SingleEventRequest bestRequest;

    @BeforeEach
    public void setUp() throws DataAccessException {

        ClearRequest clearRequest = new ClearRequest();
        ClearService clearService = new ClearService();
        clearService.clearDB(clearRequest);

        singleEventService = new SingleEventService();

    }
    @Test
    public void testEvent() throws DataAccessException {
        RegisterRequest registerRequest =  new RegisterRequest("sheila","asdada","asdasdsdsa","susan","Ellis","f");
        RegisterService registerService = new RegisterService();
        RegisterResult registerResult =  registerService.register(registerRequest);

        Event event = new Event("Sheila_Birth","sheila","Sheila_Parker",-36.1833f,144.9667f,"Australia",
                "Melbourne","birth",1970);

        Database db = new Database();
        Connection conn = db.getConnection();

        EventDao eventDao =new EventDao(conn);

        eventDao.insert(event);

        db.closeConnection(true);


        bestRequest = new SingleEventRequest(event.getEventID(),registerResult.getAuthtoken());
        SingleEventResult singleEventResult = singleEventService.singleEvent(bestRequest);

        assertNotNull(singleEventResult);
        assertEquals(event.getCity(), singleEventResult.getCity());
        assertEquals(event.getEventID(), singleEventResult.getEventID());
        assertEquals(true, singleEventResult.isSuccess());


    }

    @Test
    public void testFailEvent() throws DataAccessException {



        RegisterRequest registerRequest =  new RegisterRequest("sheila","asdada","asdasdsdsa","susan","Ellis","f");
        RegisterService registerService = new RegisterService();
        RegisterResult registerResult =  registerService.register(registerRequest);

        Event event = new Event("Sheila_Birth","sheila","Sheila_Parker",-36.1833f,144.9667f,"Australia",
                "Melbourne","birth",1970);

        Database db = new Database();
        Connection conn = db.getConnection();

        EventDao eventDao =new EventDao(conn);

        eventDao.insert(event);

        db.closeConnection(true);


        bestRequest = new SingleEventRequest("Asdasd",registerResult.getAuthtoken());
        SingleEventResult singleEventResult = singleEventService.singleEvent(bestRequest);

        assertNotNull(singleEventResult);
        assertEquals(false, singleEventResult.isSuccess());


    }


}
