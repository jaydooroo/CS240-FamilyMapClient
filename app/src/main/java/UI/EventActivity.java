package UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.mainactivity.R;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Iconify.with(new FontAwesomeModule());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent intent = getIntent();
        String eventId= intent.getStringExtra("selectedEvent");

        Bundle eventData = new Bundle();
        eventData.putString("EVENT_KEY",eventId);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.EventFrameLayout);

        if(fragment == null) {
            MapFragment mapFragment = new MapFragment();
            mapFragment.setArguments(eventData);
            fragmentManager.beginTransaction().add(R.id.EventFrameLayout,mapFragment).commit();
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