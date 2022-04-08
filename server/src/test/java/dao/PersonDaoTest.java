package dao;

import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.PersonDao;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDaoTest {
    private Database db;
    private Person bestPerson;
    private PersonDao pDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();

        bestPerson = new Person("Gale","Jay","Chris","Mike","m",
                "fafa123","mama123","wife123");

        Connection conn = db.getConnection();
        pDao = new PersonDao(conn);
        pDao.clear();
    }

    @AfterEach
    public void tearDown() {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        pDao.insert(bestPerson);
        Person compareTest = pDao.retrieve(bestPerson.getPersonID());
        assertNotNull(compareTest);
        assertEquals(bestPerson,compareTest);
    }
    @Test
    public void insertFail () throws DataAccessException {
        pDao.insert(bestPerson);

        assertThrows(DataAccessException.class, () -> pDao.insert(bestPerson));

    }
    @Test
    public void retrievePass() throws DataAccessException {
        pDao.insert(bestPerson);
        Person compareTest = pDao.retrieve(bestPerson.getPersonID());
        assertNotNull(compareTest);
        assertEquals(bestPerson,compareTest);

    }

    @Test
    public void retrieveFail() throws DataAccessException {

        pDao.insert(bestPerson);

        assertNull( pDao.retrieve("aaaaa"));

    }

    @Test
    public void clearPass () throws DataAccessException {
        pDao.insert(bestPerson);
        pDao.clear();
        assertNull(pDao.retrieve(bestPerson.getPersonID()));
    }

}
