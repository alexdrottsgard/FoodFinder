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
 * @author Filip Heidfors, Alexander J. Drottsgård, John Eklöf, Elias Moltedo
 * Applikationen startar i den här klassen. GUI för startsida där användare kan göra en sökning filtrerat
 * på prisklass och avstånd. (Prisklass ej implementerat ännu)
 */
public class MainActivity extends Activity {
    private int chosenPrice = 4, chosenDistance = 1000;
    private double longitude, latitude;
    private Button searchButton, randomButton,categoryButton, priceButton;;
    private EditText distanceField;
    private static final String[] priceClass = {"$", "$$", "$$$", "$$$$"};
    final CharSequence[] categories = {"Markera alla", "Avmarkera alla","Asiatiskt", "Hamburgare", "Husmanskost", "Italienskt",
            "hduhdud", "hfuheueherw", "hfuh73h3432","hfuh4h43h5","fhuh3u43u2h55",
            "hfuh5uh235uh325"};
    final ArrayList seletedCategories = new ArrayList();
    //private ArrayList<Integer> selectedPrices = new ArrayList<Integer>();
    private LocationManager locationManager;
    private LocationListener locationListener;
    private boolean random = false;
    private String selectedCategories;
    private AlertDialog priceAlert;

    /**
     * Metoden startar klassen
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpPriceAlert();

        categoryButton = (Button) findViewById(R.id.categoryButton);
        categoryButton.setOnClickListener(new CategoryBtnListener());
        priceButton = (Button) findViewById(R.id.priceBtn);
        priceButton.setOnClickListener(new PriceButtonListener());
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
    }

    private class CategoryBtnListener implements View.OnClickListener {

        AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Välj kategori")
                .setMultiChoiceItems(categories, null, new DialogInterface.OnMultiChoiceClickListener() {

                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            if(indexSelected == 0){
                                ListView listView = ((AlertDialog) dialog).getListView();
                                for(int i = 2; i < categories.length; i++) {
                                    listView.setItemChecked(i,true);
                                }
                            }

                            seletedCategories.add(indexSelected);
                        } else if (seletedCategories.contains(indexSelected)) {
                            // Else, if the item is already in the array, remove it
                            seletedCategories.remove(Integer.valueOf(indexSelected));
                        }
                    }
                }).setPositiveButton("Klar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on OK
                        //  You can write the code  to save the selected item here
                    }
                });

        AlertDialog categoryAlert = ab.create();

        @Override
        public void onClick(View v) {
            categoryAlert.show();
        }
    }

    /**
     * Skapar en alertdialog med checkboxar och säger hur den ska hantera klick på de olika raderna
     */
    public void setUpPriceAlert() {
        AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Välj maximal prisklass")
                .setMultiChoiceItems(priceClass, null, new DialogInterface.OnMultiChoiceClickListener() {

                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {

                            // If the user checked the item, add it to the selected items
                            ((AlertDialog) dialog).getListView().setItemChecked(0,false);
                            ((AlertDialog) dialog).getListView().setItemChecked(1,false);
                            ((AlertDialog) dialog).getListView().setItemChecked(2,false);
                            ((AlertDialog) dialog).getListView().setItemChecked(3,false);
                            ((AlertDialog) dialog).getListView().setItemChecked(indexSelected,true);

                            chosenPrice = indexSelected+1;
                            priceButton.setText(priceClass[indexSelected]);
                            System.out.println("VALD PRISKLASS::::::" + chosenPrice);
                    }
                }).setPositiveButton("Klar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on OK
                        //  You can write the code  to save the selected item here
                    }
                });

        priceAlert = ab.create();
    }

    /**
     * Lyssnare för prisklass knappen
     */
    private class PriceButtonListener implements View.OnClickListener {

        public void onClick(View v) {
            priceAlert.show();
            priceAlert.getListView().setItemChecked(chosenPrice-1,true);
        }
    }

    /**
     * Lyssnare för sökknappen
     */
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

    /**
     * Lyssnare för slumpaknappen
     */
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

    /**
     * Metoden ska visa en alertdialog med ett felmeddelande
     * @param str Meddelandet som ska visas
     */
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
        System.out.println("RESTAURANGER EFTER FILTRERING::::::" + restaurants.size());

        if(random) {
            System.out.println("RANDOM");
            Random rand = new Random();

            if(restaurants.size() > 0) {
                Intent intent = new Intent(this, InformationActivity.class);
                intent.putExtra("restaurant", restaurants.get(rand.nextInt(restaurants.size())));
                intent.putExtra("lat", getLatitude());
                intent.putExtra("lng", getLongitude());
                startActivity(intent);
            }else {
                errorAlert("SÖK PÅ FLER PRISKLASSER");
            }

        }else {

            if(restaurants.size() > 0) {
                Intent intent = new Intent(this, ResultActivity.class);
                intent.putExtra("arrayList", restaurants);
                intent.putExtra("lat", getLatitude());
                intent.putExtra("lng", getLongitude());
                startActivity(intent);
            }else {
                errorAlert("SÖK PÅ FLER PRISKLASSER");
            }
        }
    }
}