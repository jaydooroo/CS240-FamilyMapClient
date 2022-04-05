package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import Data.DataCache;
import Data.Family;
import model.Event;
import model.Person;

public class PersonActivity extends AppCompatActivity {
    private DataCache dataCache = DataCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);


        Intent intent = getIntent();
        String personId = intent.getStringExtra("selectedPerson");
        Person person = dataCache.getPeople().get(personId);

       TextView firstNameView  =  findViewById(R.id.FirstNameInfo);
       firstNameView.setText(person.getFirstName());

       TextView lastNameView = findViewById(R.id.LastNameInfo);
       lastNameView.setText(person.getLastName());

       TextView genderView = findViewById(R.id.GenderInfo);
       genderView.setText(person.getGender());

        List<Event> eventList = dataCache.getPersonEvents().get(personId);
        List<Family> familyList = findFamilyMembers(personId);

        ExpandableListView expandableListView = findViewById(R.id.expandableListView);
        expandableListView.setAdapter(new ExpandableAdapter(eventList, familyList));

    }

    private class ExpandableAdapter extends BaseExpandableListAdapter {


        private final int EVENT_LIST_GROUP_POSITION = 0;
        private final int FAMILY_LIST_GROUP_POSITION = 1;

        private List<Event> eventList;
        private List<Family> familyList;

        public ExpandableAdapter(List<Event> eventList, List<Family> familyList) {
            this.eventList = eventList;
            this.familyList = familyList;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case EVENT_LIST_GROUP_POSITION:
                    return eventList.size();
                case FAMILY_LIST_GROUP_POSITION:
                    return familyList.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case EVENT_LIST_GROUP_POSITION:
                    titleView.setText(R.string.EventList);
                    break;
                case FAMILY_LIST_GROUP_POSITION:
                    titleView.setText(R.string.FamilyList);
                    break;

                    default:
                       throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch(groupPosition) {
                case EVENT_LIST_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.life_event_view, parent, false);
                    initializeEventListView(itemView, childPosition);
                    break;
                case FAMILY_LIST_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.family_list_view, parent, false);
                    initializeFamilyListView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;

        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }


        private void initializeEventListView (View eventListView, final int childPosition) {

            Event event = eventList.get(childPosition);
            Person person = dataCache.getPeople().get(event.getPersonID());

            TextView lifeEventTypeView = eventListView.findViewById(R.id.eventType);
            lifeEventTypeView.setText(event.getEventType()+ ": " + event.getCity() + ", " + event.getCountry()
                    + "(" + event.getYear() + ")");

            TextView eventLocationView = eventListView.findViewById(R.id.eventLocationView);
            eventLocationView.setText(person.getLastName() + " " + person.getFirstName());

            ImageView eventImageView = eventListView.findViewById(R.id.eventImageView);

            eventImageView.setImageResource(R.drawable.location);

        }

        private void initializeFamilyListView (View familyListView, final int childPosition) {
            Family member =  familyList.get(childPosition);

            Person person = member.getPerson();
            String relationship = member.getRelationship();

            TextView memberNameView = familyListView.findViewById(R.id.memberName);
            memberNameView.setText(person.getFirstName() + " " + person.getLastName());

            TextView relationshipView = familyListView.findViewById(R.id.memberRelationship);
            relationshipView.setText(relationship);

            ImageView imageView = familyListView.findViewById(R.id.eventImageView);

            if(person.getGender().equals("f")) {
                imageView.setImageResource(R.drawable.female);
            }
            else {

                imageView.setImageResource(R.drawable.male);
            }
            familyListView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(PersonActivity.this, "Asdasd", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }



    private List<Family> findFamilyMembers(String personId) {

        Person person = dataCache.getPeople().get(personId);
        List<Family> familyMembers = new LinkedList<Family>();

        if(!person.getFatherID().equals("")) {
          Family family = new Family("Father",dataCache.getPeople().get(person.getFatherID()));
            familyMembers.add(family);
        }

        if(!person.getMotherID().equals("")){

            Family family = new Family("Mother",dataCache.getPeople().get(person.getMotherID()));
            familyMembers.add(family);
        }
        if(!person.getSpouseID().equals("")) {
            Family family = new Family("Spouse",dataCache.getPeople().get(person.getSpouseID()));
            familyMembers.add(family);
        }

        if(person.getGender().equals("f")) {
            for (Person elemPerson : dataCache.getPeople().values()) {
                if (elemPerson.getMotherID().equals(personId)) {
                    Family family = new Family("Child",elemPerson);
                    familyMembers.add(family);
                }
            }
        }
        else {
            for (Person elemPerson : dataCache.getPeople().values()) {
                if (elemPerson.getFatherID().equals(personId)) {

                    Family family = new Family("Child",elemPerson);
                    familyMembers.add(family);
                }
            }

        }

        return familyMembers;
    }


}