package wfiis.pizzerialesna.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.inverce.mod.core.IM;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.base.BaseFragment;

public class ContactFragment extends BaseFragment implements View.OnClickListener, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    public static String TAG = "fragment_contact";
    private TextView phone;
    private MapView mapView;

    public static ContactFragment newInstance() {
        return new ContactFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        assert getActions() != null;
        findViews(view);
        prepareViews();
        setListeners();
        initMap(view, savedInstanceState);

        getActions().topBar().showBackIcon(true);
        getActions().topBar().showMenuIcon(false);
        return view;
    }

    private void prepareViews() {
        SpannableString content = new SpannableString(phone.getText());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        phone.setText(content);
    }

    private void initMap(View view, Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);

        mapView.onCreate(savedInstanceState);
        if (mapView != null) {
            mapView.getMapAsync(this);
        }
    }

    private void setListeners() {
        phone.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE},
                        10);
                return;
            } else {
                try {
                    callToRestaurant();
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(IM.application().getApplicationContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void callToRestaurant() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone.getText()));

        startActivity(callIntent);
    }

    private void findViews(View view) {
        mapView = view.findViewById(R.id.map);
        phone = view.findViewById(R.id.fragment_contact_phone);
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

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
        if (ContextCompat.checkSelfPermission(IM.context(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(IM.context(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);

        map.addMarker(new MarkerOptions().position(
                new LatLng(52.2497539, 19.170143))
                .title(IM.context().getResources().getString(R.string.app_name))
                .icon(BitmapDescriptorFactory.defaultMarker()));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.2497539, 19.170143), 18));
    }

}