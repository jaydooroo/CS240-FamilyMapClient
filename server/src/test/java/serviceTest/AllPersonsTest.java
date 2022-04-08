package serviceTest;

import dataAccess.DataAccessException;
import model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.AllPersonRequest;
import request.ClearRequest;
import request.RegisterRequest;
import result.AllPersonResult;
import result.RegisterResult;
import services.AllPersonService;
import services.ClearService;
import services.RegisterService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AllPersonsTest {

    private AllPersonService allPersonService;
    private AllPersonRequest bestRequest;


    @BeforeEach
    public void setUp() throws DataAccessException {
        ClearRequest clearRequest = new ClearRequest();
        ClearService clearService = new ClearService();
        clearService.clearDB(clearRequest);

        allPersonService = new AllPersonService();

    }


    @Test
    public void testAllPeople() throws DataAccessException {

        RegisterRequest registerRequest =  new RegisterRequest("sheila","asdada","asdasdsdsa","susan","Ellis","f");
        RegisterService registerService = new RegisterService();
        RegisterResult registerResult =  registerService.register(registerRequest);

        bestRequest = new AllPersonRequest(registerResult.getAuthtoken());

        AllPersonResult allPersonResult = allPersonService.person(bestRequest);


        Person person =  allPersonResult.getData().get(0);

        assertNotNull(allPersonResult);
        assertEquals(true,allPersonResult.isSuccess());
        assertEquals(registerRequest.getUsername(),person.getAssociatedUsername());

    }

    @Test
    public void testFailAllPeople() {
        RegisterRequest registerRequest =  new RegisterRequest("sheila","asdada","asdasdsdsa","susan","Ellis","f");
        RegisterService registerService = new RegisterService();
        RegisterResult registerResult =  registerService.register(registerRequest);

        bestRequest = new AllPersonRequest("asdasd");
        AllPersonResult allPersonResult = allPersonService.person(bestRequest);

        assertNotNull(allPersonResult);
        assertEquals(false,allPersonResult.isSuccess());


    }




}
