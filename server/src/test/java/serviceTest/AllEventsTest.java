package serviceTest;

import dataAccess.DataAccessException;
import model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.AllEventRequest;
import request.ClearRequest;
import request.RegisterRequest;
import result.AllEventResult;
import result.RegisterResult;
import services.AllEventService;
import services.ClearService;
import services.RegisterService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AllEventsTest {

    private AllEventService allEventService;
    private AllEventRequest bestRequest;


    @BeforeEach
    public void setUp() throws DataAccessException {
        ClearRequest clearRequest = new ClearRequest();
        ClearService clearService = new ClearService();
        clearService.clearDB(clearRequest);

        allEventService = new AllEventService();

    }


    @Test
    public void testAllEvents() throws DataAccessException {

        RegisterRequest registerRequest =  new RegisterRequest("sheila","asdada","asdasdsdsa","susan","Ellis","f");
        RegisterService registerService = new RegisterService();
        RegisterResult registerResult =  registerService.register(registerRequest);

        bestRequest = new AllEventRequest(registerResult.getAuthtoken());

        AllEventResult allEventResult = allEventService.event(bestRequest);

        Event event = allEventResult.getData().get(0);
        assertNotNull(allEventResult);
        assertEquals(true,allEventResult.isSuccess());
        assertEquals(registerRequest.getUsername(),event.getAssociatedUsername());


    }

    @Test
    public void testFailAllEvents() {
        RegisterRequest registerRequest =  new RegisterRequest("sheila","asdada","asdasdsdsa","susan","Ellis","f");
        RegisterService registerService = new RegisterService();
        RegisterResult registerResult =  registerService.register(registerRequest);

        bestRequest = new AllEventRequest("asdasd");
        AllEventResult allEventResult = allEventService.event(bestRequest);

        assertNotNull(allEventResult);
        assertEquals(false,allEventResult.isSuccess());


    }



}
