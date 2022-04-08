package serviceTest;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.ClearRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import services.ClearService;
import services.LoginService;
import services.RegisterService;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    private LoginRequest bestRequest;
    private LoginService loginService;
    private RegisterService register;


    @BeforeEach
    public void setUp() throws DataAccessException {
        ClearRequest clearRequest = new ClearRequest();
        ClearService clearService = new ClearService();
        clearService.clearDB(clearRequest);

       RegisterRequest registerRequest =  new RegisterRequest("aaa","asdada","asdasdsdsa","susan","Ellis","f");

       register = new RegisterService();
       register.register(registerRequest);

        bestRequest = new LoginRequest("aaa","asdada");

        loginService = new LoginService();


    }

    @AfterEach
    public void erase() {


    }


    @Test
    public void loginPass () {
        LoginResult loginResult;
        loginResult = loginService.login(bestRequest);


        assertNotNull(loginResult);
        assertEquals(loginResult.getUsername(),bestRequest.getUsername());
        assertNotNull(loginResult.getAuthtoken());
    }

    @Test
    public void loginFail() {
        LoginResult loginResult;

        bestRequest = new LoginRequest("aaa", "adswert");
        loginResult = loginService.login(bestRequest);

        assertNotNull(loginResult);
        assertNotNull(loginResult.getMessage());
        assertNull(loginResult.getAuthtoken());

    }
}
