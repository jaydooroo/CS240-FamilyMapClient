package dao;

import dataAccess.AuthtokenDao;
import dataAccess.DataAccessException;
import dataAccess.Database;
import model.Authtoken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class AuthtokenDaoTest {

    private Database db;
    private Authtoken bestToken;
    private AuthtokenDao aDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();

        bestToken = new Authtoken("aaaab","James");

        Connection conn = db.getConnection();
        aDao = new AuthtokenDao(conn);
        aDao.clear();
    }

    @AfterEach
    public void tearDown() {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        aDao.insert(bestToken);
        Authtoken compareTest = aDao.retrieve(bestToken.getAuthtoken());
        assertNotNull(compareTest);
        assertEquals(bestToken,compareTest);
    }
    @Test
    public void insertFail () throws DataAccessException {
        aDao.insert(bestToken);

        assertThrows(DataAccessException.class, () -> aDao.insert(bestToken));

    }
    @Test
    public void retrievePass() throws DataAccessException {
        aDao.insert(bestToken);
        Authtoken compareTest = aDao.retrieve(bestToken.getAuthtoken());
        assertNotNull(compareTest);
        assertEquals(bestToken,compareTest);

    }

    @Test
    public void retrieveFail() throws DataAccessException {
        aDao.insert(bestToken);

        assertNull(aDao.retrieve("aaaaa"));
    }

    @Test
    public void clearPass () throws DataAccessException {
        aDao.insert(bestToken);
        aDao.clear();
        assertNull(aDao.retrieve(bestToken.getAuthtoken()));
    }

}
