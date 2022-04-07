package UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mainactivity.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Data.DataCache;
import model.Event;
import model.Person;

public class MapFragment extends Fragment {
    DataCache dataCache = DataCache.getInstance();
    private TextView dynamicTextView;
    private ImageView dynamicImageView;
    private Event selectedEvent;
    private List<Polyline> oldLines = new ArrayList<>();
    private GoogleMap gMap;
    HashMap<String, Float> colorsData = new HashMap<String, Float>();

    // Event Activity helper variables
    private Boolean IsEventActivity = false;
    private Event eventActivity;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            setMarkers(googleMap);
            gMap = googleMap;

          /**  LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
           **/

          if(IsEventActivity == true) {
              selectedEvent = eventActivity;

              // move Camera
              LatLng selectedEventLocation = new LatLng(selectedEvent.getLatitude(),selectedEvent.getLongitude());
              googleMap.moveCamera(CameraUpdateFactory.newLatLng(selectedEventLocation));


              changeDynamicView( selectedEvent);
              clearOldLines();
              if(dataCache.isFamilyTreeLinesOn()) {
                  familyLine(selectedEvent.getPersonID(), selectedEvent, 15, Color.GREEN, googleMap);
              }
              if (dataCache.isLifeStoryLinesOn()) {
                  lifeStoryLine(selectedEvent.getPersonID(), 15, Color.YELLOW, googleMap);
              }
              if (dataCache.isSpousesLinesOn()) {
                  spouseLine(selectedEvent.getPersonID(),selectedEvent,15,Color.RED,googleMap);
              }
          }


          googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

