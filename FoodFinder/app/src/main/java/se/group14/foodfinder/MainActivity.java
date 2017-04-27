package se.group14.foodfinder;

import android.Manifest;
import android.app.*;
import android.content.DialogInterface;
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
import android.widget.*;


import java.util.ArrayList;
import java.util.Random;

/**
 * @author Filip Heidfors, Alexander J. Drottsgård
 * Applikationen startar i den här klassen. GUI för startsida där användare kan göra en sökning filtrerat
 * på prisklass och avstånd. (Prisklass ej implementerat ännu)
 */
public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {
    private Spinner priceSpinner;
    private Button categoryButton;
    private int chosenPrice = 4, chosenDistance = 1000;
    private double longitude, latitude;
    private Button searchButton, randomButton;
    private EditText distanceField;
    private static final String[] priceClass = {"Välj prisklass", "Luffare", "Arbetarklass", "Medelklass", "Överklass"};
    final CharSequence[] categories = {"Asiatiskt", "Hamburgare", "Husmanskost", "Italienskt"};
    final ArrayList seletedCategories = new ArrayList();
    private LocationManager locationManager;
    private LocationListener locationListener;
    private boolean random = false;
    private String selectedCategories;

    /**
     * Metoden startar klassen
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        priceSpinner = (Spinner) findViewById(R.id.priceSpinner);
        categoryButton = (Button) findViewById(R.id.categoryButton);
        categoryButton.setOnClickListener(new CategoryBtnListener());
        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new SearchButtonListener());
        randomButton = (Button) findViewById(R.id.randomButton);
        randomButton.setOnClickListener(new RandomButtonListener());
        distanceField = (EditText) findViewById(R.id.distance);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {

            /**
             * Inre metod som hämtar latitude och longitude och sparar i instansvariablar.
             * @param location
             */
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

            /**
             * Inre metod som öppnar mobilens plats-inställingar om de är avstängda
             * @param provider
             */
            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        configureButton();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, priceClass);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_multichoice);
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

    /**
     * Kollar igenom manifestvillkor och om dessa inte uppfylls så går metoden vidare och uppdaterar koordinater med ett metodanrop.
     */
    private void configureButton() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            System.out.println("configureButton death");
            return;
        }

        //uppdaterar koordinater, görs även var femte sekund.
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);
    }

    /**
     * Metoden ska hantera användares val av prisklass från spinnern och lagra valet i en instansvariabel
     *
     * @param parent
     * @param view
     * @param position position/index för spinnern
     * @param id       id för spinnern
     */
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                chosenPrice = 4;
                break;
            case 1:
                chosenPrice = position;
                System.out.println(chosenPrice);
                break;
            case 2:
                chosenPrice = position;
                System.out.println(chosenPrice);
                break;
            case 3:
                chosenPrice = position;
                System.out.println(chosenPrice);
                break;
            case 4:
                chosenPrice = position;
                System.out.println(chosenPrice);
                break;
        }
    }

    /**
     * Metoden måste finnas med för klassen implementerar Adapterview
     * men vi har ingen användning för den
     *
     * @param parent
     */
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private class CategoryBtnListener implements View.OnClickListener {

        AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Välj kategori")
                .setMultiChoiceItems(categories, null, new DialogInterface.OnMultiChoiceClickListener() {

                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            seletedCategories.add(indexSelected);
                        } else if (seletedCategories.contains(indexSelected)) {
                            // Else, if the item is already in the array, remove it
                            seletedCategories.remove(Integer.valueOf(indexSelected));
                        }
                    }
                }).setPositiveButton("Välj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on OK
                        //  You can write the code  to save the selected item here
                    }
                }).setNegativeButton("Stäng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                    }
                });

        AlertDialog categoryAlert = ab.create();

        @Override
        public void onClick(View v) {
            categoryAlert.show();
        }
    }

    private class SearchButtonListener implements View.OnClickListener {
        /**
         * Metoden hanterar klick på sökknappen och startar SearchController
         * som hanterar en sökning med användarens position samt valda prisklass och avstånd
         *
         * @param v
         */
        public void onClick(View v) {
            try {
                chosenDistance = Integer.parseInt(distanceField.getText().toString());
            } catch (Exception e) {
            }
            random = false;
            new SearchController(chosenDistance, chosenPrice, MainActivity.this, getLatitude(), getLongitude());
        }
    }

    private class RandomButtonListener implements View.OnClickListener {
        /**
         * Metoden hanterar klick på slumpknappen och startar SearchController
         * som hanterar en sökning med användarens position samt valda prisklass och avstånd
         *
         * @param v
         */
        public void onClick(View v) {
            try {
                chosenDistance = Integer.parseInt(distanceField.getText().toString());
            } catch (Exception e) {
            }
            random = true;
            new SearchController(chosenDistance, chosenPrice, MainActivity.this, getLatitude(), getLongitude());
        }
    }

    public void errorAlert(String str) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(str)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * @return Användarens latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Användarens longitude
     *
     * @return
     */
    public double getLongitude() {
        return longitude;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    /**
     * Metoden öppnar en ny activity som ska visa sökresultatet
     * En lista med Restaurant-objekt som ska visas skickas med
     * (eller gå direkt till InformationActivity om det är slumpat)
     *
     * @param restaurants
     */
    public void openActivity(ArrayList<Restaurant> restaurants) {
        if (random) {
            Random rand = new Random();
            ArrayList<Restaurant> newRestaurants = new ArrayList<Restaurant>();
            for (int i = 0; i < restaurants.size(); i++) {
                if (restaurants.get(i).getPrice() <= chosenPrice) {
                    newRestaurants.add(restaurants.get(i));
                }
            }
            Intent intent = new Intent(this, InformationActivity.class);
            intent.putExtra("restaurant", restaurants.get(rand.nextInt(newRestaurants.size())));
            intent.putExtra("lat", getLatitude());
            intent.putExtra("lng", getLongitude());
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("arrayList", restaurants);
            intent.putExtra("price", chosenPrice);
            intent.putExtra("lat", getLatitude());
            intent.putExtra("lng", getLongitude());
            startActivity(intent);
        }
    }
    }



