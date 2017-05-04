package se.group14.foodfinder;
/**
 * Created by Alexander J. Drottsgård on 2017-03-31.
 * @author Filip Heidfors, John Eklöf,
 */

import android.Manifest;
import android.app.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

/**
 * Klassen är en activity där information om en restaurang finns så som
 * namn, adress, betyg, telefonnummer, öppet/stängt, vägbeskrivning och hemsida
 */
public class InformationActivity extends Activity implements OnMapReadyCallback{
    private Restaurant restaurant;
    private TextView txtName, txtAddress, txtRating, txtOpen;
    private Button btnCall, btnWeb, btnGetHere;
    private GoogleMap mGoogleMap;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_information2);

        Intent intent = getIntent();
        restaurant = (Restaurant) intent.getSerializableExtra("restaurant");

        //Toast.makeText(getApplicationContext(),"Prisklass: " + restaurant.getPrice() + "Avstånd: " + restaurant.getDistance(),Toast.LENGTH_LONG).show();

        txtName = (TextView) findViewById(R.id.restaurantName);
        txtName.setText(restaurant.getName());

        txtOpen = (TextView) findViewById(R.id.open);
        txtOpen.setText(restaurant.getOpen());
        if(restaurant.getOpen().equals("Öppet")) {
            txtOpen.setTextColor(Color.GREEN);
        }else {
            txtOpen.setTextColor(Color.RED);
        }

        txtAddress = (TextView) findViewById(R.id.adressField);

        String str = restaurant.getAddress();
        str = str.replaceAll("\"", "");
        str = str.replace("]", "");
        str = str.replace("[", "");
        //txtAddress.setText(str);

        txtRating = (TextView) findViewById(R.id.rating);
        txtRating.setText("" + restaurant.getRating());

        btnCall = (Button) findViewById(R.id.btn_call);
        btnCall.setOnClickListener(new ButtonCallListener());
        btnGetHere = (Button) findViewById(R.id.btn_get_here);
        btnGetHere.setOnClickListener(new ButtonGetHereListener());
        btnWeb = (Button) findViewById(R.id.btn_website);
        btnWeb.setOnClickListener(new ButtonWebsiteListener());

        mapView = (MapView) findViewById(R.id.mapView);

        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    /**
     * Metod som körs när google maps är redo att visas
     * @param googleMap GoogleMap
     */
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        try{
            mGoogleMap.setMyLocationEnabled(true);
        }catch (SecurityException se) {

        }
        LatLng pos = new LatLng(restaurant.getLatitude(),restaurant.getLongitude());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
        mGoogleMap.addMarker(new MarkerOptions().position(pos).title(restaurant.getName()));
    }

    /**
     * Lyssnare för klick på ringknappen. Ska öppna telefonappen med restaurangens
     * telefonnummer inmatat
     */
    private class ButtonCallListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            try {
                if(restaurant.getPhone() != null) {
                    Intent ci = new Intent(Intent.ACTION_DIAL);
                    ci.setData(Uri.parse("tel:" + restaurant.getPhone()));
                    InformationActivity.this.startActivity(ci);
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(InformationActivity.this);
                    builder.setMessage("Telefonnummer är tyvärr ej tillgängligt för den här restaurangen ¯\\_(ツ)_/¯")
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

    /**
     * Lyssnare för hemsidaknappen. Ska öppna hemsida till restaurangen
     * i extern webbläsare
     */
    private class ButtonWebsiteListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if(restaurant.getWebsite() != null) {
                Uri uri = Uri.parse(restaurant.getWebsite());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(InformationActivity.this);
                builder.setMessage("Hemsida är tyvärr ej tillgänglig för den här restaurangen ¯\\_(ツ)_/¯")
                        .setCancelable(true)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    /**
     * Lyssnare för vägbeskrivningsknapp. Ska öppna vägbeskrivning i extern kartapp
     */
    private class ButtonGetHereListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr="+restaurant.getLatitude()+","+restaurant.getLongitude()));
            startActivity(intent);
        }
    }
}
