import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;

import Server.ServerProxy;
import model.Event;
import model.Person;
import model.User;
import request.AllEventRequest;
import request.AllPersonRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.AllEventResult;
import result.AllPersonResult;
import result.LoginResult;
import result.RegisterResult;

public class serverTest {
    private RegisterResult masterRegisterResult;
    ServerProxy serverProxy;

    @Before
    public void setUp() throws IOException {

        serverProxy = new ServerProxy("127.0.0.1", "8080");
        serverProxy.clear();
        RegisterRequest registerRequest = new RegisterRequest("ejh612", "dlwpgus", "ads", "Jehyeon", "Lee", "m");
        masterRegisterResult = serverProxy.register(registerRequest);
    }

    @Test
    public void loginPass () throws IOException {
        LoginResult loginResult;
        LoginRequest loginRequest = new LoginRequest("ejh612", "dlwpgus");


        loginResult = serverProxy.login(loginRequest);


        Assert.assertNotNull(loginResult);
        Assert.assertEquals(loginResult.getUsername(), masterRegisterResult.getUsername());
        Assert.assertNotNull(loginResult.getAuthtoken());
    }

    @Test
    public void loginFail()  {
        LoginResult loginResult;
        LoginRequest loginRequest = new LoginRequest("ejh612", "dlwpgudsasdfs");


        try {
            loginResult = serverProxy.login(loginRequest);
        } catch (IOException e) {
            Assert.assertEquals("Http Access Error", e.getMessage());
        }


    }

    @Test
    public void registerPass () throws IOException {
        RegisterResult regiResult;
        RegisterRequest bestRequest = new RegisterRequest("aaaftyfytse66","asdada","asdasdsdsa","susan","Ellis","f");

        regiResult = serverProxy.register(bestRequest);

        Assert.assertNotNull(regiResult);
        Assert.assertEquals(true, regiResult.isSuccess());
    }

    @Test
    public void registerFail ()  {
        RegisterResult regiResult;
        RegisterRequest bestRequest = new RegisterRequest("aaaftyfytse66","asdada","asdasdsdsa","susan","Ellis","faa");
        try {
            regiResult = serverProxy.register(bestRequest);
        } catch (IOException e) {
            Assert.assertEquals("Http Access Error", e.getMessage());
        }

    }

    @Test
    public void testAllEvents() throws IOException {


        AllEventRequest  bestRequest = new AllEventRequest(masterRegisterResult.getAuthtoken());

        AllEventResult allEventResult = serverProxy.getAllEvents(bestRequest);

        Event event = allEventResult.getData().get(0);
        Assert.assertNotNull(allEventResult);
        Assert.assertEquals(true,allEventResult.isSuccess());
        Assert.assertEquals(masterRegisterResult.getUsername(),event.getAssociatedUsername());


    }

    @Test
    public void testFailAllEvents()  {


        AllEventRequest  bestRequest = new AllEventRequest("Asdsa");

        try {
            AllEventResult allEventResult = serverProxy.getAllEvents(bestRequest);
        } catch (IOException e) {
            Assert.assertEquals("Http Access Error", e.getMessage());
        }
    }


    @Test
    public void testAllPeople() throws IOException {



        AllPersonRequest bestRequest = new AllPersonRequest(masterRegisterResult.getAuthtoken());

        AllPersonResult allPersonResult = serverProxy.getAllPeople(bestRequest);


        Person person =  allPersonResult.getData().get(0);

        Assert.assertNotNull(allPersonResult);
        Assert.assertEquals(true,allPersonResult.isSuccess());
        Assert.assertEquals(masterRegisterResult.getUsername(),person.getAssociatedUsername());

    }

    @Test
    public void testFailAllPeople()  {

        AllPersonRequest bestRequest = new AllPersonRequest("ASdsadsa");

        try {
            AllPersonResult allPersonResult = serverProxy.getAllPeople(bestRequest);
        } catch (IOException e) {
            Assert.assertEquals("Http Access Error", e.getMessage());
        }

    }


}
