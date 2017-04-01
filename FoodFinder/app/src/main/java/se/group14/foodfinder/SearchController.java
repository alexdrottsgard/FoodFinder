package se.group14.foodfinder;

import android.app.AlertDialog;
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
    private MainActivity mainActivity;
    private static final String API = "https://api.foursquare.com/v2/venues/search?ll=";
    private static final String CLIEND_ID = "QTBTJY4EUWO0TROZGBRZ4I1YZN51DCG4UMM11IBUCWFLHVXF";
    private static final String CLIENT_SECRET = "EX42ZK4A210FHPKT5SK1VXHJCDNAEOXYZUVECOEU1PFNIBEB";
    private ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
    private String test;

    public SearchController(int distance, int price, MainActivity ma) {
        this.distance=distance;
        this.price=price;
        mainActivity = ma;
        System.out.println(distance + " " + price);
        getData();
    }

    public void getData() {
        execute(API+"40.7,-74&section=food&radius="+distance+"&client_id="+CLIEND_ID+"&client_secret="+CLIENT_SECRET+"&v=20170331");
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

                    restaurant.setName(venue.getString("name"));

                    JSONObject location = (JSONObject) venue.getJSONObject("location");

                    restaurant.setLatitude(Double.valueOf(location.getString("lat")));
                    restaurant.setLatitude(Double.valueOf(location.getString("lng")));

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

        mainActivity.showAlert(str.toString());

    }
}
