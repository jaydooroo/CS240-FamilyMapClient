package serviceTest;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.ClearRequest;
import request.RegisterRequest;
import request.SinglePersonRequest;
import result.RegisterResult;
import result.SinglePersonResult;
import services.ClearService;
import services.RegisterService;
import services.SinglePersonService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SinglePersonTest {

    private SinglePersonService singlePersonService;
    private SinglePersonRequest bestRequest;

    @BeforeEach
    public void setUp() throws DataAccessException {

        ClearRequest clearRequest = new ClearRequest();
        ClearService clearService = new ClearService();
        clearService.clearDB(clearRequest);


        singlePersonService = new SinglePersonService();

    }

    @Test
    public void testPerson() throws DataAccessException {



        RegisterRequest registerRequest =  new RegisterRequest("sheila","asdada","asdasdsdsa","susan","Ellis","f");
        RegisterService registerService = new RegisterService();
        RegisterResult registerResult =  registerService.register(registerRequest);

        bestRequest = new SinglePersonRequest(registerResult.getPersonID(), registerResult.getAuthtoken());
        SinglePersonResult singlePersonResult = singlePersonService.singlePerson(bestRequest);

        assertNotNull(singlePersonResult);
        assertEquals(registerRequest.getFirstName(), singlePersonResult.getFirstName());
        assertEquals(registerRequest.getLastName(), singlePersonResult.getLastName());
        assertEquals(true, singlePersonResult.isSuccess());


    }

    @Test
    public void testFailPerson() throws DataAccessException {



        RegisterRequest registerRequest =  new RegisterRequest("sheila","asdada","asdasdsdsa","susan","Ellis","f");
        RegisterService registerService = new RegisterService();
        RegisterResult registerResult =  registerService.register(registerRequest);

        bestRequest = new SinglePersonRequest( "asdeqw", registerResult.getAuthtoken());
        SinglePersonResult singlePersonResult = singlePersonService.singlePerson(bestRequest);

        assertNotNull(singlePersonResult);
        assertEquals(false, singlePersonResult.isSuccess());


    }
}
