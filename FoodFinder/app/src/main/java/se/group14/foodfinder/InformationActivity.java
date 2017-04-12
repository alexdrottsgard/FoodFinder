package se.group14.foodfinder;
/**
 * Created by Alexander J. Drottsgård on 2017-03-31.
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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

    /**
     * Lyssnare för klick på ringknappen. Ska öppna telefonappen och ringa restaurang
     */
    private class ButtonCallListener extends Activity implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            try {
                if(restaurant.getPhone() != null) {
                    Intent ci = new Intent(Intent.ACTION_DIAL);
                    ci.setData(Uri.parse("tel:" + restaurant.getPhone()));
                    startActivity(ci);
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(InformationActivity.this);
                    builder.setMessage("Telefonnummer finns tyvärr ej tillgängligt för restaurangen")
                            .setCancelable(true)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

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
