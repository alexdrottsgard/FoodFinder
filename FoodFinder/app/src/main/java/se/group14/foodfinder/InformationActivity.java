package se.group14.foodfinder;
/**
 * Created by Alexander J. Drottsgård on 2017-03-31.
 */

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.widget.*;


public class InformationActivity extends Activity {
    private Restaurant restaurant;
    private TextView txtName;
    private TextView txtAddress;
    private TextView txtWebsite;
    private TextView txtRating;
    private Button btnCall, btnWeb, btnGetHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_information);

        Intent intent = getIntent();
        restaurant = (Restaurant) intent.getSerializableExtra("restaurant");

        txtName = (TextView) findViewById(R.id.restaurantName);
        txtName.setText(restaurant.getName());

        txtAddress = (TextView) findViewById(R.id.adressField);

        StringBuilder stringBuilder = new StringBuilder();
        String str = restaurant.getAddress();
        str = str.replaceAll("\"", "");
        str = str.replace("]", "");
        str = str.replace("[", "");
        txtAddress.setText(str);

        txtRating = (TextView) findViewById(R.id.rating);
        txtRating.setText("" + restaurant.getRating());

        btnCall = (Button) findViewById(R.id.btn_call);
        btnCall.setOnClickListener(new ButtonCallListener());
        btnGetHere = (Button) findViewById(R.id.btn_get_here);
        btnGetHere.setOnClickListener(new ButtonGetHereListener());
        btnWeb = (Button) findViewById(R.id.btn_website);
        btnWeb.setOnClickListener(new ButtonWebsiteListener());
    }

    private class ButtonCallListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            try {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + restaurant.getPhone());
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (ActivityCompat.checkSelfPermission(InformationActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
            } catch (ActivityNotFoundException activityException) {
                //Log.e("Calling a Phone Number", "Call failed", activityException);
            }
        }
    }

    private class ButtonWebsiteListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }

    private class ButtonGetHereListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }
}
