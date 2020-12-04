package com.example.buildathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import com.example.buildathon.Fragments.DiscussFragment;
import com.example.buildathon.Fragments.HomeFragment;
import com.example.buildathon.Fragments.ProfileFragment;
import com.example.buildathon.Fragments.SearchFragment;
import com.example.buildathon.Utils.LoadingBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    LoadingBar loadingBar;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingBar = new LoadingBar(MainActivity.this);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        if(savedInstanceState==null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new HomeFragment()).addToBackStack("home").commit();

        }

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_home:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new HomeFragment()).addToBackStack("home").commit();
                                break;
                            case R.id.menu_search:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new SearchFragment()).addToBackStack("search").commit();
                                break;
                            case R.id.menu_dash:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new ProfileFragment()).addToBackStack("profile").commit();
                                break;
                            case R.id.menu_discuss:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new DiscussFragment()).addToBackStack("discuss").commit();
                                break;
                        }
                        return true;
                    }
                });
    }

    public void loadFragment(Fragment fm, Bundle args){

        fm.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_fragment, fm)
                .addToBackStack(null).commit();
        HomeFragment home = new HomeFragment();
        Log.e("frag_pos", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));

    }

    @Override
    public void onBackPressed() {

        if (bottomNavigationView.getSelectedItemId()!=R.id.menu_home)
        {
            bottomNavigationView.setSelectedItemId(R.id.menu_home);
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new HomeFragment()).addToBackStack("home").commit();

        }
        super.onBackPressed();
    }
}