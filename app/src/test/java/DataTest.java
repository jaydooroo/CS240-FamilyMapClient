import com.google.gson.Gson;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Data.DataCache;
import Data.Family;
import Server.GetDataTask;
import UI.SearchActivity;
import model.Event;
import model.Person;
import request.LoadRequest;

public class DataTest {
    DataCache dataCache = DataCache.getInstance();

    @Before
    public void setUp() throws FileNotFoundException {

        Gson gson = new Gson();
        FileReader fileReader = new FileReader("src/test/java/testSrc.txt");
        LoadRequest loadRequest = gson.fromJson(fileReader,LoadRequest.class);

        Person[] people = loadRequest.getPersons();
        Event[] events = loadRequest.getEvents();
        dataCache.setPeople(new HashMap<String,Person>());
        dataCache.setEvents( new HashMap<String, Event>());
        dataCache.setPersonEvents(new HashMap<String, List<Event>>());

        for(int i = 0; i < people.length; i++) {

            dataCache.getPeople().put(people[i].getPersonID(), people[i]);
        }
        for(int i = 0; i <events.length; i++) {

            dataCache.getEvents().put(events[i].getEventID(), events[i]);
        }

        dataCache.setPersonEvents(createPeopleEvents(loadRequest.getEvents()));
        HashSet<String> maternalAncestor = new HashSet<String>();
        HashSet<String> paternalAncestor = new HashSet<String>();


        ancestorDivision(people[0].getFatherID(),paternalAncestor, (HashMap<String, Person>) dataCache.getPeople());
        ancestorDivision(people[0].getMotherID(),maternalAncestor, (HashMap<String, Person>) dataCache.getPeople());

        dataCache.setMaternalAncestors(maternalAncestor);
        dataCache.setPaternalAncestors(paternalAncestor);

        dataCache.setPersonID(people[0].getPersonID());
        dataCache.setAuthtoken("aaa");

    }

    @After
    public void cleanUp() {
        dataCache.clear();
    }

    @Test
    public void calculateSpouse() {

        Person user = dataCache.getPeople().get(dataCache.getPersonID());
        Person spouse = dataCache.getPeople().get(user.getSpouseID());
        Assert.assertEquals("Davis_Hyer", spouse.getPersonID());

        user = dataCache.getPeople().get("Blaine_McGary");
        spouse = dataCache.getPeople().get(user.getSpouseID());
        Assert.assertEquals("Betty_White", spouse.getPersonID());

        user = dataCache.getPeople().get("Ken_Rodham");
        spouse = dataCache.getPeople().get(user.getSpouseID());
        Assert.assertEquals("Mrs_Rodham", spouse.getPersonID());
    }

    @Test
    public void calculateFamily() {
        Person user = dataCache.getPeople().get(dataCache.getPersonID());

        dataCache.setFamilyRelationship(user.getFatherID());
        boolean hasChild = false;

// child comparison
            for(Family elem : dataCache.getFamilyList()) {
                if(elem.getRelationship().toLowerCase().equals("child")) {
                   if( elem.getPerson().getPersonID().equals("Sheila_Parker")) {
                       hasChild =true;
                   }
                }
            }

        Assert.assertEquals(true, hasChild);

            hasChild = false;

// Spouse comparison
        for(Family elem : dataCache.getFamilyList()) {
            if(elem.getRelationship().toLowerCase().equals("spouse")) {
                if( elem.getPerson().getPersonID().equals("Betty_White")) {
                    hasChild =true;
                }
            }
        }

        Assert.assertEquals(true, hasChild);

        hasChild = false;
        // Mother comparison
        for(Family elem : dataCache.getFamilyList()) {
            if(elem.getRelationship().toLowerCase().equals("mother")) {
                if( elem.getPerson().getPersonID().equals("Mrs_Rodham")) {
                    hasChild =true;
                }
            }
        }

        Assert.assertEquals(true, hasChild);

        hasChild = false;
        // father comparison
        for(Family elem : dataCache.getFamilyList()) {
            if(elem.getRelationship().toLowerCase().equals("father")) {
                if( elem.getPerson().getPersonID().equals("Ken_Rodham")) {
                    hasChild =true;
                }
            }
        }

        Assert.assertEquals(true, hasChild);

    }

    @Test
    public void femaleFilter() {
        dataCache.setFemaleEventsOn(false);
        dataCache.filterEvents();

        boolean hasFemaleEvents = false;

        for(Event elem: dataCache.getFilteredEvents().values()) {
            Person person = dataCache.getPeople().get(elem.getPersonID());
            if(person.getGender().toLowerCase().equals("f")) {
                hasFemaleEvents = true;
            }
        }


        Assert.assertEquals(false,hasFemaleEvents);

    }


