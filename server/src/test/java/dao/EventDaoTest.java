package dao;

import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.EventDao;
import model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;


public class EventDaoTest {
    private Database db;
    private Event bestEvent;
    private EventDao eDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();

        bestEvent = new Event("abcde","Gale","123aaaa", 22.5f, 33.4f,
                "Korea", "Seoul", "birth", 1996);

        Connection conn = db.getConnection();
        eDao = new EventDao(conn);
        eDao.clear();
    }

    @AfterEach
    public void tearDown () throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void insertPass () throws DataAccessException {
        eDao.insert(bestEvent);
        Event compareTest = eDao.retrieve(bestEvent.getEventID());
        assertNotNull(compareTest);
        assertEquals(bestEvent,compareTest);
    }

    @Test
    public void insertFail () throws DataAccessException {
        eDao.insert(bestEvent);

        assertThrows(DataAccessException.class, () -> eDao.insert(bestEvent));
    }

    @Test
    public void retrievePass() throws DataAccessException {
        eDao.insert(bestEvent);
        Event compareTest = eDao.retrieve(bestEvent.getEventID());
        assertNotNull(compareTest);
        assertEquals(bestEvent,compareTest);
    }

    @Test
    public void retrieveFail() throws DataAccessException {
        eDao.insert(bestEvent);

        assertNull(eDao.retrieve("eewwqq"));
    }

    @Test
    public void clearPass() throws DataAccessException {
        eDao.insert(bestEvent);
        eDao.clear();
        assertNull(eDao.retrieve(bestEvent.getEventID()));
    }
}
