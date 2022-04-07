package Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Person;
import model.Event;
public class DataCache {

    private static DataCache instance = new DataCache();
    public static DataCache getInstance() {
        return instance;
    }

    private DataCache() {
        filteredEvents = new HashMap<String, Event>();
        filteredPeople = new HashMap<String, Person>();
    }

    private Map<String, Person> people;
    private Map<String, Event> events;
    private Map<String, List<Event>> personEvents ;
    private HashSet<String> paternalAncestors;
    private HashSet<String> maternalAncestors;

    //User Information

    private String username;
    private String personID;
    private String authtoken;

    //for testing
    private List<Family> familyList;

    //settings
    private boolean isLifeStoryLinesOn = true;
    private boolean isFamilyTreeLinesOn = true;
    private boolean isSpousesLinesOn = true;
    private boolean isFatherSideOn = true;
    private boolean isMotherSideOn = true;
    private boolean isMaleEventsOn = true;
    private boolean isFemaleEventsOn = true;


    private Map<String, Event> filteredEvents;
    private Map<String, Person> filteredPeople;

    public Map<String, Person> getFilteredPeople() {
        return filteredPeople;
    }

    public void setFilteredPeople(Map<String, Person> filteredPeople) {
        this.filteredPeople = filteredPeople;
    }


    public Map<String, Event> getFilteredEvents() {
        return filteredEvents;
    }

    public void setFilteredEvents(Map<String, Event> filteredEvents) {
        this.filteredEvents = filteredEvents;
    }



    public boolean isLifeStoryLinesOn() {
        return isLifeStoryLinesOn;
    }

    public void setLifeStoryLinesOn(boolean lifeStoryLinesOn) {
        isLifeStoryLinesOn = lifeStoryLinesOn;
    }

    public boolean isFamilyTreeLinesOn() {
        return isFamilyTreeLinesOn;
    }

    public void setFamilyTreeLinesOn(boolean familyTreeLinesOn) {
        isFamilyTreeLinesOn = familyTreeLinesOn;
    }

    public boolean isSpousesLinesOn() {
        return isSpousesLinesOn;
    }

    public void setSpousesLinesOn(boolean spousesLinesOn) {
        isSpousesLinesOn = spousesLinesOn;
    }

    public boolean isFatherSideOn() {
        return isFatherSideOn;
    }

    public void setFatherSideOn(boolean fatherSideOn) {
        isFatherSideOn = fatherSideOn;
    }

    public boolean isMotherSideOn() {
        return isMotherSideOn;
    }

    public void setMotherSideOn(boolean motherSideOn) {
        isMotherSideOn = motherSideOn;
    }

    public boolean isMaleEventsOn() {
        return isMaleEventsOn;
    }

    public void setMaleEventsOn(boolean maleEventsOn) {
        isMaleEventsOn = maleEventsOn;
    }

    public boolean isFemaleEventsOn() {
        return isFemaleEventsOn;
    }

    public void setFemaleEventsOn(boolean femaleEventsOn) {
        isFemaleEventsOn = femaleEventsOn;
    }



    public static void setInstance(DataCache instance) {
        DataCache.instance = instance;
    }

    public void clear() {
        this.people = null;
        this.events = null;
        this.personEvents = null;
        this.paternalAncestors = null;
        this.maternalAncestors = null;

        this.username = null;
        this.personID = null;
        this.authtoken = null;
        this.filteredEvents= null;
        this.filteredPeople = null;

        isLifeStoryLinesOn = true;
        isFamilyTreeLinesOn = true;
        isSpousesLinesOn = true;
        isFatherSideOn = true;
        isMotherSideOn = true;
        isMaleEventsOn = true;
        isFemaleEventsOn = true;
    }

    public Map<String, Person> getPeople() {
        return people;
    }

    public void setPeople(Map<String, Person> people) {
        this.people = people;
    }

    public Map<String, Event> getEvents() {
        return events;
    }

    public void setEvents(Map<String, Event> events) {
        this.events = events;
    }

    public Map<String, List<Event>> getPersonEvents() {
        return personEvents;
    }

