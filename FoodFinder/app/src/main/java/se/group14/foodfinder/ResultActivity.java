package se.group14.foodfinder;
/**
 * Created by Alexander J. Drottsgård on 2017-03-31.
 */

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    private ArrayList<Restaurant> restaurants;
    private ListView resultView;
    private String[] nameArray;
    private String[] distanceArray;
    private ArrayAdapter<String> nameAdapter;
    private ArrayAdapter<String> distanceAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


    //Lägg in restauranger i listView
    public void showInfo() {
        setContentView(R.layout.activity_result);
        resultView = (ListView) findViewById(R.id.resultView);
        nameAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, nameArray);
        //distanceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, distanceArray);

        resultView.setOnItemClickListener(new ListListener());
        resultView.setAdapter(nameAdapter);
        //resultView.setAdapter(distanceAdapter);
        nameAdapter.notifyDataSetChanged();
        //distanceAdapter.notifyDataSetChanged();

        for(int i = 0; i < restaurants.size(); i++){


        }
    }

    private class ListListener implements AdapterView.OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(ResultActivity.this, InformationActivity.class);

        }
    }

}
