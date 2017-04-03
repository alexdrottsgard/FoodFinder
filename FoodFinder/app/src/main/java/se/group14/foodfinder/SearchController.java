package se.group14.foodfinder;

import android.os.AsyncTask;

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
    private String test;

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
        execute(API+latitude+","+longitude+"&categoryId=4d4b7105d754a06374d81259&radius="+distance+"&intent=browse&client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET+"&v=20170331");
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

                    //getVenueInfo(restaurant, venue.getString("id"));
                    new VenueHandler(restaurant,venue.getString("id")).start();

                    restaurant.setName(venue.getString("name"));

                    String id = venue.getString("id");

                    JSONObject location = (JSONObject) venue.getJSONObject("location");

                    restaurant.setLatitude(Double.valueOf(location.getString("lat")));
                    restaurant.setLongitude(Double.valueOf(location.getString("lng")));

                    restaurant.setAddress(location.getString("formattedAddress"));
                    restaurant.setDistance(location.getInt("distance"));

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

    protected void getVenueInfo(Restaurant restaurant, String id) {
       try {
           String newURL = "https://api.foursquare.com/v2/venues/" + id + "?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&v=20170331";


           URL url = new URL(newURL);
           HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
           urlConnection.setRequestMethod("GET");
           urlConnection.setDoInput(true);
           urlConnection.connect();

           String response      = streamToString(urlConnection.getInputStream());
           JSONObject jsonObj   = (JSONObject) new JSONTokener(response).nextValue();

           //JSONArray venue	    = (JSONArray) jsonObj.getJSONObject("response").getJSONArray("venue");
           JSONObject venue         = (JSONObject) jsonObj.getJSONObject("response");

           System.out.println(venue.get("contact").toString());
           System.out.println("BAJSTOLLE "+venue.toString());

           /*
           JSONObject contact = (JSONObject) venue.getJSONObject("contact");
           restaurant.setPhone(contact.getString("phone"));
           System.out.println("Telefonnummer: "+contact.getString("phone"));
           */

       }catch (IOException e){
           System.out.println("HeJ, Funkar ej");
       }catch (JSONException e){
           e.printStackTrace();
           System.out.println("HeJ, Funkar ej JSON");
       }
    }

    private class VenueHandler extends Thread {
        private Restaurant restaurant;
        private String id;

        public VenueHandler(Restaurant restaurant, String id){
            this.id = id;
            this.restaurant = restaurant;
        }

        public void run() {
            try {
                String newURL = "https://api.foursquare.com/v2/venues/" + id + "?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&v=20170331";


                URL url = new URL(newURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.connect();

                String response      = streamToString(urlConnection.getInputStream());
                JSONObject jsonObj   = (JSONObject) new JSONTokener(response).nextValue();

                //JSONArray venue	    = (JSONArray) jsonObj.getJSONObject("response").getJSONArray("venue");
                JSONObject venue         = (JSONObject) jsonObj.getJSONObject("response");

                try{
                    //System.out.println(venue.get("contact").toString());
                    JSONObject contact = (JSONObject) venue.getJSONObject("contact");
                    String phone = (String) venue.getJSONObject("contact").getString("phone");
                    System.out.println("TELEFON: " + phone);
                    //System.out.println("TELEFON: " + contact.getString("phone"));
                }catch(Exception e){
                    System.out.println("Exception för contact");
                }

                System.out.println("BAJSTOLLE "+venue.toString());

           /*
           JSONObject contact = (JSONObject) venue.getJSONObject("contact");
           restaurant.setPhone(contact.getString("phone"));
           System.out.println("Telefonnummer: "+contact.getString("phone"));
           */

            }catch (IOException e){
                System.out.println("HeJ, Funkar ej");
            }catch (JSONException e){
                e.printStackTrace();
                System.out.println("HeJ, Funkar ej JSON");
            }
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        System.out.println("onPostExecute metoden");


        StringBuilder str = new StringBuilder();

        for(int i = 0; i < restaurants.size(); i++){
            str.append("\nRestarang " + i + ":");
            str.append("Namn: "+restaurants.get(i).getName());
            str.append("Adress: "+restaurants.get(i).getAddress());
            str.append("Avstånd: "+restaurants.get(i).getDistance());

            System.out.println("\nRestarang " + i + ":\n");
            System.out.println("\nNamn: "+restaurants.get(i).getName()+"\n");
            System.out.println("\nAdress: "+restaurants.get(i).getAddress()+"\n");
            System.out.println("\nAvstånd: "+restaurants.get(i).getDistance()+"\n");
        }

        new ResultActivity(restaurants);

        //mainActivity.showAlert(str.toString());

    }
}
