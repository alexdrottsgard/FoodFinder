package se.group14.foodfinder;

import android.accounts.NetworkErrorException;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import java.util.ArrayList;

/**
 * Created by AlexanderJD on 2017-03-17.
 * @author Filip Heidfors, Alexander J. Drottsgård, Elias Moltedo
 * Klassen hanterar en sökning från användaren genom anrop till foursquares API
 */

public class SearchController extends AsyncTask<String, Void, Void> {
    private int distance, price;
    private double latitude, longitude;
    private MainActivity mainActivity;
    private static final String API = "https://api.foursquare.com/v2/venues/search?ll=";
    private static final String CLIENT_ID = "QTBTJY4EUWO0TROZGBRZ4I1YZN51DCG4UMM11IBUCWFLHVXF";
    private static final String CLIENT_SECRET = "EX42ZK4A210FHPKT5SK1VXHJCDNAEOXYZUVECOEU1PFNIBEB";
    private ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
    private ProgressDialog progressDialog;
    private static int error = 0;

    /**
     *
     * @param distance Avståndet användaren valt för sin sökning
     * @param price Prisklassen användaren har valt
     * @param ma Instans av MainActivity
     * @param latitude Användarens latitude
     * @param longitude Användarens longitude
     */
    public SearchController(int distance, int price, MainActivity ma, double latitude, double longitude) {
        this.distance=distance;
        this.price=price;
        this.latitude = latitude;
        this.longitude = longitude;
        mainActivity = ma;
        System.out.println("distance: "+distance + " Pris: " + price + " Lat: "+ latitude + " Lon: " + longitude);
        getData();
    }

    /**
     * Metoden exekverar metoden doInBackground för att nätverksanrop får inte göras på Main/UI tråden
     */
    public void getData() {
        //execute(API+latitude+","+longitude+"&categoryId=4d4b7105d754a06374d81259&radius="+distance+"&intent=browse&client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET+"&v=20170331");
        execute(API+"55.609069,12.994678&categoryId=4d4b7105d754a06374d81259&limit=50&radius="+distance+"&intent=browse&client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET+"&v=20170331");
    }

    /**
     * Metoden gör en sträng av en API'ets inputstream för att användas vid hämtning av
     * data från API
     * @param is
     * @return Inputstreamen som sträng
     * @throws IOException Input/Output exception
     */
    private String streamToString(InputStream is) throws IOException {
        String str  = "";

        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader 	= new BufferedReader(new InputStreamReader(is));

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
            } finally {
                is.close();
            }

            str = sb.toString();
        }

