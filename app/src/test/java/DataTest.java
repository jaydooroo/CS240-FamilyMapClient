import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Data.DataCache;
import Server.GetDataTask;
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
}
