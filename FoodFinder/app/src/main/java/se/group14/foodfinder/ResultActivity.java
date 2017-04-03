package se.group14.foodfinder;
/**
 * Created by Alexander J. Drottsgård on 2017-03-31.
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    private ArrayList<Restaurant> restaurants;

    public ResultActivity(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
        showInfo();
    }

    //Lägg in restauranger i listView
    public void showInfo() {
        for(int i = 0; i < restaurants.size(); i++){
            System.out.println("\nRestarang " + i + ":\n");
            System.out.println("\nNamn: "+restaurants.get(i).getName()+"\n");
            System.out.println("\nAdress: "+restaurants.get(i).getAddress()+"\n");
            System.out.println("\nAvstånd: "+restaurants.get(i).getDistance()+"\n");
            System.out.println("\nTelefonnummer: "+restaurants.get(i).getPhone()+"\n");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }
}
