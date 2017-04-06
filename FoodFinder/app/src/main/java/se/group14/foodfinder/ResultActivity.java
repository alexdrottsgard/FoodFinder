package se.group14.foodfinder;
/**
 * Created by Alexander J. Drottsgård on 2017-03-31.
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    private ArrayList<Restaurant> restaurants;
    private ListView resultView;
    private String[] restuarantsArray;

    /*
    public ResultActivity(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
        showInfo();
    }

    //Lägg in restauranger i listView
    public void showInfo() {
        setContentView(R.layout.activity_result);
        resultView = (ListView) findViewById(R.id.resultView);
        ArrayAdapter<Restaurant> arrayAdapter =
                new ArrayAdapter<Restaurant>(this,android.R.layout.simple_list_item_1, restaurants);

        resultView.setAdapter(arrayAdapter);


        for(int i = 0; i < restaurants.size(); i++){

            /*
            System.out.println("\nRestarang " + i + ":\n");
            System.out.println("\nNamn: "+restaurants.get(i).getName()+"\n");
            System.out.println("\nAdress: "+restaurants.get(i).getAddress()+"\n");
            System.out.println("\nAvstånd: "+restaurants.get(i).getDistance()+"\n");

//            System.out.println("\nTelefonnummer: "+restaurants.get(i).getPhone()+"\n");
        }
    }
    */

}
