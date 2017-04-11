package se.group14.foodfinder;
/**
 * Created by Alexander J. Drottsgård on 2017-03-31.
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;


public class InformationActivity extends Activity {
    private Restaurant restaurant;
    private TextView txtName;
    private TextView txtAddress;
    private TextView txtWebsite;
    private TextView txtRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_information);

        Intent intent = getIntent();
        restaurant = (Restaurant)intent.getSerializableExtra("restaurant");

        txtName = (TextView) findViewById(R.id.restaurantName);
        txtName.setText(restaurant.getName());

        txtAddress = (TextView) findViewById(R.id.adressField);

        StringBuilder stringBuilder = new StringBuilder();
        String str = restaurant.getAddress();
        str = str.replaceAll("\"","");
        str = str.replace("]","");
        str = str.replace("[","");
        txtAddress.setText(str);

        //txtWebsite = (TextView) findViewById(R.id.webbAdressField);
        //txtWebsite.setText(restaurant.getWebsite());

        txtRating = (TextView) findViewById(R.id.rating);
        txtRating.setText(""+restaurant.getRating());

    }
}