    public void setPersonEvents(Map<String, List<Event>> personEvents) {
        this.personEvents = personEvents;
    }

    public HashSet<String> getPaternalAncestors() {
        return paternalAncestors;
    }

    public void setPaternalAncestors(HashSet<String> paternalAncestors) {
        this.paternalAncestors = paternalAncestors;
    }

    public HashSet<String> getMaternalAncestors() {
        return maternalAncestors;
    }

    public void setMaternalAncestors(HashSet<String> maternalAncestors) {
        this.maternalAncestors = maternalAncestors;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }


    public void sortEventLists () {

        for(List<Event> elem: personEvents.values()) {
            Collections.sort(elem, new ListComparator());
        }
    }

    private class  ListComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {


            int year1 = ((Event)o1).getYear();
            int year2 = ((Event)o2).getYear();

            String eventType1 = ((Event) o1).getEventType();
            String eventType2 = ((Event) o2).getEventType();

            if(eventType1.toLowerCase().equals("birth")) {
                return -1;
            }
            else if (eventType2.toLowerCase().equals("birth")) {
                return 1;
            }

            if(eventType1.toLowerCase().equals("death")) {
                    return 1;
            }
            else if(eventType2.toLowerCase().equals("death")){
                    return -1;
            }


            if(year1 > year2){
                return 1;
            }
            else if(year1 < year2) {
                return -1;
            }else {
                // If it is not sorted by years sorted by string.
               return eventType1.compareTo(eventType2);
            }
        }
    }

    public void filterEvents () {
        filteredEvents = new HashMap<String, Event>();
        filteredPeople = new HashMap<String, Person>();

        //initial setting for user.
        filteredPeople.put(personID,people.get(personID));
        for(Event elem: personEvents.get(personID)) {
            filteredEvents.put(elem.getEventID(),elem);
        }

        // spouse setting

        String spouseID = people.get(personID).getSpouseID();
        if(!spouseID.equals("")) {
            filteredPeople.put(spouseID, people.get(spouseID));
            for (Event elem : personEvents.get(spouseID)) {
                filteredEvents.put(elem.getEventID(), elem);
            }
        }


        //father side filter
        if(isFatherSideOn) {
            for(String elem:paternalAncestors) {
                filteredPeople.put(elem, people.get(elem));
                for(Event eventElem: personEvents.get(elem)) {
                    filteredEvents.put(eventElem.getEventID(),eventElem);
                }
            }
        }
        if(isMotherSideOn) {
            for(String elem:maternalAncestors) {
                filteredPeople.put(elem, people.get(elem));
                for(Event eventElem: personEvents.get(elem)) {
                    filteredEvents.put(eventElem.getEventID(),eventElem);
                }
            }
        }
        if(!isMaleEventsOn()) {
            List<String> peopleId = new ArrayList<String>();
            List<String> eventsId = new ArrayList<String>();

            for(Person elemPerson: filteredPeople.values()) {
                if(elemPerson.getGender().equals("m")) {
                    peopleId.add(elemPerson.getPersonID());
                    for(Event elem: personEvents.get(elemPerson.getPersonID())) {
                        eventsId.add(elem.getEventID());
                    }
                }
            }

            for( String elem: peopleId) {
                filteredPeople.remove(elem);
            }

            for(String elem: eventsId) {
                filteredEvents.remove(elem);
            }
        }
        if(!isFemaleEventsOn) {
            List<String> peopleId = new ArrayList<String>();
            List<String> eventsId = new ArrayList<String>();

            for(Person elemPerson: filteredPeople.values()) {
                if(elemPerson.getGender().equals("f")) {
                    peopleId.add(elemPerson.getPersonID());
                    for(Event elem: personEvents.get(elemPerson.getPersonID())) {
                        eventsId.add(elem.getEventID());
                    }
                }
            }

            for( String elem: peopleId) {
                filteredPeople.remove(elem);
            }

            for(String elem: eventsId) {
                filteredEvents.remove(elem);
            }
        }

    }

}