    @Test
    public void maleFilter() {
        dataCache.setMaleEventsOn(false);
        dataCache.filterEvents();

        boolean hasMaleEvents = false;

        for(Event elem: dataCache.getFilteredEvents().values()) {
            Person person = dataCache.getPeople().get(elem.getPersonID());
            if(person.getGender().toLowerCase().equals("m")) {
                hasMaleEvents = true;
            }
        }


        Assert.assertEquals(false,hasMaleEvents);
    }

    @Test
    public void MaternalFilter() {
        dataCache.setMotherSideOn(false);
        dataCache.filterEvents();

        boolean hasMaternalEvents = false;

        for(Event elem: dataCache.getFilteredEvents().values()) {
            Person person = dataCache.getPeople().get(elem.getPersonID());
            if(person.getPersonID().toLowerCase().equals("Betty_White")) {
                hasMaternalEvents = true;
            }
        }


        Assert.assertEquals(false,hasMaternalEvents);
    }

    @Test
    public void paternalFilter() {
        dataCache.setFatherSideOn(false);
        dataCache.filterEvents();

        boolean hasPaternalEvents = false;

        for(Event elem: dataCache.getFilteredEvents().values()) {
            Person person = dataCache.getPeople().get(elem.getPersonID());
            if(person.getPersonID().toLowerCase().equals("Blaine_McGary")) {
                hasPaternalEvents = true;
            }
        }


        Assert.assertEquals(false,hasPaternalEvents);
    }

    @Test
    public void sortTest() {
        dataCache.sortEventLists();

        List<Event> events = (List<Event>) dataCache.getPersonEvents().get("Ken_Rodham");

        Assert.assertEquals("Graduated from BYU", events.get(0).getEventType());
        Assert.assertEquals("marriage", events.get(1).getEventType());

    }

    @Test
    public void searchTest() {

        dataCache.filterEvents();

        List<Event> searchEventList = new ArrayList<Event>();
        List<Person> searchPersonList = new ArrayList<Person>();

        eventSearch("birth",searchEventList);
        Assert.assertEquals(3,searchEventList.size());

        searchEventList = new ArrayList<Event>();

        personSearch("she",searchPersonList);
        eventSearch("she",searchEventList);
        Assert.assertEquals(1,searchPersonList.size());
        Assert.assertEquals(5,searchEventList.size());

    }



    private HashMap<String, List<Event>> createPeopleEvents (Event[] list)  {

        HashMap<String, List<Event>> peopleListHashMap = new HashMap<String, List<Event>>();

        for(Event elem: list) {

            if(peopleListHashMap.containsKey(elem.getPersonID())) {
                peopleListHashMap.get(elem.getPersonID()).add(elem);
            }
            else {
                LinkedList<Event> linkedList = new LinkedList<Event>();
                linkedList.add(elem);
                peopleListHashMap.put(elem.getPersonID(),linkedList);
            }
        }

        return peopleListHashMap;
    }

    private void ancestorDivision(String id, HashSet<String> ancestor, HashMap<String,Person> people) {
        Person currentPerson = people.get(id);
        ancestor.add(id);

        if( !currentPerson.getFatherID().equals("")) {
            ancestorDivision(currentPerson.getFatherID(),ancestor,people);
        }

        if (!currentPerson.getMotherID().equals("")) {
            ancestorDivision(currentPerson.getMotherID(),ancestor,people);
        }
    }

    // same algorithm with the code in search activity. cannot access search activity from test class.

    public void eventSearch(String query, List<Event> searchEventList) {
        List<Event> eventList = new ArrayList<>(dataCache.getFilteredEvents().values());

        for(int i = 0; i < eventList.size(); i++) {
            String type = eventList.get(i).getEventType();
            String city = eventList.get(i).getCity();
            String country = eventList.get(i).getCountry();
            String year = String.valueOf(eventList.get(i).getYear());

            Person relatedPerson = dataCache.getPeople().get(eventList.get(i).getPersonID());
            String associatedPerson = relatedPerson.getFirstName() + " " + relatedPerson.getLastName();
            if(type.toLowerCase().contains(query.toLowerCase()) ||
                    city.toLowerCase().contains(query.toLowerCase()) ||
                    country.toLowerCase().contains(query.toLowerCase()) ||
                    associatedPerson.toLowerCase().contains(query.toLowerCase()) ||
                    year.toLowerCase().contains(query.toLowerCase())) {

                searchEventList.add(eventList.get(i));
            }
        }
    }

    public void personSearch(String query, List<Person> searchPersonList) {
        List<Person> personList = new ArrayList<>(dataCache.getPeople().values());
        for(int i = 0; i < personList.size(); i++) {
            String firstName = personList.get(i).getFirstName();
            String lastName = personList.get(i).getLastName();

            if(firstName.toLowerCase().contains(query.toLowerCase()) ||
                    lastName.toLowerCase().contains(query.toLowerCase())) {
                searchPersonList.add(personList.get(i));
            }
        }
    }



}
