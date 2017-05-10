package se.group14.foodfinder;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Klassen hanterar två fragments som användaren kan bläddra mellan.
 * En lista av restauranger (ResultListView) och en karta på restauranger (ResultMapView)
 * @author Filip Heidfors, Elias Moltedo
 */
public class ResultActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private ArrayList<Restaurant> restaurants;
    private ResultListView list;
    private ResultMapView map;
    private LatLng userPosition;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Restauranger");
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        restaurants = (ArrayList<Restaurant>) intent.getSerializableExtra("arrayList");
        System.out.println("ANTAL RESTAURANGER!!!!::::::::" + restaurants.size());
        double lat = (double) intent.getDoubleExtra("lat",0);
        double lng = (double) intent.getDoubleExtra("lng", 0);
        userPosition = new LatLng(lat,lng);

        list = new ResultListView();
        list.setArguments(restaurants, this);

        map = new ResultMapView();
        map.setArguments(restaurants,userPosition,this);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_result_activity, menu);
        return true;
    }


    /**
     * Hanterar click/swipe för att skifta fragment
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return list;
                case 1:
                    return map;
                default:
                    return null;
            }
        }

        /**
         * Metoden ska returnera antalet fragments
         * @return antal fragments
         */
        public int getCount() {
            return 2;
        }

        /**
         * Metoden returnerar text för dom olika tabbarna
         * @param position Aktuell tab's index
         * @return Texten som står på tabben
         */
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Lista";
                case 1:
                    return "Karta";
            }
            return null;
        }
    }

}