              @Override
              public boolean onMarkerClick(@NonNull Marker marker) {

                  selectedEvent = (Event) marker.getTag();
                  changeDynamicView( selectedEvent);
                  clearOldLines();
                  if(dataCache.isFamilyTreeLinesOn()) {
                      familyLine(selectedEvent.getPersonID(), selectedEvent, 15, Color.GREEN, googleMap);
                  }
                  if (dataCache.isLifeStoryLinesOn()) {
                      lifeStoryLine(selectedEvent.getPersonID(), 15, Color.YELLOW, googleMap);
                  }
                  if (dataCache.isSpousesLinesOn()) {
                      spouseLine(selectedEvent.getPersonID(),selectedEvent,15,Color.RED,googleMap);
                  }
                  return false;
              }
          });
        }


    };

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Iconify.with(new FontAwesomeModule());
        dataCache.filterEvents();
    }


    /**
    public class DynamicLayout extends Activity {
        private FrameLayout dynamicLayout;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ViewGroup viewGroup = new ViewGroup() {
                @Override
                protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

                }
            }
            dynamicLayout = dynamicLayout.findViewById(R.id.androidMapFrame);


        }
    }
**/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Event Activity (Not sure if it works, Not really sure if the map ready perform first or not)
        Bundle arguments = getArguments();
        if(arguments != null && arguments.containsKey("EVENT_KEY")) {
            String eventActivityId = getArguments().getString("EVENT_KEY");
            IsEventActivity = true;
            eventActivity = dataCache.getEvents().get(eventActivityId);
        }



        View view = inflater.inflate(R.layout.fragment_map,container,false);

        dynamicTextView = (TextView) view.findViewById(R.id.dynamicTextView);
        dynamicImageView = (ImageView) view.findViewById(R.id.dynamicImageView);

        dynamicImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 personActivityTransition(selectedEvent.getPersonID());
            }
        });
        dynamicTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                personActivityTransition(selectedEvent.getPersonID());
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (gMap != null) {
            gMap.clear();
            setMarkers(gMap);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    public void setMarkers(GoogleMap googleMap) {
        int colorIndex = 0;

        float colors[] = {
                BitmapDescriptorFactory.HUE_RED,
                BitmapDescriptorFactory.HUE_ORANGE,
                BitmapDescriptorFactory.HUE_YELLOW,
                BitmapDescriptorFactory.HUE_GREEN,
                BitmapDescriptorFactory.HUE_CYAN,
                BitmapDescriptorFactory.HUE_AZURE,
                BitmapDescriptorFactory.HUE_BLUE,
                BitmapDescriptorFactory.HUE_VIOLET,
                BitmapDescriptorFactory.HUE_MAGENTA,
                BitmapDescriptorFactory.HUE_ROSE
        };

        for (Event elem: dataCache.getFilteredEvents().values()) {
            if(!colorsData.containsKey(elem.getEventType().toLowerCase())) {
                colorsData.put(elem.getEventType().toLowerCase(),colors[colorIndex%10]);
                colorIndex++;
            }
            Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(elem.getLatitude(), elem.getLongitude())).
                    icon(BitmapDescriptorFactory.defaultMarker(colorsData.get(elem.getEventType().toLowerCase()))));
            marker.setTag(elem);
        }

    }

    private void changeDynamicView( Event event) {

        Person person = dataCache.getPeople().get(event.getPersonID());

        dynamicTextView.setText(person.getFirstName() + " " + person.getLastName() + "\n" +
                event.getEventType().toUpperCase() + ": " + event.getCity() +", " + event.getCountry());

        if(person.getGender().equals("m")) {

            Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(R.color.purple_700).sizeDp(40);
            dynamicImageView.setImageDrawable(genderIcon);
            // 이미지 추가하기
        }
        else {
            Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).color(Color.RED).sizeDp(40);
            dynamicImageView.setImageDrawable(genderIcon);
        }

    }

    private void personActivityTransition(String personID) {

        Intent intent = new Intent(getActivity(), PersonActivity.class);
        intent.putExtra("selectedPerson",personID);
        startActivity(intent);
    }
    private void clearOldLines() {
        for(Polyline elem: oldLines){
            elem.remove();
        }
    }

    private void spouseLine(String personID, Event startEvent, float width, int color, GoogleMap googleMap) {

        Person person = dataCache.getPeople().get(personID);
        if(!person.getSpouseID().equals("") && dataCache.getFilteredPeople().containsKey(person.getSpouseID())) {
            LatLng startPoint = new LatLng(startEvent.getLatitude(),startEvent.getLongitude());
            Event endEvent = earliestEvent(person.getSpouseID());
            LatLng endPoint = new LatLng(endEvent.getLatitude(),endEvent.getLongitude());

            PolylineOptions options = new PolylineOptions().add(startPoint).add(endPoint).color(color).width(width);

            oldLines.add(googleMap.addPolyline(options));
        }

    }

    private void lifeStoryLine(String personID, float width, int color, GoogleMap googleMap) {

        //lifeStoryList is alreay sorted in the GetDataTask
        List<Event> lifeStoryList = dataCache.getPersonEvents().get(personID);
        Event startEvent = lifeStoryList.get(0);

        for (int i = 1; i < lifeStoryList.size(); i++) {
            LatLng startPoint = new LatLng(startEvent.getLatitude(),startEvent.getLongitude());
            Event endEvent = lifeStoryList.get(i);
            LatLng endPoint = new LatLng(endEvent.getLatitude(),endEvent.getLongitude());
            PolylineOptions options = new PolylineOptions().add(startPoint).add(endPoint).color(color).width(width);
            oldLines.add(googleMap.addPolyline(options));

            startEvent = endEvent;
        }

    }

    private void familyLine(String personId, Event startEvent, float width, int color, GoogleMap googleMap) {
        Person person = dataCache.getPeople().get(personId);


        if(!person.getFatherID().equals("") && dataCache.getFilteredPeople().containsKey(person.getFatherID())) {
            LatLng startPoint = new LatLng(startEvent.getLatitude(),startEvent.getLongitude());
            Event endEvent = earliestEvent(person.getFatherID());
            LatLng endPoint = new LatLng(endEvent.getLatitude(), endEvent.getLongitude());

            PolylineOptions options = new PolylineOptions().add(startPoint).add(endPoint).color(color).width(width);


            oldLines.add(googleMap.addPolyline(options));

            familyLine(person.getFatherID(),endEvent, (float) (width * 0.6), color, googleMap);
        }

        if(!person.getMotherID().equals("") && dataCache.getFilteredPeople().containsKey(person.getMotherID())) {

            LatLng startPoint = new LatLng(startEvent.getLatitude(),startEvent.getLongitude());
            Event endEvent = earliestEvent(person.getMotherID());
            LatLng endPoint = new LatLng(endEvent.getLatitude(), endEvent.getLongitude());

            PolylineOptions options = new PolylineOptions().add(startPoint).add(endPoint).color(color).width(width);

            oldLines.add(googleMap.addPolyline(options));

            familyLine(person.getMotherID(),endEvent, (float) (width * 0.6), color, googleMap);
        }

    }

    private Event earliestEvent(String personId) {

        List<Event> events = dataCache.getPersonEvents().get(personId);
        int minimumYear = events.get(0).getYear();
        Event birthEvent = null;
        Event resultEvent = events.get(0);
        for(int i = 1; i < events.size(); i++) {
            if(events.get(i).getYear() < minimumYear) {
                resultEvent = events.get(i);
                minimumYear = resultEvent.getYear();
            }
            if(events.get(i).getEventType().toLowerCase().equals("birth")) {
                birthEvent = events.get(i);
            }
        }

        if(birthEvent != null) {
            return birthEvent;
        }
        else {
            return resultEvent;
        }

    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        if(IsEventActivity == false) {
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.main_menu, menu);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchMenuItem:
                Intent intent1 = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent1);
                return true;
            case R.id.settingMenuItem:
                Intent intent2 = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

}