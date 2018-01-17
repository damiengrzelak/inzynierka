package wfiis.pizzerialesna.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.inverce.mod.core.IM;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.base.BaseFragment;

public class ContactFragment extends BaseFragment implements View.OnClickListener, OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;

    public static ContactFragment newInstance() {
        return new ContactFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        assert getActions() != null;

        findViews(view);
        setListeners();
        initMap(view, savedInstanceState);

        getActions().topBar().showBackIcon(true);
        return view;
    }

    private void initMap(View view, Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        if (mapView != null) {
            mapView.getMapAsync(this);
        }
    }

    private void setListeners() {
    }

    private void findViews(View view) {
        mapView = view.findViewById(R.id.map);
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(IM.context(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(IM.context(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        Location myLocation = map.getMyLocation();
        if (myLocation != null) {
            map.addMarker(new MarkerOptions().position(
                    new LatLng(52.2497539, 19.170143))
                    .title("Pizzeria Leśna")
                    .icon(BitmapDescriptorFactory.defaultMarker()));

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.2497539, 19.170143), 8));
        }
    }
}