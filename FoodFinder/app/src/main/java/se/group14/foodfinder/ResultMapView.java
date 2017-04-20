package se.group14.foodfinder;

//import android.app.Fragment;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by filipheidfors on 2017-04-17.
 */

public class ResultMapView extends Fragment implements OnMapReadyCallback{
    private Activity activity;
    private ArrayList<Restaurant> restaurants;
    private MapView mapView;
    private GoogleMap mGoogleMap;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.result_map_view, container, false);

        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        //addMarkers();
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

        LatLng pos = new LatLng(55.609069,12.99467);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 12));
        addMarkers();
    }

    public void setArguments(ArrayList<Restaurant> restaurants, Activity a) {
        this.restaurants = restaurants;
        activity = a;
    }

    public void addMarkers() {
        for(int i = 0; i < restaurants.size(); i++) {
            LatLng latlng = new LatLng(restaurants.get(i).getLatitude(), restaurants.get(i).getLongitude());
            mGoogleMap.addMarker(new MarkerOptions().position(latlng));
        }
    }
}
