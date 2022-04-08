package services.helper;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.EventDao;
import dataAccess.PersonDao;
import model.Event;
import model.Person;
import model.User;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.util.Random;
import java.util.UUID;

public class GeneratePerson {
    private final Connection conn;

    private final User user;
    private final int generations;
    private Person userPerson;

    //check if the generations is less than 0
    public GeneratePerson(User user, int generations, Connection conn) throws DataAccessException, FileNotFoundException {
        this.user = user;
        this.conn = conn;
        this.userPerson = new Person();
        if(generations > 0 ) {
            this.generations = generations + 1;
            SetInitialUserPerson();
        }
        else {
            throw new DataAccessException("ERROR:less than 0 generations populate request");
        }

        this.userPerson = generatePerson(null,this.generations,null);
    }

    public GeneratePerson(User user, Connection conn) throws FileNotFoundException, DataAccessException {
        this.user = user;
        this.generations = 5;
        this.conn = conn;
        this.userPerson = new Person();
        SetInitialUserPerson();
        this.userPerson = generatePerson(null,this.generations,null );

    }

    private void SetInitialUserPerson () {
        userPerson.setFirstName(user.getFirstName());
        userPerson.setLastName(user.getLastName());
        userPerson.setPersonID(user.getPersonID());
        userPerson.setGender(user.getGender());
        userPerson.setSpouseID(null);
    }

    public Person generatePerson (Person person, int generations, Event currentPersonEvent) throws FileNotFoundException, DataAccessException {

        Person mother = null;
        Person father = null;
        // Do not insert personEvent
        Event personEvent;
        Event motherEvent = new Event();
        Event fatherEvent = new Event();
        Event motherMarriageEvent = new Event();
        Event fatherMarriageEvent = new Event();
        Event motherDeathEvent = new Event();
        Event fatherDeathEvent = new Event();

        //Setting person properties
        if (this.generations == generations) {
            person = userPerson;
            person.setAssociatedUsername(user.getUsername());
            personEvent = SetBirthEvent(person);

            //putting the current person birth event into database
            EventDao eventIntoDataBase = new EventDao(conn);
            eventIntoDataBase.insert(personEvent);
        }
        else {
            personEvent = currentPersonEvent;
        }

        if (generations > 1) {

            mother = setMother(person);
            father = setFather(person);

            // mother, father birthEvent generation
            motherEvent = SetBirthEvent(personEvent,mother);
            fatherEvent = SetBirthEvent(personEvent,father);

            // mother,father marriageEvent generation
            Event tempMarriageEvent = SetMarriageEvent(motherEvent,fatherEvent);
            motherMarriageEvent = CopyMarriageEvent(tempMarriageEvent,mother);
            fatherMarriageEvent = CopyMarriageEvent(tempMarriageEvent,father);

            // mother,father deathEvent generation
            motherDeathEvent = SetDeathEvent(personEvent,tempMarriageEvent,motherEvent,mother);
            fatherDeathEvent = SetDeathEvent(personEvent,tempMarriageEvent,fatherEvent,father);


            //setting spouse ID
            mother.setSpouseID(father.getPersonID());
            father.setSpouseID(mother.getPersonID());

            mother = generatePerson(mother,generations -1,motherEvent);
            father = generatePerson(father, generations -1,fatherEvent);

        }

        if (mother == null || father == null) {
            person.setFatherID(null);
            person.setMotherID(null);

        }
        else {
            person.setFatherID(father.getPersonID());
            person.setMotherID(mother.getPersonID());

            //inserting all the events for the mother and father into database.
            EventDao eventIntoDataBase = new EventDao(conn);
            eventIntoDataBase.insert(motherEvent);
            eventIntoDataBase.insert(fatherEvent);
            eventIntoDataBase.insert(fatherMarriageEvent);
            eventIntoDataBase.insert(motherMarriageEvent);
            eventIntoDataBase.insert(motherDeathEvent);
            eventIntoDataBase.insert(fatherDeathEvent);
        }


        PersonDao personIntoDataBase = new PersonDao(conn);
        personIntoDataBase.insert(person);

        return person;
    }

    public Person setMother (Person child) throws FileNotFoundException, DataAccessException {
        Person mother = new Person();

        mother.setPersonID(UUID.randomUUID().toString());
        mother.setAssociatedUsername(user.getUsername());
        mother.setGender("m");
        SetRandomFirstName(mother);
        SetRandomLastName(mother);

        return mother;
    }

    public Person setFather(Person child) throws FileNotFoundException, DataAccessException {

        Person father = new Person();

        father.setPersonID(UUID.randomUUID().toString());
        father.setAssociatedUsername(user.getUsername());
        father.setGender("f");
        SetRandomFirstName(father);
        father.setLastName(child.getLastName());

        return father;

    }

    public void SetRandomFirstName (Person person) throws DataAccessException, FileNotFoundException {

        Gson gson = new Gson();
        NameString firstNameList;
        String gender = person.getGender();

        if (gender.equals("f")) {
            Reader reader = new FileReader("json/fnames.json");
            firstNameList = gson.fromJson(reader, NameString.class);

        }
        else if (gender.equals("m")) {
            Reader reader = new FileReader("json/mnames.json");
            firstNameList = gson.fromJson(reader,NameString.class);
        }
        else{
            throw new DataAccessException("ERROR: Wrong Gender Type");
        }
        Random r = new Random();
        int randomNumber = r.nextInt(firstNameList.getData().length);

        person.setFirstName(firstNameList.getData()[randomNumber]);
    }

