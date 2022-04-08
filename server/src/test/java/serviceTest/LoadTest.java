package serviceTest;

import dataAccess.*;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.ClearRequest;
import request.LoadRequest;
import result.LoadResult;
import services.ClearService;
import services.LoadService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoadTest {
    private LoadRequest bestRequest;
    private LoadService loadService;


    @BeforeEach
    public void setUp() throws DataAccessException {

        ClearRequest clearRequest = new ClearRequest();
        ClearService clearService = new ClearService();
        clearService.clearDB(clearRequest);



    }

    @Test
    public void LoadPass() throws DataAccessException {

        Person person = new Person("Sheila_Parker","sheila","Sheila","Parker",
                "f","Blaine_McGary","Betty_White","Davis_Hyer");


        User user = new User("sheila","parker","sheila@parker.com","Sheila",
                "Parker","f","Sheila_Parker");

        Event event = new Event("Sheila_Birth","sheila","Sheila_Parker",-36.1833f,144.9667f,"Australia",
                "Melbourne","birth",1970);

        Person[] people = new Person[1];
        people[0] = person;

        User[] users = new User[1];
        users[0] = user;

        Event[] events = new Event[1];
        events[0] = event;



        bestRequest = new LoadRequest();
        bestRequest.setPersons(people);
        bestRequest.setUsers(users);
        bestRequest.setEvents(events);

        loadService = new LoadService();

        LoadResult loadResult;
        loadResult = loadService.load(bestRequest);


        Database db = new Database();
        Connection conn = db.getConnection();

        PersonDao personDao = new PersonDao(conn);
        EventDao eventDao = new EventDao(conn);
        UserDao userDao = new UserDao(conn);

        Person dbPerson = personDao.retrieve(person.getPersonID());
        Event dbEvent = eventDao.retrieve(event.getEventID());
        User dbUser = userDao.retrieve(user.getUsername());

        db.closeConnection(true);

        assertNotNull(loadResult);
        assertEquals(true, loadResult.isSuccess());
        assertEquals(user,dbUser);
        assertEquals(person,dbPerson);
        assertEquals(event,dbEvent);

    }

    @Test
    public void LoadFail () {
        LoadResult loadResult;
        bestRequest = new LoadRequest();
        loadService = new LoadService();

        User user = new User("sheila","parker","sheila@parker.com","Sheila",
                "Parker","adssd","Sheila_Parker");
        User[] users = new User[1];
        users[0] = user;

        bestRequest.setUsers(users);

        loadResult = loadService.load(bestRequest);

        assertNotNull(loadResult);
        assertEquals(false, loadResult.isSuccess());

    }




}
