package se.group14.foodfinder;

//import android.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by filipheidfors on 2017-04-17.
 * @author Filip Heidfors,
 * Klassen hanterar visning av restauranger på google maps, samt användarens position.
 */

public class ResultMapView extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener{
    private Activity activity;
    private ArrayList<Restaurant> restaurants;
    private MapView mapView;
    private GoogleMap mGoogleMap;
    private View view;
    private HashMap<Marker, Restaurant> hashMap = new HashMap<Marker, Restaurant>();
    private LatLng userPosition;

    /**
     * Metoden körs när vyn skapas
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return vyn
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.result_map_view, container, false);
        return view;
    }

    /**
     * Metoden körs när vyn ÄR skapad och ska då instansiera mapView
     * @param view
     * @param savedInstanceState
     */
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.mapView);

        if(mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    /**
     * Metoden körs när google maps är redo att visas
     * @param googleMap
     */
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        try{
            mGoogleMap.setMyLocationEnabled(true);
        }catch (SecurityException se) {

        }
        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPosition, 15));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(55.609069,12.994678), 15));
        mGoogleMap.setOnInfoWindowClickListener(this);
        addMarkers();
    }

    /**
     * Metoden används för att skicka data till den här klassen
     * @param restaurants En arraylist med restaurangobjekt som ska visas på kartan
     * @param latlng Användarens position
     * @param a En activity (ResultActivity)
     */
    public void setArguments(ArrayList<Restaurant> restaurants, LatLng latlng, Activity a) {
        this.restaurants = restaurants;
        userPosition = latlng;
        activity = a;
    }

    /**
     * Metoden ska lägga till markers/pins på kartan och
     * varje marker ska vara nyckel till ett restaurangobjekt
     */
    public void addMarkers() {
        Marker marker;
        MarkerOptions markerOptions;
        LatLng latlng;

        for(int i = 0; i < restaurants.size(); i++) {
            latlng = new LatLng(restaurants.get(i).getLatitude(), restaurants.get(i).getLongitude());
            markerOptions = new MarkerOptions(); //.position(latlng).title(restaurants.get(i).getName()).snippet(restaurants.get(i).getDistance() + " meter");
            markerOptions.position(latlng);
            markerOptions.title(restaurants.get(i).getName());
            markerOptions.snippet("Avstånd: " +restaurants.get(i).getDistance() + " meter Betyg: " + restaurants.get(i).getRating() + "/10");
            marker = mGoogleMap.addMarker(markerOptions);
            hashMap.put(marker,restaurants.get(i));
        }
    }

    /**
     * Metoden hanterar klick på en marker. Ska öppna en ny activity (InformationActivity) som visar
     * info om vald restaurang
     * @param marker Markern användaren klickade på
     */
    public void onInfoWindowClick(Marker marker) {
        System.out.println("RESTAURANG MAN KLICKADE PÅ " + hashMap.get(marker).getName());
        Intent intent = new Intent(activity, InformationActivity.class);
        intent.putExtra("restaurant", hashMap.get(marker));
        startActivity(intent);
    }
}
