package UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.mainactivity.R;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import Data.DataCache;


public class MainActivity extends AppCompatActivity implements LoginFragment.Listener{

private DataCache dataCache = DataCache.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Iconify.with(new FontAwesomeModule());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentFrameLayout);
        if(fragment == null) {
            fragment = createLoginFragment();

            fragmentManager.beginTransaction().add(R.id.fragmentFrameLayout,fragment).commit();
        }
        else {
            if(fragment instanceof LoginFragment) {
                ((LoginFragment) fragment).registerListener(this);
            }
        }
    }

    @Override
    public void onResume() {

        super.onResume();

        Intent intent = getIntent();
        if(dataCache.getAuthtoken() == null) {
            this.getFragmentManager().popBackStack();
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            Fragment fragment = createLoginFragment();
                fragmentManager.beginTransaction().replace(R.id.fragmentFrameLayout,fragment).commit();
        }
    }

    private Fragment createLoginFragment() {
        LoginFragment fragment = new LoginFragment();
        fragment.registerListener (this);
        return fragment;
    }

    @Override
    public void notifyLogin() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = new MapFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fragmentFrameLayout, fragment)
                .commit();
    }

}