    public void SetRandomLastName (Person person) throws FileNotFoundException {

        Gson gson = new Gson();
        NameString lastNameList;


            Reader reader = new FileReader("json/snames.json");
            lastNameList = gson.fromJson(reader,NameString.class);

        Random r = new Random();
        int randomNumber = r.nextInt(lastNameList.getData().length);

        person.setLastName(lastNameList.getData()[randomNumber]);
    }


    public Event SetMarriageEvent ( Event motherBirth, Event fatherBirth) throws FileNotFoundException {
        Event marriageEvent =new Event();
        int maxBirth;
        int randomNumber;
        int minBirth;
        Random r = new Random();


        maxBirth = Math.max(motherBirth.getYear(), fatherBirth.getYear());
        minBirth = Math.min(motherBirth.getYear(), fatherBirth.getYear());
        randomNumber = maxBirth + 13 + r.nextInt(80 - (maxBirth-minBirth) - 13);
        System.out.println( "SetMarriage: " + (80 - (maxBirth-minBirth) - 13) + " \n" +
                "maxBirth : " + maxBirth + "\n" + "minBirth: " + minBirth + "\n" +
                "Marriage Date: " + randomNumber + "\n");


        marriageEvent.setYear(randomNumber);
        marriageEvent.setAssociatedUsername(this.user.getUsername());
        marriageEvent.setEventType("Marriage");

        SetRandomLocation(marriageEvent);

        return marriageEvent;
    }
    public Event SetBirthEvent (Event childrenBirth, Person person) throws FileNotFoundException, DataAccessException {
        Event birthEvent = new Event();

        birthEvent.setEventID(UUID.randomUUID().toString());
        birthEvent.setAssociatedUsername(this.user.getUsername());
        birthEvent.setPersonID(person.getPersonID());
        SetRandomLocation(birthEvent);
        birthEvent.setEventType("birth");
        Random r = new Random();
        int randomNumber;

        if (childrenBirth == null) {
            randomNumber = 1902 + r.nextInt(120);

        }
        else if (person.getGender().equals("m")) {
            int basicYear = childrenBirth.getYear() - 50;

            // childrenBirth -13 >= parentBirth > childrenBirth - 51
            randomNumber = basicYear + r.nextInt(37);
        }
        else {
            int basicYear = childrenBirth.getYear() - 60;
            randomNumber = basicYear + r.nextInt(47);
        }
        birthEvent.setYear(randomNumber);

      return birthEvent;
    }

    //Initial SetBirthEvent
    public Event SetBirthEvent (Person person) throws FileNotFoundException {
        Event birthEvent = new Event();

        birthEvent.setEventID(UUID.randomUUID().toString());
        birthEvent.setAssociatedUsername(this.user.getUsername());
        birthEvent.setPersonID(person.getPersonID());
        SetRandomLocation(birthEvent);
        birthEvent.setEventType("birth");
        Random r = new Random();
        int randomNumber;

        randomNumber = 1902 + r.nextInt(120);

        birthEvent.setYear(randomNumber);

        return birthEvent;
    }

    public Event SetDeathEvent (Event children, Event marriage, Event personBirth, Person person ) throws FileNotFoundException {
        Event deathEvent = new Event();
        int minDeathYear;
        int maxDeathYear;
        int randomDeathYear;

        if(children.getYear() > marriage.getYear()) {

            // parentDeath > childBirth
            minDeathYear = children.getYear() +1;
        }
        else {
            // parentDeath > marriageDate
            minDeathYear = marriage.getYear() +1 ;
        }

        maxDeathYear = 120 + personBirth.getYear();

        Random r = new Random();

        randomDeathYear = minDeathYear + r.nextInt(maxDeathYear - minDeathYear );

        deathEvent.setEventID(UUID.randomUUID().toString());
        deathEvent.setAssociatedUsername(this.user.getUsername());
        deathEvent.setEventType("death");
        SetRandomLocation(deathEvent);
        deathEvent.setPersonID(person.getPersonID());
        deathEvent.setYear(randomDeathYear);

        return deathEvent;
    }

    public void SetRandomLocation (Event event) throws FileNotFoundException {
        LocationData locationHelpers;
        Gson gson = new Gson();

        Reader reader = new FileReader("json/locations.json");
        locationHelpers =  gson.fromJson(reader,LocationData.class);

        Random r = new Random();

        int randomIndex = r.nextInt(locationHelpers.getData().length);

        event.setCountry(locationHelpers.getData()[randomIndex].getCountry());
        event.setCity(locationHelpers.getData()[randomIndex].getCity());
        event.setLatitude(locationHelpers.getData()[randomIndex].getLatitude());
        event.setLongitude(locationHelpers.getData()[randomIndex].getLongitude());

    }

    public Event CopyMarriageEvent (Event marriage, Person person) {
        Event copyEvent = new Event();

        copyEvent.setEventID(UUID.randomUUID().toString());
        copyEvent.setPersonID(person.getPersonID());

        copyEvent.setAssociatedUsername(marriage.getAssociatedUsername());
        copyEvent.setLatitude(marriage.getLatitude());
        copyEvent.setLongitude(marriage.getLongitude());
        copyEvent.setCountry(marriage.getCountry());
        copyEvent.setCity(marriage.getCity());
        copyEvent.setEventType(marriage.getEventType());
        copyEvent.setYear(marriage.getYear());

        return copyEvent;
    }

}
