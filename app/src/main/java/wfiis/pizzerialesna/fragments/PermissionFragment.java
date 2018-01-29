package wfiis.pizzerialesna.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import wfiis.pizzerialesna.R;
import wfiis.pizzerialesna.base.BaseFragment;

public class PermissionFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    private Switch localizationSwitch;
    private Switch phoneSwitch;

    public static PermissionFragment newInstance() {
        return new PermissionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_permissions, container, false);
        assert getActions() != null;

        findViews(view);
        setListeners();
        prepareView();

        getActions().topBar().showBackIcon(true);
        getActions().topBar().showMenuIcon(false);
        return view;
    }

    private void setListeners() {
        phoneSwitch.setOnCheckedChangeListener(this);
    }

    private void findViews(View view) {
        localizationSwitch = view.findViewById(R.id.fragment_permission_localization_switch);
        phoneSwitch = view.findViewById(R.id.fragment_permission_phone_switch);
    }

    private void prepareView() {
        String locationPerission = android.Manifest.permission.ACCESS_FINE_LOCATION;
        int res = getContext().checkCallingOrSelfPermission(locationPerission);

        if (res == PackageManager.PERMISSION_GRANTED) {
            localizationSwitch.setChecked(true);
        } else {
            localizationSwitch.setChecked(false);
        }

        String phonePermission = Manifest.permission.CALL_PHONE;
        res = getContext().checkCallingOrSelfPermission(phonePermission);
        if (res == PackageManager.PERMISSION_GRANTED) {
            phoneSwitch.setChecked(true);
        } else {
            phoneSwitch.setChecked(false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();

        switch (id){
            case R.id.fragment_permission_localization_switch:
                break;
            case R.id.fragment_permission_phone_switch:
                phonePermChange(b);
                break;
        }
    }

    private void phonePermChange(boolean isOn) {
        if(isOn){
            //
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},
                    10);
        }
    }
}
