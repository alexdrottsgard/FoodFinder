package se.group14.foodfinder;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener , View.OnClickListener{
    private Spinner priceSpinner;
    private int chosenPrice, chosenDistance;
    private double longitude;
    private double latitude;
    private Button searchButton;
    private EditText distanceField;
    private static final String[] priceClass = {"Prisklass 1", "Prisklass 2", "Prisklass 3", "Prisklass 4"};
    private Button testBtn;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        priceSpinner = (Spinner) findViewById(R.id.priceSpinner);
        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);
        distanceField = (EditText) findViewById(R.id.distance);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                System.out.println("onLocationChanged");
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                System.out.println("longitude: " + longitude + " + " + "latitude: " + latitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        configureButton();

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, priceClass);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            priceSpinner.setAdapter(adapter);
            priceSpinner.setOnItemSelectedListener(this);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                    configureButton();
                break;
            default:
                break;
        }
    }

    private void configureButton() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            System.out.println("configureButton death");
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);

            /*
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("onClick method");

                    chosenDistance = Integer.parseInt(distanceField.getText().toString());
                    new SearchController(chosenDistance, chosenPrice, MainActivity.this, latitude, longitude);
                }
            });
            */
    }






    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                chosenPrice = position+1;
                System.out.println(chosenPrice);
                break;
            case 1:
                chosenPrice = position+1;
                System.out.println(chosenPrice);
                break;
            case 2:
                chosenPrice = position+1;
                System.out.println(chosenPrice);
                break;
            case 3:
                chosenPrice = position+1;
                System.out.println(chosenPrice);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


   @Override
    public void onClick(View v) {
       //locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        v = searchButton;
        chosenDistance = Integer.parseInt(distanceField.getText().toString());
        new SearchController(chosenDistance, chosenPrice, this, getLatitude(), getLongitude());
    }



    public void showAlert(String str) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(str)
                .setCancelable(true);
        AlertDialog alert = builder.create();
        alert.show();

    }
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return  longitude;
    }

}
