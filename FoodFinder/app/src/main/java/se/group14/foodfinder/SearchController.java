package se.group14.foodfinder;

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

public class SearchController {
    private int distance, price;
    private static final String CLIEND_ID = "QTBTJY4EUWO0TROZGBRZ4I1YZN51DCG4UMM11IBUCWFLHVXF";
    private static final String CLIENT_SECRET = "EX42ZK4A210FHPKT5SK1VXHJCDNAEOXYZUVECOEU1PFNIBEB";
    private ArrayList<Restaurant> restaurants;
    private String test;

    public SearchController(int distance, int price) {
        this.distance=distance;
        this.price=price;
        System.out.println(distance + " " + price);
        getData();
    }

    public void getData() {
        try {
            URL url = new URL("https://api.foursquare.com/v2/venues/search?ll=40.7,-74&section=food&radius="+distance+"&client_id="+CLIEND_ID+"&client_secret="+CLIENT_SECRET+"&v=20170331");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            String response		= streamToString(urlConnection.getInputStream());
            JSONObject jsonObj 	= (JSONObject) new JSONTokener(response).nextValue();

            JSONArray groups	= (JSONArray) jsonObj.getJSONObject("response").getJSONArray("groups");

            int length			= groups.length();

            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    JSONObject group 	= (JSONObject) groups.get(i);
                    JSONArray items 	= (JSONArray) group.getJSONArray("items");

                    int ilength 		= items.length();

                    for (int j = 0; j < ilength; j++) {
                        JSONObject item = (JSONObject) items.get(j);

                        Restaurant restaurant 	= new Restaurant();

                        restaurant.setName(item.getString("name"));

                        JSONObject location = (JSONObject) item.getJSONObject("location");

                        restaurant.setLatitude(Double.valueOf(location.getString("lat")));
                        restaurant.setLatitude(Double.valueOf(location.getString("lng")));

                        restaurant.setAddress(location.getString("address"));
                        restaurant.setDistance(location.getInt("distance"));


                        //JSONObject category = (JSONObject) item.getJSONObject("categorie");
                        //restaurant.setCategory(lo);

                        restaurants.add(restaurant);

                        System.out.println(restaurants.toArray());
                    }
                }
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
}
