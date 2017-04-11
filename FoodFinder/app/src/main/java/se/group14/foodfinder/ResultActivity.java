package se.group14.foodfinder;


import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Alexander J. Drottsgård on 2017-03-31.
 * @author Filip Heidfors, Alexander J. Drottsgård
 * Klassen är UI och ska visa sökresultatet av restauranger i en ListView och på en karta
 */
public class ResultActivity extends AppCompatActivity {
    private ArrayList<Restaurant> restaurants;
    private ListView resultView;
    private String[] nameArray;
    private String[] distanceArray;
    private ArrayAdapter<String> nameAdapter;
    private ArrayAdapter<String> distanceAdapter;

    /**
     * Metoden som startar activityn
     * @param savedInstanceState
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        //restaurants = intent.getParcelableArrayExtra(MainActivity.EXTRA);
        restaurants = (ArrayList<Restaurant>) intent.getSerializableExtra("arrayList");

        System.out.println("ONCREATE I RESULTACTIVITY!!!!!!");
        System.out.println(restaurants.size());
        nameArray = new String[restaurants.size()];
        distanceArray = new String[restaurants.size()];
        for(int i = 0; i < restaurants.size(); i++){

            System.out.println("I RESULTACTIVITY BIATXHZ");
            System.out.println("\nRestarang " + i + ":\n");
            System.out.println("\nNamn: "+restaurants.get(i).getName()+"\n");
            System.out.println("\nAdress: "+restaurants.get(i).getAddress()+"\n");
            System.out.println("\nAvstånd: "+restaurants.get(i).getDistance()+"\n");
            System.out.println("\nTelefonnummer: "+restaurants.get(i).getPhone()+"\n");

            nameArray[i] = restaurants.get(i).getName();
            distanceArray[i] = ""+restaurants.get(i).getDistance();
        }

        showInfo();
    }


    //Metod som ska sköta insättningen av data till ListViewn
    public void showInfo() {
        setContentView(R.layout.activity_result);
        resultView = (ListView) findViewById(R.id.resultView);
        nameAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, nameArray);
//        distanceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, distanceArray);


        RestaurantListAdapter adapter = new RestaurantListAdapter(getApplicationContext());
        resultView.setAdapter(adapter);


        resultView.setOnItemClickListener(new ListListener());


//        resultView.setAdapter(nameAdapter);
        //resultView.setAdapter(distanceAdapter);
//        nameAdapter.notifyDataSetChanged();
        //distanceAdapter.notifyDataSetChanged();

        for(int i = 0; i < restaurants.size(); i++){


        }

    }

    /**
     * Inre klass som implementerar Adapterview. Hanterar klick på ListViewn.
     */
    private class ListListener implements AdapterView.OnItemClickListener {

        /**
         * Metod som hanterar klick på en rad i ListViewn
         * @param parent
         * @param view
         * @param position Positionen på raden som klickades på
         * @param id Id för raden som klickades på
         */
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(ResultActivity.this, InformationActivity.class);
            intent.putExtra("restaurant", restaurants.get(position));
            startActivity(intent);
        }
    }

    /**
     * Inre klass som extendar BaseAdapter. Hanterar resultView så den kan visa mer information.
     */
    private class RestaurantListAdapter extends BaseAdapter {

        private Context mContext;

        public RestaurantListAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return restaurants.size();
        }

        @Override
        public Object getItem(int position) {
            return restaurants.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mContext, R.layout.restaurant_list, null);
            TextView restaurantName = (TextView) view.findViewById(R.id.restaurantName);
            TextView distance = (TextView) view.findViewById(R.id.distance);
            TextView rating = (TextView) view.findViewById(R.id.rating);

            restaurantName.setText(restaurants.get(position).getName());
            distance.setText(restaurants.get(position).getDistance() + " meter");
            rating.setText(String.valueOf(restaurants.get(position).getRating() + "/10"));

            view.setTag(restaurants.get(position).getId());

            return view;
        }
    }

}
