package se.group14.foodfinder;

//import android.app.Fragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by filipheidfors on 2017-04-17.
 */

public class Tab1List extends Fragment {
    private Activity activity;
    private ArrayList<Restaurant> restaurants;
    private ListView resultView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1listview, container, false);
        resultView = (ListView) rootView.findViewById(R.id.resultView);
        showInfo();
        return rootView;
    }


    public void setArguments(ArrayList<Restaurant> restaurants, Activity a) {
        this.restaurants = restaurants;
        activity = a;
    }

    //Metod som ska sköta insättningen av data till ListViewn
    public void showInfo() {
        //setContentView(R.layout.activity_result);
        //nameAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, nameArray);
//        distanceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, distanceArray);

        RestaurantListAdapter adapter = new RestaurantListAdapter(activity.getApplicationContext());
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
            View view = View.inflate(mContext, R.layout.restaurant_list_data, null);
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
            Intent intent = new Intent(activity, InformationActivity.class);
            intent.putExtra("restaurant", restaurants.get(position));
            startActivity(intent);
        }
    }


}
