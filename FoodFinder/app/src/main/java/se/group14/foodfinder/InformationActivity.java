package se.group14.foodfinder;
/**
 * Created by Alexander J. Drottsgård on 2017-03-31.
 */

import android.app.*;
import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;


public class InformationActivity extends Activity implements OnMapReadyCallback {
    private Restaurant restaurant;
    private TextView txtName, txtAddress, txtRating;
    private Button btnCall, btnWeb, btnGetHere;
    private GoogleMap mGoogleMap;
    private MapView mapView;

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

        mapView = (MapView) findViewById(R.id.mapView);

        if(mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        LatLng pos = new LatLng(restaurant.getLatitude(),restaurant.getLongitude());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
        mGoogleMap.addMarker(new MarkerOptions().position(pos).title(restaurant.getName()));
    }

    /**
     * Lyssnare för klick på ringknappen. Ska öppna telefonappen och ringa restaurang
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

    private class ButtonGetHereListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr="+restaurant.getLatitude()+","+restaurant.getLongitude()));
            startActivity(intent);
        }
    }
}
