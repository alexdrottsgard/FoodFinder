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
import java.util.HashMap;

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
    private ArrayList<Restaurant> tempRestaurants = new ArrayList<Restaurant>();
    private ArrayList<Integer> selectedPrices;
    private ProgressDialog progressDialog;
    private static int error = 0;
    private String categories;
    private HashMap<String,String> categoryMap = new HashMap<String, String>();

    /**
     *
     * @param distance Avståndet användaren valt för sin sökning
     * @param price Användarens val av prisklass
     * @param ma Instans av MainActivity
     * @param latitude Användarens latitude
     * @param longitude Användarens longitude
     */
    public SearchController(int distance, int price, MainActivity ma, double latitude, double longitude, ArrayList<String> selectedCategories) {
        this.distance = distance;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
        mainActivity = ma;

        categories = getCategoriesString(selectedCategories);
        System.out.println(categories + " i searchcontroller");

        //System.out.println("distance: "+distance + " Pris: " + price + " Lat: "+ latitude + " Lon: " + longitude);
        getData();
    }

    public String getCategoriesString(ArrayList<String> selectedCategories) {
        categoryMap.put("Asiatiskt", "4bf58dd8d48988d142941735");
        categoryMap.put("Hamburgare", "4bf58dd8d48988d16c941735");
        categoryMap.put("Vegetarisk/Vegansk", "4bf58dd8d48988d1d3941735");
        categoryMap.put("Italienskt", "4bf58dd8d48988d110941735");
        categoryMap.put("Indiskt", "4bf58dd8d48988d10f941735");
        categoryMap.put("Mexikanskt", "4bf58dd8d48988d1c1941735");
        categoryMap.put("Mellanöstern", "4bf58dd8d48988d115941735");
        categoryMap.put("Kebab", "5283c7b4e4b094cb91ec88d7");
        categoryMap.put("Franskt", "4bf58dd8d48988d10c941735");
        categoryMap.put("Pizza", "4bf58dd8d48988d1ca941735");
        categoryMap.put("Grekiskt", "4bf58dd8d48988d10e941735");
        categoryMap.put("Sallad", "4bf58dd8d48988d1bd941735");
        categoryMap.put("Skandinaviskt", "4bf58dd8d48988d1c6941735");

        StringBuilder categoryBuilder = new StringBuilder();
        for(int i = 0; i < selectedCategories.size(); i ++) {
            if(categoryMap.containsKey(selectedCategories.get(i))) {
                categoryBuilder.append(categoryMap.get(selectedCategories.get(i)) + ",");
            }
        }
        if(categoryBuilder.length() > 0) {
            categoryBuilder.deleteCharAt(categoryBuilder.length()-1);
        }

        return categoryBuilder.toString();
    }

    /**
     * Metoden exekverar metoden doInBackground för att nätverksanrop får inte göras på Main/UI tråden
     */
    public void getData() {

        if(categories.length() > 0 && categories != null) {
            execute(API+"55.609069,12.994678&categoryId="+categories+"&limit=50&radius="+distance+"&intent=browse&client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET+"&v=20170331");
        }else {
            execute(API+"55.609069,12.994678&categoryId=4d4b7105d754a06374d81259&limit=50&radius="+distance+"&intent=browse&client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET+"&v=20170331");
        }
        //execute(API+latitude+","+longitude+"&categoryId=4d4b7105d754a06374d81259&radius="+distance+"&intent=browse&client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET+"&v=20170331");

    }

    /**
     * Metoden gör en sträng av en API'ets inputstream för att användas vid hämtning av
     * data från API
     * @param is Inputstreamen som ska göras till sträng
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

                    JSONArray categories = (JSONArray) venue.getJSONArray("categories");
                    JSONObject category = (JSONObject) categories.get(0);
                    restaurant.setCategory(category.getString("name"));

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

    /**
     * Metoden körs före doInBackground exekverats. Börjar visa en progressdialog för att visa
     * att visa för användaren att systemet arbetar
     */
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

                   // new VenueHandlerTest().execute();
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


    private class VenueHandlerTest extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            Restaurant restaurant;

            for(int i = 0; i < restaurants.size(); i++) {
                restaurant = restaurants.get(i);
                try {
                    String newURL = "https://api.foursquare.com/v2/venues/" + restaurant.getId() + "?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&v=20170331";

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

                    if (venue.has("url")) {
                        restaurant.setWebsite(venue.getString("url"));
                        System.out.println("HEMSIDA:::::::::::" + restaurant.getWebsite());
                    }

                    if (venue.has("rating")) {
                        restaurant.setRating(venue.getDouble("rating"));
                        System.out.println("BETYG:::::::::::" + restaurant.getRating());
                    }

                    if (venue.has("hours")) {
                        JSONObject hours = venue.getJSONObject("hours");
                        boolean open = hours.getBoolean("isOpen");
                        System.out.println("ÖPPET OR NAH???? :::::::::" + open);
                        if (open) {
                            restaurant.setOpen("Öppet");
                        } else {
                            restaurant.setOpen("Stängt");
                        }

                    } else {
                        restaurant.setOpen("");
                    }

                    if (venue.has("price")) {
                        JSONObject price = (JSONObject) venue.getJSONObject("price");
                        restaurant.setPrice(price.getInt("tier"));
                    } else {
                        restaurant.setPrice(0);
                    }
                } catch (IOException e) {
                    System.out.println("HeJ, Funkar ej");
                    //getMainActivity().getSearchButton().setEnabled(true);
                    progressDialog.dismiss();
                    getMainActivity().errorAlert("Något gick fel");
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("HeJ, Funkar ej JSON");
                    //getMainActivity().getSearchButton().setEnabled(true);
                    progressDialog.dismiss();
                    getMainActivity().errorAlert("Något gick fel");
                } catch (Exception e) {
                    getMainActivity().errorAlert("Något gick fel");
                    //getMainActivity().getSearchButton().setEnabled(true);
                    progressDialog.dismiss();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            System.out.println("RESTAURANGER INNAN FILTRERING::::::" + restaurants.size());
            mainActivity.openActivity(filterRestaurants(restaurants));
            mainActivity.getSearchButton().setEnabled(true);
            progressDialog.dismiss();
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

                if(venue.has("price")) {
                    JSONObject price = (JSONObject) venue.getJSONObject("price");
                    restaurant.setPrice(price.getInt("tier"));
                }else {
                    restaurant.setPrice(0);
                }

                if(index == restaurants.size()-1) {
                    Thread.sleep(500);
                    System.out.println("RESTAURANGER INNAN FILTRERING::::::" + restaurants.size());
                    mainActivity.openActivity(filterRestaurants(restaurants));
                    mainActivity.getSearchButton().setEnabled(true);
                    progressDialog.dismiss();
                }

            } catch (IOException e) {
                System.out.println("HeJ, Funkar ej");
                getMainActivity().getSearchButton().setEnabled(true);
                progressDialog.dismiss();
                getMainActivity().errorAlert("Något gick fel");
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("HeJ, Funkar ej JSON");
                getMainActivity().getSearchButton().setEnabled(true);
                progressDialog.dismiss();
                getMainActivity().errorAlert("Något gick fel");
            } catch (Exception e) {
                getMainActivity().errorAlert("Något gick fel");
                getMainActivity().getSearchButton().setEnabled(true);
                progressDialog.dismiss();
            }
        }
    }

    /**
     * Metoden filtrerar bort restauranger vars prisklass är högre än
     * användarens val av prisklass och restauranger vars prisklass = 0
     * @param restaurants Arraylisten av restauranger som ska filtreras
     * @return den nya filtrerade restaurangen
     */
    public ArrayList<Restaurant> filterRestaurants(ArrayList<Restaurant> restaurants) {
        ArrayList<Restaurant> res = new ArrayList<Restaurant>();
        for(int i = 0; i < restaurants.size(); i++) {
            if(restaurants.get(i).getPrice() <= price && restaurants.get(i).getPrice() != 0) {
                res.add(restaurants.get(i));
            }
        }
        return res;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }
}
