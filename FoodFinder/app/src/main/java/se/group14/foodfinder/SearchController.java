package se.group14.foodfinder;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by AlexanderJD on 2017-03-17.
 */

public class SearchController extends AsyncTask<String, Void, Void> {
    private int distance, price;
    private double latitude, longitude;
    private MainActivity mainActivity;
    private static final String API = "https://api.foursquare.com/v2/venues/search?ll=";
    private static final String CLIENT_ID = "QTBTJY4EUWO0TROZGBRZ4I1YZN51DCG4UMM11IBUCWFLHVXF";
    private static final String CLIENT_SECRET = "EX42ZK4A210FHPKT5SK1VXHJCDNAEOXYZUVECOEU1PFNIBEB";
    private ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
    //private Restaurant restaurant;

    public SearchController(int distance, int price, MainActivity ma, double latitude, double longitude) {
        this.distance=distance;
        this.price=price;
        this.latitude = latitude;
        this.longitude = longitude;
        mainActivity = ma;
        System.out.println("distance: "+distance + " Pris: " + price + " Lat: "+ latitude + " Lon: " + longitude);
        getData();
    }

    public void getData() {
        //execute(API+latitude+","+longitude+"&categoryId=4d4b7105d754a06374d81259&radius="+distance+"&intent=browse&client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET+"&v=20170331");
        execute(API+"40.7,-74&categoryId=4d4b7105d754a06374d81259&radius="+distance+"&intent=browse&client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET+"&v=20170331");
    }

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
     * @param params url'en till API'et som används för att hämta data
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

        }catch (MalformedURLException e) {
            //e.printStackTrace();
            System.out.println("MALFORMED URL");
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("IOEXCEPTION");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        System.out.println("onPostExecute metoden");
        for(int i = 0; i < restaurants.size(); i++) {
            new VenueHandler(restaurants.get(i),restaurants.get(i).getId(),i).start();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Intent intent = new Intent(this, ResultActivity.class);
        //startActivity(intent);
        mainActivity.openActivity(restaurants);
    }


    private class VenueHandler extends Thread {
        private Restaurant restaurant;
        private String id;
        private int index;

        public VenueHandler(Restaurant restaurant, String id, int i) {
            this.id = id;
            index = i;
            this.restaurant = restaurant;
            System.out.println("VENUEHANDLER: " + this.restaurant.getName());
        }

        public void run() {
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

                if (contact.has("phone")) {
                    restaurant.setPhone(contact.getString("phone"));
                    System.out.println("TELEFONNUMMER: " + restaurant.getPhone());
                }


            } catch (IOException e) {
                System.out.println("HeJ, Funkar ej");
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("HeJ, Funkar ej JSON");
            }
        }
    }

}
