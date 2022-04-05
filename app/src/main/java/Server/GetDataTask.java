package Server;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import Data.DataCache;
import model.Event;
import model.Person;
import request.AllEventRequest;
import request.AllPersonRequest;
import result.AllEventResult;
import result.AllPersonResult;

public class GetDataTask implements Runnable{

    private final Handler messageHandler;
    private String serverPort;
    private String serverHost;
    private DataCache dataCache = DataCache.getInstance();


    public GetDataTask(Handler messageHandler, String serverHost, String serverPort) {
        this.messageHandler = messageHandler;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        ServerProxy serverProxy = new ServerProxy(serverHost, serverPort);

        AllPersonRequest allPersonRequest = new AllPersonRequest(dataCache.getAuthtoken());
        AllPersonResult allPersonResult;

        AllEventRequest allEventRequest = new AllEventRequest(dataCache.getAuthtoken());
        AllEventResult allEventResult;

        try{
            allPersonResult = serverProxy.getAllPeople(allPersonRequest);
            allEventResult = serverProxy.getAllEvents(allEventRequest);

            HashMap<String, Person> personHashMap = listToHashMapPerson(allPersonResult.getData());
            HashMap<String, Event> eventHashMap = listToHashMapEvent(allEventResult.getData());
            dataCache.setPeople(personHashMap);
            dataCache.setEvents(eventHashMap);

            dataCache.setPersonEvents(createPeopleEvents(allEventResult.getData()));

            Person user = personHashMap.get(dataCache.getPersonID());

            HashSet<String> maternalAncestor = new HashSet<String>();
            HashSet<String> paternalAncestor = new HashSet<String>();


            ancestorDivision(user.getFatherID(),paternalAncestor,personHashMap);
            ancestorDivision(user.getMotherID(),maternalAncestor,personHashMap);

            dataCache.setPaternalAncestors(paternalAncestor);
            dataCache.setMaternalAncestors(maternalAncestor);

            dataCache.sortEventLists();

            sendMessage(true);

        } catch (IOException e) {
            e.printStackTrace();

            sendMessage(false);
        }
    }

    private void sendMessage(Boolean success) {

        Message message = Message.obtain();
        Bundle messageBundle= new Bundle();
        messageBundle.putBoolean("SUCCESS_KEY",success);

        message.setData(messageBundle);
        messageHandler.sendMessage(message);
    }


    private HashMap<String, Person> listToHashMapPerson (LinkedList<Person> list) {

        HashMap<String, Person> personHashMap = new HashMap<String,Person>();

        for(Person elem: list) {
            personHashMap.put(elem.getPersonID(),elem);
        }
        return personHashMap;
    }


    private HashMap<String, Event> listToHashMapEvent (LinkedList<Event> list) {

        HashMap<String, Event> eventHashMap = new HashMap<String,Event>();

        for(Event elem: list) {
            eventHashMap.put(elem.getEventID(),elem);
        }
        return eventHashMap;
    }

    private HashMap<String, List<Event>> createPeopleEvents (LinkedList<Event> list)  {

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
