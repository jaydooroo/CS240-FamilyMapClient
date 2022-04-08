package dao;

import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.UserDao;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class UserDaoTest {

    private Database db;
    private User bestUser;
    private UserDao uDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();

        bestUser = new User("Gale", "ddd1234", "lagalbi555@gmail.com", "Chris",
                "Mike", "m", "Gale123A");

        Connection conn = db.getConnection();
        uDao = new UserDao(conn);
        uDao.clear();
    }

    @AfterEach
    public void tearDow() {
        db.closeConnection(false);
    }
    @Test
    public void insertPass() throws DataAccessException {
        uDao.insert(bestUser);
        User compareTest= uDao.retrieve(bestUser.getUsername());
        assertNotNull(compareTest);
        assertEquals(bestUser,compareTest);
    }

    @Test
    public void retrievePass() throws DataAccessException {
        uDao.insert(bestUser);
        User compareTest = uDao.retrieve(bestUser.getUsername());
        assertNotNull(compareTest);
        assertEquals(bestUser,compareTest);

    }

    @Test
    public void retrieveFail() throws DataAccessException {

        uDao.insert(bestUser);
        assertNull( uDao.retrieve("aaaaa"));

    }

    @Test
    public void insertFail() throws DataAccessException {

        uDao.insert(bestUser);

        assertThrows(DataAccessException.class, () -> uDao.insert(bestUser));

    }

    @Test
    public void clearPass() throws DataAccessException {

        uDao.insert(bestUser);
        uDao.clear();
        assertNull(uDao.retrieve(bestUser.getUsername()));
    }

}
