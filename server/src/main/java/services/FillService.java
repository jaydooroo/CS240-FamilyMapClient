package services;

import dataAccess.*;
import model.User;
import request.FillRequest;
import result.FillResult;
import services.helper.GeneratePerson;

import java.io.FileNotFoundException;
import java.sql.Connection;

/***
 * call data access object in order to fill out the database of the information.
 * the private variables are the userDao and PersonDao.
 */
public class FillService {
   private Database db;
   private UserDao userDao;
   private PersonDao personDao;
   private EventDao eventDao ;
   private boolean success;


   /***
    * Constructor which receives userDao, personDao, eventDao data and stores those into class variables.
    * @param userDao
    * @param personDao
    * @param eventDao
    */
   public FillService(UserDao userDao, PersonDao personDao, EventDao eventDao) {
      this.eventDao = eventDao;
      this.personDao = personDao;
      this.userDao = userDao;
   }

   public FillService() {}

   /***
    * Populates the server's database with generated data for the specified username.
    * @param r FillRequest class
    * @return  FillResult class which contains the information of the Fill operation success.
    */
   public FillResult fill (FillRequest r) {
      try{
         db = new Database();
         Connection conn = db.getConnection();

         PersonDao pDao = new PersonDao(conn);
         EventDao eDao = new EventDao(conn);
         UserDao uDao = new UserDao(conn);

         User user = uDao.retrieve(r.getUsername());


         if(user == null) {
            throw new DataAccessException("Error: Invalid username");
         }

         pDao.clearAssociatedUsername(r.getUsername());
         eDao.clearAssociatedUsername(r.getUsername());

         if (r.getGeneration() >= 0) {
            GeneratePerson generatePerson = new GeneratePerson(user,r.getGeneration(),conn);
         }
         else {
            throw new DataAccessException("Error: Invalid generations parameter");
         }

         success = true;
         int generations = r.getGeneration();

         int sum = 0;
         int x = 1;
         // x = 30 if generation is 4 => later x++ to count user person and event
         for(int i = 0; i < generations; i++) {
            sum += (x *2);
            x = x*2;
         }


         int totalGeneratedPersons =  sum + 1;
         int totalGeneratedEvents = sum * 3 + 1;

         FillResult fillResult = new FillResult("Successfully added " + totalGeneratedPersons +
                 " persons and " + totalGeneratedEvents +" events to the database.", success);

         db.closeConnection(true);
         return fillResult;

      } catch (DataAccessException e) {
         success = false;
         e.printStackTrace();
         FillResult fillResult = new FillResult(e.getMessage(),success);

         db.closeConnection(false);
         return fillResult;
      } catch (FileNotFoundException e) {
         success = false;

         e.printStackTrace();
         FillResult fillResult = new FillResult(e.getMessage(),success);

         db.closeConnection(false);
         return fillResult;
      }

   }

   public boolean isSuccess() {
      return success;
   }
}