        return str;
    }

    /**
     * @author Filip Heidfors
     * Bakgrundstråd som gör en request till API'et. Får/kan inte göras på Main/UI thread
     * @param params url'en till API'et som används
     * @return null
     */
    @Override
    protected Void doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();

            String response		= streamToString(urlConnection.getInputStream());
            JSONObject jsonObj 	= (JSONObject) new JSONTokener(response).nextValue();

            JSONArray venues	= (JSONArray) jsonObj.getJSONObject("response").getJSONArray("venues");

            int length			= venues.length();
            System.out.println("Antal restauranger som hämtats från foursquare: "+length);

            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    JSONObject venue 	= (JSONObject) venues.get(i);

                    Restaurant restaurant = new Restaurant();
                    restaurant.setName(venue.getString("name"));
                    restaurant.setId(venue.getString("id"));

                    JSONObject location = (JSONObject) venue.getJSONObject("location");

                    restaurant.setAddress(location.getString("formattedAddress"));
                    restaurant.setDistance(location.getInt("distance"));

                    restaurant.setLatitude(Double.valueOf(location.getString("lat")));
                    restaurant.setLongitude(Double.valueOf(location.getString("lng")));

                    restaurants.add(restaurant);
                }
            }

        } catch (MalformedURLException e) {
            //e.printStackTrace();
            System.out.println("MALFORMED URL");
            error = 1;
            mainActivity.errorAlert("Fel med url");
        } catch (ConnectException e) {
            error = 4;
            mainActivity.errorAlert("Ooops! Något gick fel. Kanske internet? ¯\\_(ツ)_/¯");
        } catch (SocketTimeoutException se) {
            error = 5;
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("IOEXCEPTION");
            error = 2;
        } catch (JSONException e) {
            e.printStackTrace();
            error = 3;
            mainActivity.errorAlert(e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(mainActivity);
        progressDialog.setMessage("Söker...");
        progressDialog.setProgressStyle(ProgressDialog.BUTTON_NEGATIVE);
        progressDialog.show();
    }

    /**
     * Metoden körs när doInBackground körts klart i bakgrundstråden och datan hämtats och
     * hanterats (eller misslyckats hämtas) från APIet.
     * @param aVoid
     */
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        System.out.println("onPostExecute metoden");
        switch (error) {
            case 0:
                if(restaurants.size() > 0) {
                    for(int i = 0; i < restaurants.size(); i++) {
                        new VenueHandler(restaurants.get(i),restaurants.get(i).getId(), i).start();
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }else {
                    mainActivity.errorAlert("Ooops! Sökningen gav inget resultat ¯\\_(ツ)_/¯ \nPröva öka ditt avstånd");
                    mainActivity.getSearchButton().setEnabled(true);
                    progressDialog.dismiss();
                }
                break;
            default:
                mainActivity.errorAlert("Ooops! Kunde inte ansluta till servern ¯\\_(ツ)_/¯");
                mainActivity.getSearchButton().setEnabled(true);
                progressDialog.dismiss();
                error = 0;
        }

    }

    /**
     * Inre klass som är en tråd som har i uppgift att göra en ny request till API för varje restaurangs
     * id
     */
    private class VenueHandler extends Thread {
        private Restaurant restaurant;
        private String id;
        private int index;

        /**
         * @param restaurant Restaurant-objekt som attribut ska läggas till på
         * @param id Restaurangens id
         */
        public VenueHandler(Restaurant restaurant, String id, int i) {
            this.id = id;
            this.restaurant = restaurant;
            index = i;
            System.out.println("VENUEHANDLER: " + this.restaurant.getName());
        }

        /**
         * Run metoden för tråden som gör en API request och hanterar datan och lägger till
         * attribut till Restaurang objektet
         */
        public synchronized void run() {
            try {
                String newURL = "https://api.foursquare.com/v2/venues/" + id + "?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&v=20170331";

                URL url = new URL(newURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.connect();

                String response = streamToString(urlConnection.getInputStream());
                JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();

                JSONObject venue = (JSONObject) jsonObj.getJSONObject("response").getJSONObject("venue");

                JSONObject contact = (JSONObject) venue.getJSONObject("contact");

                if(venue.has("price")) {
                    JSONObject price = (JSONObject) venue.getJSONObject("price");
                    restaurant.setPrice(price.getInt("tier"));
                }else {
                    restaurant.setPrice(0);
                }

                if (contact.has("phone")) {
                    restaurant.setPhone(contact.getString("phone"));
                    System.out.println("TELEFONNUMMER: " + restaurant.getPhone());
                }

                if(venue.has("url")) {
                    restaurant.setWebsite(venue.getString("url"));
                    System.out.println("HEMSIDA:::::::::::" + restaurant.getWebsite());
                }

                if(venue.has("rating")) {
                    restaurant.setRating(venue.getDouble("rating"));
                    System.out.println("BETYG:::::::::::" + restaurant.getRating());
                }

                if(venue.has("hours")) {
                    JSONObject hours = venue.getJSONObject("hours");
                    boolean open = hours.getBoolean("isOpen");
                    System.out.println("ÖPPET OR NAH???? :::::::::" + open);
                    if(open) {
                        restaurant.setOpen("Öppet");
                    }else {
                        restaurant.setOpen("Stängt");
                    }

                }else {
                    restaurant.setOpen("");
                }

                if(index == restaurants.size()-1) {
                    mainActivity.openActivity(restaurants);
                    mainActivity.getSearchButton().setEnabled(true);
                    progressDialog.dismiss();
                }

            } catch (IOException e) {
                System.out.println("HeJ, Funkar ej");
                mainActivity.errorAlert("Något gick fel");
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("HeJ, Funkar ej JSON");
                mainActivity.errorAlert("Något gick fel");
            } catch (Exception e) {
                mainActivity.errorAlert("Något gick fel");
            }
        }
    }

}
