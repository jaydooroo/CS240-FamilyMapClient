package UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.mainactivity.R;

import java.util.ArrayList;
import java.util.List;

import Data.DataCache;
import model.Event;
import model.Person;

public class SearchActivity extends AppCompatActivity {

    private DataCache dataCache = DataCache.getInstance();

    private static final int EVENT_ITEM_VIEW_TYPE = 0;
    private static final int PERSON_ITEM_VIEW_TYPE = 1;
    List<Event> eventList = new ArrayList<>(dataCache.getFilteredEvents().values());
    List<Person> personList = new ArrayList<>(dataCache.getPeople().values());

    List<Event> searchEventList = new ArrayList<Event>();
    List<Person> searchPersonList = new ArrayList<Person>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchView searchView = findViewById(R.id.searchView);

        RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        SearchContentsAdapter adapter = new SearchContentsAdapter(searchEventList, searchPersonList);
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchEventList.clear();
                searchPersonList.clear();

                eventSearch(query,searchEventList);
                personSearch(query,searchPersonList);

                SearchContentsAdapter adapter = new SearchContentsAdapter(searchEventList,searchPersonList);
                recyclerView.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });


    }

    private void eventSearch(String query, List<Event> searchEventList) {

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

    private void personSearch(String query, List<Person> searchPersonList) {
        for(int i = 0; i < personList.size(); i++) {
            String firstName = personList.get(i).getFirstName();
            String lastName = personList.get(i).getLastName();

            if(firstName.toLowerCase().contains(query.toLowerCase()) ||
            lastName.toLowerCase().contains(query.toLowerCase())) {
                searchPersonList.add(personList.get(i));
            }
        }
    }

    private class SearchContentsAdapter extends RecyclerView.Adapter<SearchContentsViewHolder>{

        private final List<Event> eventList;
        private final List<Person> personList;

        SearchContentsAdapter(List<Event> eventList, List<Person> personList) {
            this.eventList = eventList;
            this.personList = personList;
        }

        @Override
        public int getItemViewType(int position) {
            return position < personList.size() ? PERSON_ITEM_VIEW_TYPE : EVENT_ITEM_VIEW_TYPE;
        }

        @NonNull
        @Override
        public SearchContentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            if (viewType == PERSON_ITEM_VIEW_TYPE) {
                view = getLayoutInflater().inflate(R.layout.family_list_view, parent, false);
            }
            else {
                view = getLayoutInflater().inflate(R.layout.life_event_view, parent, false);
            }
            return new SearchContentsViewHolder(view, viewType) ;
        }

        @Override
        public void onBindViewHolder(@NonNull SearchContentsViewHolder holder, int position) {
            if(position < personList.size()) {
                holder.bind(personList.get(position));
            }
            else {
                holder.bind(eventList.get(position - personList.size()));
                System.out.println(position - personList.size());
            }

        }

        @Override
        public int getItemCount() {
            return personList.size() + eventList.size();
        }
    }

    private class SearchContentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView personGenderImg;
        private final TextView personName;

        private TextView eventType ;
        private TextView eventPersonName;

        private Person person;
        private Event event;



        private final int viewType;

         SearchContentsViewHolder( View itemView, int viewType) {


            super(itemView);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if(viewType == PERSON_ITEM_VIEW_TYPE) {
                this.personGenderImg = itemView.findViewById(R.id.familyGenderImage);
                this.personName = itemView.findViewById(R.id.memberName);
                this.eventType = null;
                this.eventPersonName = null;
            }
            else {
                this.eventType = itemView.findViewById(R.id.eventType);
                this.eventPersonName = itemView.findViewById(R.id.eventPersonNameView);
                this.personGenderImg = null;
                this.personName = null;
            }

        }

        @Override
        public void onClick(View view) {

             if(viewType == PERSON_ITEM_VIEW_TYPE) {
                 personActivityTransition(person.getPersonID());
             }
             else {
                 eventActivityTransition(event.getEventID());
             }

        }

        private void bind(Person person) {
             this.person = person;
             if(person.getGender().equals("f")) {
                 personGenderImg.setImageResource(R.drawable.female);
             }
             else {
                 personGenderImg.setImageResource(R.drawable.male);
             }
             personName.setText(person.getFirstName() + " " + person.getLastName());
        }

        private void bind(Event event) {

            this.event = event;
            eventType.setText(event.getEventType()+ ": " + event.getCity() + ", " + event.getCountry()
                    + "(" + event.getYear() + ")");
            Person eventPerson = dataCache.getPeople().get(event.getPersonID());
            eventPersonName.setText(eventPerson.getFirstName() + " " + eventPerson.getLastName());
        }
        private void personActivityTransition(String personID) {

            Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
            intent.putExtra("selectedPerson",personID);
            startActivity(intent);
        }
        private void eventActivityTransition(String eventID) {

            Intent intent = new Intent(SearchActivity.this, EventActivity.class);
            intent.putExtra("selectedEvent",eventID);
            startActivity(intent);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }
}