package request;

import model.Event;
import model.Person;
import model.User;

/***
 * Loads the user, person, and event data.
 * Used as a LoadRequest.
 */
public class LoadRequest {

    User[] users;
    Person[] persons;
    Event[] events;


    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }


}
