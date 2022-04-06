package UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TableRow;

import com.example.mainactivity.R;

import Data.DataCache;

public class SettingsActivity extends AppCompatActivity {

    private DataCache dataCache = DataCache.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        //Logout
        TableRow logoutRow = findViewById(R.id.logoutText);
        logoutRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                dataCache.setAuthtoken(null);
                startActivity(intent);
            }
        });

        //LifeStoryLinesSwitch
        Switch lifeStorySwitch = findViewById(R.id.lifeStorySwitch);
        lifeStorySwitch.setChecked(dataCache.isLifeStoryLinesOn());

        lifeStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    dataCache.setLifeStoryLinesOn(true);
                }
                else {
                    dataCache.setLifeStoryLinesOn(false);
                }

            }
        });

        Switch familyTreeSwitch = findViewById(R.id.familyTreeSwitch);
        familyTreeSwitch.setChecked(dataCache.isFamilyTreeLinesOn());

        familyTreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                dataCache.setFamilyTreeLinesOn(isChecked);
            }
        });

        Switch spouseTreeSwitch = findViewById(R.id.spouseLinesSwitch);
        spouseTreeSwitch.setChecked(dataCache.isSpousesLinesOn());

        spouseTreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                dataCache.setSpousesLinesOn(isChecked);
            }
        });

        // Events Switches

        Switch fatherSideSwitch = findViewById(R.id.fathersSideSwitch);
        fatherSideSwitch.setChecked(dataCache.isFatherSideOn());

        fatherSideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                dataCache.setFatherSideOn(isChecked);
                dataCache.filterEvents();
            }
        });

        Switch motherSideSwitch = findViewById(R.id.mothersSideSwitch);
        motherSideSwitch.setChecked(dataCache.isMotherSideOn());

        motherSideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                dataCache.setMotherSideOn(isChecked);
                dataCache.filterEvents();
            }
        });

        Switch maleEventsSwitch = findViewById(R.id.maleEventsSwitch);
        maleEventsSwitch.setChecked(dataCache.isMaleEventsOn());

        maleEventsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                dataCache.setMaleEventsOn(isChecked);
                dataCache.filterEvents();
            }
        });

        Switch femaleEventsSwitch = findViewById(R.id.femaleEventsSwitch);
        femaleEventsSwitch.setChecked(dataCache.isFemaleEventsOn());

        femaleEventsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheked) {
                dataCache.setFemaleEventsOn(isCheked);
                dataCache.filterEvents();
            }
        });

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